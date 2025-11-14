package ru.rsreu.savushkin.boidssimulation.model.entity;

import ru.rsreu.savushkin.boidssimulation.config.Settings;
import ru.rsreu.savushkin.boidssimulation.dto.SimulationSnapshot;
import ru.rsreu.savushkin.boidssimulation.model.SimulationModel;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Comparator;

public class PredatorEntity extends RunnableEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private transient SimulationModel model;

    public PredatorEntity(int id, Point position, SimulationModel model) {
        super(id, position, Settings.PREDATOR_SPEED);
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
        var fish = snapshot.getEntities().stream()
                .filter(e -> e instanceof FishEntity)
                .min(Comparator.comparingDouble(this::distanceTo))
                .orElse(null);

        if (fish != null) {
            double dx = fish.position.x - position.x;
            double dy = fish.position.y - position.y;
            normalizeAndSetVelocity(dx, dy);
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

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.model = null;
    }
}