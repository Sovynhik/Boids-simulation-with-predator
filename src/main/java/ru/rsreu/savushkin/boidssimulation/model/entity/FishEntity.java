package ru.rsreu.savushkin.boidssimulation.model.entity;

import ru.rsreu.savushkin.boidssimulation.config.Settings;
import ru.rsreu.savushkin.boidssimulation.dto.SimulationSnapshot;
import ru.rsreu.savushkin.boidssimulation.model.SimulationModel;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.stream.Collectors;

public class FishEntity extends RunnableEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private transient SimulationModel model;

    public FishEntity(int id, Point position, SimulationModel model) {
        super(id, position, Settings.FISH_SPEED);
        this.model = model;
    }

    public void setModel(SimulationModel model) {
        this.model = model;
    }

    @Override
    protected SimulationSnapshot getCurrentSnapshot() {
        return model.createSnapshot();
    }

    @Override
    public void calculateBehavior(SimulationSnapshot snapshot) {
        var others = snapshot.getEntities().stream()
                .filter(e -> e instanceof FishEntity && e != this)
                .collect(Collectors.toList());

        var predator = snapshot.getPredator();

        if (predator != null && distanceTo(predator) < Settings.PANIC_RADIUS) {
            double dx = position.x - predator.position.x;
            double dy = position.y - predator.position.y;
            normalizeAndSetVelocity(dx, dy);
            return;
        }

        double sepX = 0, sepY = 0, alignX = 0, alignY = 0, cohX = 0, cohY = 0;
        int sepCount = 0, alignCount = 0, cohCount = 0;

        for (var e : others) {
            double d = distanceTo(e);
            if (d < Settings.SEPARATION_RADIUS) {
                sepX += position.x - e.position.x;
                sepY += position.y - e.position.y;
                sepCount++;
            }
            if (d < Settings.ALIGNMENT_RADIUS) {
                alignX += ((FishEntity)e).vx;
                alignY += ((FishEntity)e).vy;
                alignCount++;
            }
            if (d < Settings.COHESION_RADIUS) {
                cohX += e.position.x;
                cohY += e.position.y;
                cohCount++;
            }
        }

        if (sepCount > 0) { sepX /= sepCount; sepY /= sepCount; }
        if (alignCount > 0) { alignX /= alignCount; alignY /= alignCount; }
        if (cohCount > 0) {
            cohX = (cohX / cohCount) - position.x;
            cohY = (cohY / cohCount) - position.y;
        }

        double vx = sepX * Settings.SEPARATION_WEIGHT +
                alignX * Settings.ALIGNMENT_WEIGHT +
                cohX * Settings.COHESION_WEIGHT;

        double vy = sepY * Settings.SEPARATION_WEIGHT +
                alignY * Settings.ALIGNMENT_WEIGHT +
                cohY * Settings.COHESION_WEIGHT;

        if (alignCount > 10) {
            vx += (alignX - vx) * 1.5;
            vy += (alignY - vy) * 1.5;
        }

        if (sepCount > 8) {
            double centerX = 0, centerY = 0;
            int closeCount = 0;
            for (var e : others) {
                if (distanceTo(e) < Settings.SEPARATION_RADIUS * 1.5) {
                    centerX += e.position.x;
                    centerY += e.position.y;
                    closeCount++;
                }
            }
            if (closeCount > 0) {
                centerX /= closeCount; centerY /= closeCount;
                double awayX = position.x - centerX;
                double awayY = position.y - centerY;
                double dist = Math.hypot(awayX, awayY);
                if (dist < 15) {
                    vx += awayX * 0.3;
                    vy += awayY * 0.3;
                }
            }
        }

        vx += (Math.random() - 0.5) * Settings.JITTER_STRENGTH;
        vy += (Math.random() - 0.5) * Settings.JITTER_STRENGTH;

        applyBoundaryAvoidance();

        normalizeAndSetVelocity(vx, vy);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.model = null;
    }
}