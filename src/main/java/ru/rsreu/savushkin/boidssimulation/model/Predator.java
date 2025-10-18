package ru.rsreu.savushkin.boidssimulation.model;

import ru.rsreu.savushkin.boidssimulation.state.GameField;
import ru.rsreu.savushkin.boidssimulation.util.ProjectLogger;

import java.util.List;

public class Predator extends Entity {
    private static final double PREDATOR_SPEED = 3.0;
    private static final double EAT_RADIUS = 10.0;

    private final GameField field;

    public Predator(double x, double y, GameField field) {
        super(x, y, PREDATOR_SPEED);
        this.field = field;
    }

    @Override
    public void update() {
        Fish closest = findClosestFish();
        if (closest != null) {
            double dx = closest.getX() - x;
            double dy = closest.getY() - y;
            double dist = Math.sqrt(dx * dx + dy * dy);
            if (dist < EAT_RADIUS) {
                field.removeFish(closest);
                ProjectLogger.logger.info("Fish eaten by predator");
            } else {
                vx += dx / dist;
                vy += dy / dist;
            }
        }

        normalizeSpeed();
        x += vx;
        y += vy;

        reflectBoundaries();

        logPosition();
    }

    private Fish findClosestFish() {
        List<Fish> fishes = field.getFishes();
        Fish closest = null;
        double minDist = Double.MAX_VALUE;
        for (Fish fish : fishes) {
            double dx = fish.getX() - x;
            double dy = fish.getY() - y;
            double dist = Math.sqrt(dx * dx + dy * dy);
            if (dist < minDist) {
                minDist = dist;
                closest = fish;
            }
        }
        return closest;
    }
}