package ru.rsreu.savushkin.boidssimulation.entity;

import ru.rsreu.savushkin.boidssimulation.config.Settings;
import ru.rsreu.savushkin.boidssimulation.model.GameField;

import java.awt.*;

public class Fish extends Entity {
    public Fish(Point position) {
        super(position, Settings.FISH_SPEED);
    }

    @Override
    public void calculateBehavior(GameField field) {
        double sepX = 0, sepY = 0, alignX = 0, alignY = 0, cohX = 0, cohY = 0;
        int sepCount = 0, alignCount = 0, cohCount = 0;

        Predator predator = field.getPredator();
        if (predator != null && distanceTo(predator) < Settings.PANIC_RADIUS) {
            double dx = position.x - predator.position.x;
            double dy = position.y - predator.position.y;
            setTargetVelocity(dx, dy);
            return;
        }

        for (Entity e : field.getEntities()) {
            if (e == this || !(e instanceof Fish)) continue;
            double dist = distanceTo(e);

            if (dist < Settings.SEPARATION_RADIUS) { sepX += position.x - e.position.x; sepY += position.y - e.position.y; sepCount++; }
            if (dist < Settings.ALIGNMENT_RADIUS) { alignX += ((Fish)e).targetVelocityX; alignY += ((Fish)e).targetVelocityY; alignCount++; }
            if (dist < Settings.COHESION_RADIUS) { cohX += e.position.x; cohY += e.position.y; cohCount++; }
        }

        if (sepCount > 0) { sepX /= sepCount; sepY /= sepCount; }
        if (alignCount > 0) { alignX /= alignCount; alignY /= alignCount; }
        if (cohCount > 0) { cohX = (cohX / cohCount) - position.x; cohY = (cohY / cohCount) - position.y; }

        double vx = sepX * 1.5 + alignX * 1.0 + cohX * 0.8;
        double vy = sepY * 1.5 + alignY * 1.0 + cohY * 0.8;

        setTargetVelocity(vx, vy);
    }
}