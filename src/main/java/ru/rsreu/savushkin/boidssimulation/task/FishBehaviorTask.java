package ru.rsreu.savushkin.boidssimulation.task;

import ru.rsreu.savushkin.boidssimulation.entity.Fish;
import ru.rsreu.savushkin.boidssimulation.model.GameField;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class FishBehaviorTask implements Runnable {
    private final Fish fish;
    private final GameField field;
    private final AtomicBoolean running;
    private static final Logger log = com.prutzkow.projectlogger.ProjectLogger.logger;

    public FishBehaviorTask(Fish fish, GameField field, AtomicBoolean running) {
        this.fish = fish;
        this.field = field;
        this.running = running;
    }

    @Override
    public void run() {
        while (running.get()) {
            try {
                fish.calculateBehavior(field);
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        log.config("FishBehaviorTask stopped");
    }
}