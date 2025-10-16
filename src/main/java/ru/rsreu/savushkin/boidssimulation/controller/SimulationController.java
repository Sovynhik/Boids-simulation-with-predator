package ru.rsreu.savushkin.boidssimulation.controller;

import ru.rsreu.savushkin.boidssimulation.model.Fish;
import ru.rsreu.savushkin.boidssimulation.model.Predator;
import ru.rsreu.savushkin.boidssimulation.state.GameField;
import ru.rsreu.savushkin.boidssimulation.task.FishTask;
import ru.rsreu.savushkin.boidssimulation.task.PredatorTask;
import ru.rsreu.savushkin.boidssimulation.task.RespawnControllerTask;
import ru.rsreu.savushkin.boidssimulation.util.ProjectLogger;

import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimulationController {
    private final GameField field = new GameField();
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private ExecutorService executor;

    public void initialize(int initialFish) {
        field.setController(this); // Устанавливаем ссылку на контроллер
        field.setPredator(new Predator(400, 300, field));
        for (int i = 0; i < initialFish; i++) {
            Fish fish = new Fish(Math.random() * 800, Math.random() * 600, field);
            field.addFish(fish);
            startFishTask(fish);
        }
        ProjectLogger.logger.info("Initialized with " + initialFish + " fishes");
    }

    public void startSimulation() {
        if (!isRunning.get()) {
            isRunning.set(true);
            executor = Executors.newCachedThreadPool();
            for (Fish fish : field.getFishes()) {
                startFishTask(fish);
            }
            executor.submit(new PredatorTask(field.getPredator(), field, isRunning));
            executor.submit(new RespawnControllerTask(field, isRunning));
            ProjectLogger.logger.info("Simulation started");
        }
    }

    public void stopSimulation() {
        if (isRunning.get()) {
            isRunning.set(false);
            if (executor != null) {
                executor.shutdownNow();
            }
            ProjectLogger.logger.info("Simulation stopped");
        }
    }

    public void render(Graphics2D g2d) {
        g2d.setColor(Color.BLUE);
        for (Fish fish : field.getFishes()) {
            g2d.fillOval((int) fish.getX() - 5, (int) fish.getY() - 5, 10, 10);
        }
        g2d.setColor(Color.RED);
        Predator predator = field.getPredator();
        if (predator != null) {
            g2d.fillOval((int) predator.getX() - 10, (int) predator.getY() - 10, 20, 20);
        }

        g2d.setColor(Color.BLACK);
        g2d.drawString("Fishes: " + field.getFishCount(), 10, 20);
    }

    public boolean isRunning() {
        return isRunning.get();
    }

    public void startFishTask(Fish fish) {
        if (executor == null || executor.isShutdown()) {
            executor = Executors.newCachedThreadPool();
            ProjectLogger.logger.config("Recreated executor pool");
        }
        executor.submit(new FishTask(fish, field, isRunning));
        ProjectLogger.logger.config("Started FishTask for fish at (" + fish.getX() + ", " + fish.getY() + ")");
    }
}