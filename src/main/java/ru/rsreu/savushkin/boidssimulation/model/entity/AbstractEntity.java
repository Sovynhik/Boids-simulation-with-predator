package ru.rsreu.savushkin.boidssimulation.model.entity;

import ru.rsreu.savushkin.boidssimulation.dto.SimulationSnapshot;

import java.awt.Point;
import java.io.Serializable;

public abstract class AbstractEntity implements Serializable, Cloneable {
    private final int id;
    protected Point position;
    protected double vx, vy;
    protected final double speed;

    public AbstractEntity(int id, Point position, double speed) {
        this.id = id;
        this.position = new Point(position);
        this.speed = speed;
        this.vx = 0;
        this.vy = 0;
    }

    public int getId() {
        return id;
    }

    public Point getPosition() {
        return new Point(position);
    }

    public double getVelocityX() {
        return vx;
    }

    public double getVelocityY() {
        return vy;
    }

    public synchronized void setVelocity(double vx, double vy) {
        double mag = Math.hypot(vx, vy);
        if (mag > 0) {
            this.vx = (vx / mag) * speed;
            this.vy = (vy / mag) * speed;
        }
    }

    public synchronized void moveAndBounce(int w, int h) {
        double nx = position.x + vx;
        double ny = position.y + vy;
        if (nx < 0 || nx > w) {
            vx = -vx;
            nx = Math.max(0, Math.min(nx, w));
        }
        if (ny < 0 || ny > h) {
            vy = -vy;
            ny = Math.max(0, Math.min(ny, h));
        }
        position.setLocation((int) nx, (int) ny);
    }

    public double distanceTo(AbstractEntity o) {
        double dx = position.x - o.position.x;
        double dy = position.y - o.position.y;
        return Math.hypot(dx, dy);
    }

    public abstract void calculateBehavior(SimulationSnapshot snapshot);

    @Override
    public boolean equals(Object o) {
        return o instanceof AbstractEntity && id == ((AbstractEntity) o).id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public AbstractEntity clone() {
        try {
            AbstractEntity clone = (AbstractEntity) super.clone();
            clone.position = new Point(this.position);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Clone not supported, but Cloneable is implemented", e);
        }
    }
}