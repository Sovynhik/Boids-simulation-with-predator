package ru.rsreu.savushkin.boids_simulation.task;

import ru.rsreu.savushkin.boids_simulation.model.Fish;
import ru.rsreu.savushkin.boids_simulation.state.GameField;

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
            // Здесь позже добавить логику Boids на основе field.getFishes() и predator
            try {
                Thread.sleep(50); // Задержка для симуляции
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}