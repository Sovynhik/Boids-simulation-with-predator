package ru.rsreu.savushkin.boidssimulation.task;

import ru.rsreu.savushkin.boidssimulation.config.Settings;
import ru.rsreu.savushkin.boidssimulation.model.SimulationModel;

import java.util.concurrent.atomic.AtomicBoolean;

public class SimulationLoopTask implements Runnable {
    private final SimulationModel model;
    private final AtomicBoolean running;
    private final AtomicBoolean paused;
    private static final long FRAME_TIME = 1_000_000_000L / Settings.TARGET_FPS;

    public SimulationLoopTask(SimulationModel model, AtomicBoolean running, AtomicBoolean paused) {
        this.model = model;
        this.running = running;
        this.paused = paused;
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        while (running.get()) {
            if (paused.get()) {
                try { Thread.sleep(100); } catch (InterruptedException e) { break; }
                continue;
            }

            long now = System.nanoTime();
            if (now - lastTime >= FRAME_TIME) {
                model.getGameField().applyVelocitiesAndCheckCollisions();
                model.fireSimulationUpdated();
                lastTime = now;
            }

            try { Thread.sleep(1); } catch (InterruptedException e) { break; }
        }
    }
}