package ru.rsreu.savushkin.boidssimulation.task;

import ru.rsreu.savushkin.boidssimulation.model.Fish;
import ru.rsreu.savushkin.boidssimulation.state.GameField;
import ru.rsreu.savushkin.boidssimulation.util.ProjectLogger;

import java.util.concurrent.atomic.AtomicBoolean;

public class RespawnControllerTask implements Runnable {
    private static final int RESPAWN_THRESHOLD = 30;
    private static final int MAX_FISH = 50;

    private final GameField field;
    private final AtomicBoolean isRunning;

    public RespawnControllerTask(GameField field, AtomicBoolean isRunning) {
        this.field = field;
        this.isRunning = isRunning;
    }

    @Override
    public void run() {
        while (isRunning.get()) {
            int currentFishCount = field.getFishCount();
            ProjectLogger.logger.config("Checking respawn: Current fish count = " + currentFishCount);
            if (currentFishCount < RESPAWN_THRESHOLD && currentFishCount < MAX_FISH) {
                double spawnX = 10 + Math.random() * 20;
                double spawnY = 10 + Math.random() * 20;
                Fish newFish = new Fish(spawnX, spawnY, field);
                field.addFish(newFish);
                field.getController().startFishTask(newFish);
                ProjectLogger.logger.info("Respawned fish at (" + newFish.getX() + ", " + newFish.getY() + ")");
            } else {
                ProjectLogger.logger.config("No respawn needed: Count = " + currentFishCount);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                ProjectLogger.logger.severe("Respawn thread interrupted: " + e.getMessage());
            }
        }
        ProjectLogger.logger.info("RespawnControllerTask stopped");
    }
}