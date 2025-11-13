package ru.rsreu.savushkin.boidssimulation.model.entity;

import ru.rsreu.savushkin.boidssimulation.config.Settings;
import ru.rsreu.savushkin.boidssimulation.dto.SimulationSnapshot;

import java.util.stream.Collectors;

public class Fish extends AbstractEntity {
    public Fish(int id, java.awt.Point position) {
        super(id, position, Settings.FISH_SPEED);
    }

    @Override
    public void calculateBehavior(SimulationSnapshot snapshot) {
        var others = snapshot.getEntities().stream()
                .filter(e -> e instanceof Fish && e != this)
                .collect(Collectors.toList());

        var predator = snapshot.getPredator();

        if (predator != null && distanceTo(predator) < Settings.PANIC_RADIUS) {
            double dx = position.x - predator.position.x;
            double dy = position.y - predator.position.y;
            setVelocity(dx, dy);
            return;
        }

        double sepX = 0, sepY = 0, alignX = 0, alignY = 0, cohX = 0, cohY = 0;
        int sepCount = 0, alignCount = 0, cohCount = 0;

        for (var e : others) {
            double d = distanceTo(e);
            if (d < Settings.SEPARATION_RADIUS) { sepX += position.x - e.position.x; sepY += position.y - e.position.y; sepCount++; }
            if (d < Settings.ALIGNMENT_RADIUS) { alignX += ((Fish)e).vx; alignY += ((Fish)e).vy; alignCount++; }
            if (d < Settings.COHESION_RADIUS) { cohX += e.position.x; cohY += e.position.y; cohCount++; }
        }

        if (sepCount > 0) { sepX /= sepCount; sepY /= sepCount; }
        if (alignCount > 0) { alignX /= alignCount; alignY /= alignCount; }
        if (cohCount > 0) { cohX = (cohX / cohCount) - position.x; cohY = (cohY / cohCount) - position.y; }

        double vx = sepX * 1.5 + alignX * 1.0 + cohX * 0.8;
        double vy = sepY * 1.5 + alignY * 1.0 + cohY * 0.8;
        setVelocity(vx, vy);
    }
}