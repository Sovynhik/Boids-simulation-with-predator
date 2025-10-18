package ru.rsreu.savushkin.boidssimulation.task;

import ru.rsreu.savushkin.boidssimulation.model.Fish;
import ru.rsreu.savushkin.boidssimulation.state.GameField;

import java.util.concurrent.atomic.AtomicBoolean;

public class FishTask implements Runnable {
    private final Fish fish;
    private final GameField field;
    private final AtomicBoolean isRunning;

    public FishTask(Fish fish, GameField field, AtomicBoolean isRunning) {
        this.fish = fish;
        this.field = field;
        this.isRunning = isRunning;
    }

    @Override
    public void run() {
        while (isRunning.get()) {
            fish.update();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}