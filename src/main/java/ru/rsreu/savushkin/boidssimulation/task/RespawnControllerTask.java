package ru.rsreu.savushkin.boidssimulation.task;

import ru.rsreu.savushkin.boidssimulation.config.Settings;
import ru.rsreu.savushkin.boidssimulation.entity.Fish;
import ru.rsreu.savushkin.boidssimulation.model.GameField;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class RespawnControllerTask implements Runnable {
    private final GameField field;
    private final AtomicBoolean running;
    private final ExecutorService executor;
    private static final Logger log = com.prutzkow.projectlogger.ProjectLogger.logger;

    private static final Random RAND = new Random();

    public RespawnControllerTask(GameField field, AtomicBoolean running, ExecutorService executor) {
        this.field = field;
        this.running = running;
        this.executor = executor;
    }

    @Override
    public void run() {
        while (running.get()) {
            try {
                if (field.getFishCount() < Settings.FISH_RESPAWN_THRESHOLD) {
                    for (int i = 0; i < Settings.FISH_RESPAWN_AMOUNT; i++) {
                        int x = RAND.nextInt(Settings.RESPAWN_ZONE_SIZE);
                        int y = RAND.nextInt(Settings.RESPAWN_ZONE_SIZE);
                        Point spawnPoint = new Point(x, y);

                        Fish newFish = new Fish(spawnPoint);
                        field.addFish(newFish);
                        executor.submit(new FishBehaviorTask(newFish, field, running));
                    }
                    log.info("Respawn: +" + Settings.FISH_RESPAWN_AMOUNT + " fish. Sum: " + field.getFishCount());
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}