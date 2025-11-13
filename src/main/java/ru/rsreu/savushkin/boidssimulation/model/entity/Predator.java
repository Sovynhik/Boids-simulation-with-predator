package ru.rsreu.savushkin.boidssimulation.model.entity;

import ru.rsreu.savushkin.boidssimulation.config.Settings;
import ru.rsreu.savushkin.boidssimulation.dto.SimulationSnapshot;

import java.util.Comparator;

public class Predator extends AbstractEntity {
    public Predator(int id, java.awt.Point position) {
        super(id, position, Settings.PREDATOR_SPEED);
    }

    @Override
    public void calculateBehavior(SimulationSnapshot snapshot) {
        var fish = snapshot.getEntities().stream()
                .filter(e -> e instanceof Fish)
                .min(Comparator.comparingDouble(this::distanceTo))
                .orElse(null);

        if (fish != null) {
            double dx = fish.position.x - position.x;
            double dy = fish.position.y - position.y;
            setVelocity(dx, dy);
        } else {
            vx += (Math.random() - 0.5) * 0.5;
            vy += (Math.random() - 0.5) * 0.5;
            double mag = Math.hypot(vx, vy);
            if (mag > 0) {
                vx = (vx / mag) * speed;
                vy = (vy / mag) * speed;
            }
        }
    }
}