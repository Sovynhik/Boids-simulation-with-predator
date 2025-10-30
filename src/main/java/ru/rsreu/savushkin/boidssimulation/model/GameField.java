package ru.rsreu.savushkin.boidssimulation.model;

import ru.rsreu.savushkin.boidssimulation.config.Settings;
import ru.rsreu.savushkin.boidssimulation.entity.Entity;
import ru.rsreu.savushkin.boidssimulation.entity.Fish;
import ru.rsreu.savushkin.boidssimulation.entity.Predator;

import java.awt.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class GameField {
    private final List<Entity> entities = new CopyOnWriteArrayList<>();
    private final AtomicInteger fishCount = new AtomicInteger(0);
    private final Predator predator;

    public GameField() {
        predator = new Predator(new Point(Settings.GAME_FIELD_WIDTH / 2, Settings.GAME_FIELD_HEIGHT / 2));
        entities.add(predator);
    }

    public List<Entity> getEntities() { return entities; }
    public Predator getPredator() { return predator; }

    public void addFish(Fish fish) {
        entities.add(fish);
        fishCount.incrementAndGet();
    }

    public void removeFish(Fish fish) {
        entities.remove(fish);
        fishCount.decrementAndGet();
    }

    public int getFishCount() { return fishCount.get(); }

    public void applyVelocitiesAndCheckCollisions() {
        for (Entity e : entities) {
            e.moveAndBounce(Settings.GAME_FIELD_WIDTH, Settings.GAME_FIELD_HEIGHT);
        }

        Predator predator = getPredator();
        for (Entity e : entities) {
            if (e instanceof Fish && predator.distanceTo(e) < Settings.EAT_RADIUS) {
                removeFish((Fish) e);
                break;
            }
        }
    }
}