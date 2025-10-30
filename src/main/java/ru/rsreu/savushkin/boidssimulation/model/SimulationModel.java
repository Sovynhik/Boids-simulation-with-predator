package ru.rsreu.savushkin.boidssimulation.model;

import ru.rsreu.savushkin.boidssimulation.config.Settings;
import ru.rsreu.savushkin.boidssimulation.entity.Entity;
import ru.rsreu.savushkin.boidssimulation.entity.Fish;
import ru.rsreu.savushkin.boidssimulation.entity.Predator;
import ru.rsreu.savushkin.boidssimulation.event.SimulationListener;
import ru.rsreu.savushkin.boidssimulation.task.FishBehaviorTask;
import ru.rsreu.savushkin.boidssimulation.task.PredatorBehaviorTask;
import ru.rsreu.savushkin.boidssimulation.task.RespawnControllerTask;
import ru.rsreu.savushkin.boidssimulation.task.SimulationLoopTask;

import java.awt.*;
import java.beans.PropertyChangeSupport;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class SimulationModel implements Model {
    private final GameField field = new GameField();

    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final AtomicBoolean isPaused = new AtomicBoolean(false);
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private ExecutorService executor;

    private static final Logger log = com.prutzkow.projectlogger.ProjectLogger.logger;

    public SimulationModel() {
        Random r = new Random();
        for (int i = 0; i < Settings.INITIAL_FISH_COUNT; i++) {
            field.addFish(new Fish(new Point(r.nextInt(Settings.GAME_FIELD_WIDTH), r.nextInt(Settings.GAME_FIELD_HEIGHT))));
        }
    }

    @Override
    public void startSimulation() {
        if (isRunning.get()) return;
        isRunning.set(true);
        isPaused.set(false);

        executor = Executors.newCachedThreadPool();

        for (Entity e : field.getEntities()) {
            if (e instanceof Fish) {
                executor.submit(new FishBehaviorTask((Fish)e, field, isRunning));
            } else if (e instanceof Predator) {
                executor.submit(new PredatorBehaviorTask((Predator)e, field, isRunning));
            }
        }

        executor.submit(new RespawnControllerTask(field, isRunning, executor));
        executor.submit(new SimulationLoopTask(this, isRunning, isPaused));

        log.info("Simulation start.");
    }

    @Override
    public void pauseSimulation() {
        isPaused.set(!isPaused.get());
        log.info(isPaused.get() ? "Pause" : "Continue");
    }

    @Override
    public void stopSimulation() {
        isRunning.set(false);
        if (executor != null) executor.shutdownNow();
        log.info("Simulation stop.");
    }

    @Override
    public GameField getGameField() { return field; }

    @Override
    public void addSimulationListener(SimulationListener listener) {
        pcs.addPropertyChangeListener("updated", evt -> listener.onSimulationUpdated());
    }

    public void fireSimulationUpdated() {
        pcs.firePropertyChange("updated", false, true);
    }

    public boolean isPaused() { return isPaused.get(); }
}