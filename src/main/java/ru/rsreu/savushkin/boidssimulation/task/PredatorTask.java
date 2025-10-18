package ru.rsreu.savushkin.boidssimulation.task;

import ru.rsreu.savushkin.boidssimulation.model.Predator;
import ru.rsreu.savushkin.boidssimulation.state.GameField;
import ru.rsreu.savushkin.boidssimulation.util.ProjectLogger;

import java.util.concurrent.atomic.AtomicBoolean;

public class PredatorTask implements Runnable {
    private final Predator predator;
    private final GameField field;
    private final AtomicBoolean isRunning;

    public PredatorTask(Predator predator, GameField field, AtomicBoolean isRunning) {
        this.predator = predator;
        this.field = field;
        this.isRunning = isRunning;
    }

    @Override
    public void run() {
        ProjectLogger.logger.info("Predator thread started");
        while (isRunning.get()) {
            predator.update();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                ProjectLogger.logger.severe("Predator thread interrupted: " + e.getMessage());
            }
        }
        ProjectLogger.logger.info("Predator thread stopped");
    }
}