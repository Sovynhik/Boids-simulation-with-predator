package ru.rsreu.savushkin.boidssimulation.entity;

import ru.rsreu.savushkin.boidssimulation.config.Settings;
import ru.rsreu.savushkin.boidssimulation.model.GameField;

import java.awt.*;

public class Predator extends Entity {
    public Predator(Point position) {
        super(position, Settings.PREDATOR_SPEED);
    }

    @Override
    public void calculateBehavior(GameField field) {
        Entity nearest = null;
        double minDist = Double.MAX_VALUE;

        for (Entity e : field.getEntities()) {
            if (e instanceof Fish) {
                double d = distanceTo(e);
                if (d < minDist) { minDist = d; nearest = e; }
            }
        }

        if (nearest != null) {
            double dx = nearest.position.x - position.x;
            double dy = nearest.position.y - position.y;
            setTargetVelocity(dx, dy);
        } else {
            targetVelocityX += (Math.random() - 0.5) * 0.5;
            targetVelocityY += (Math.random() - 0.5) * 0.5;
            normalize();
        }
    }

    private void normalize() {
        double mag = Math.sqrt(targetVelocityX * targetVelocityX + targetVelocityY * targetVelocityY);
        if (mag > 0) {
            targetVelocityX = (targetVelocityX / mag) * speed;
            targetVelocityY = (targetVelocityY / mag) * speed;
        }
    }
}