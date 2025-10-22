package ru.rsreu.savushkin.boidssimulation.model;

import ru.rsreu.savushkin.boidssimulation.util.ProjectLogger;

public abstract class Entity {
    protected double x;
    protected double y;
    protected double vx;
    protected double vy;
    protected final double maxSpeed;

    public Entity(double x, double y, double maxSpeed) {
        this.x = x;
        this.y = y;
        this.maxSpeed = maxSpeed;
        this.vx = 0;
        this.vy = 0;
    }

    public abstract void update();

    protected void normalizeSpeed() {
        double speed = Math.sqrt(vx * vx + vy * vy);
        if (speed > maxSpeed) {
            vx = (vx / speed) * maxSpeed;
            vy = (vy / speed) * maxSpeed;
        }
    }

    protected void reflectBoundaries() {
        if (x < 0) {
            x = 0;
            vx = -vx;
        } else if (x > 800) {
            x = 800;
            vx = -vx;
        }
        if (y < 0) {
            y = 0;
            vy = -vy;
        } else if (y > 600) {
            y = 600;
            vy = -vy;
        }
    }

    protected void logPosition() {
        ProjectLogger.logger.config("Predator updated: x=" + String.format("%.2f", x) +
                ", y=" + String.format("%.2f", y) +
                ", vx=" + String.format("%.2f", vx) +
                ", vy=" + String.format("%.2f", vy));
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getVx() { return vx; }
    public double getVy() { return vy; }
}