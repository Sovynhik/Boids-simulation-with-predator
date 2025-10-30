package ru.rsreu.savushkin.boidssimulation.task;

import ru.rsreu.savushkin.boidssimulation.entity.Predator;
import ru.rsreu.savushkin.boidssimulation.model.GameField;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class PredatorBehaviorTask implements Runnable {
    private final Predator predator;
    private final GameField field;
    private final AtomicBoolean running;
    private static final Logger log = com.prutzkow.projectlogger.ProjectLogger.logger;

    public PredatorBehaviorTask(Predator predator, GameField field, AtomicBoolean running) {
        this.predator = predator;
        this.field = field;
        this.running = running;
    }

    @Override
    public void run() {
        while (running.get()) {
            try {
                predator.calculateBehavior(field);
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        log.config("PredatorBehaviorTask stopped");
    }
}