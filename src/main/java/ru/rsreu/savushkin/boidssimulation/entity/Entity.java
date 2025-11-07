package ru.rsreu.savushkin.boidssimulation.entity;

import ru.rsreu.savushkin.boidssimulation.model.GameField;

import java.awt.Point;

public abstract class Entity {
    protected Point position;
    protected double targetVelocityX, targetVelocityY;
    protected final double speed;

    public Entity(Point position, double speed) {
        this.position = new Point(position);
        this.speed = speed;
        this.targetVelocityX = 0;
        this.targetVelocityY = 0;
    }

    public Point getPosition() { return position; }
    public void setPosition(Point p) { this.position = p; }

    public double getTargetVelocityX() { return targetVelocityX; }
    public double getTargetVelocityY() { return targetVelocityY; }

    public void setTargetVelocity(double vx, double vy) {
        double mag = Math.sqrt(vx * vx + vy * vy);
        if (mag > 0) {
            this.targetVelocityX = (vx / mag) * speed;
            this.targetVelocityY = (vy / mag) * speed;
        }
    }

    public void moveAndBounce(int width, int height) {
        double nx = position.x + targetVelocityX;
        double ny = position.y + targetVelocityY;

        if (nx < 0 || nx > width) {
            targetVelocityX = -targetVelocityX;
            nx = Math.max(0, Math.min(nx, width));
        }
        if (ny < 0 || ny > height) {
            targetVelocityY = -targetVelocityY;
            ny = Math.max(0, Math.min(ny, height));
        }

        position.setLocation((int)nx, (int)ny);
    }

    public double distanceTo(Entity other) {
        double dx = this.position.x - other.position.x;
        double dy = this.position.y - other.position.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public abstract void calculateBehavior(GameField field);
}
