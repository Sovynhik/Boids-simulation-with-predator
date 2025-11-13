package ru.rsreu.savushkin.boidssimulation.model.task;

import ru.rsreu.savushkin.boidssimulation.config.Settings;
import ru.rsreu.savushkin.boidssimulation.model.SimulationModel;
import ru.rsreu.savushkin.boidssimulation.model.entity.Fish;
import com.prutzkow.projectlogger.ProjectLogger;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class RespawnTask implements Runnable {
    private final SimulationModel model;
    private final AtomicBoolean paused = new AtomicBoolean(false);
    private final AtomicBoolean cancelled = new AtomicBoolean(false);
    private static final Random RAND = new Random();
    private static int counter = 1000;

    public RespawnTask(SimulationModel model) { this.model = model; }

    @Override
    public void run() {
        while (!cancelled.get()) {
            if (paused.get()) { try { Thread.sleep(100); } catch (InterruptedException e) { break; } continue; }
            if (model.getFishCount() < Settings.FISH_RESPAWN_THRESHOLD) {
                for (int i = 0; i < Settings.FISH_RESPAWN_AMOUNT; i++) {
                    int x = RAND.nextInt(Settings.RESPAWN_ZONE_SIZE);
                    int y = RAND.nextInt(Settings.RESPAWN_ZONE_SIZE);
                    Fish fish = new Fish(++counter, new java.awt.Point(x, y));
                    model.addEntity(fish);
                }
                ProjectLogger.logger.info("Respawn: +" + Settings.FISH_RESPAWN_AMOUNT);
            }
            try { Thread.sleep(1000); } catch (InterruptedException e) { break; }
        }
    }

    public void setPaused(boolean p) { paused.set(p); }
    public void cancel() { cancelled.set(true); }
}