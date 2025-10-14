package ru.rsreu.savushkin.boids_simulation.model;

import java.util.concurrent.ThreadLocalRandom;

public abstract class Entity {
    protected double x, y; // Позиция
    protected double vx, vy; // Скорость
    protected final double maxSpeed;

    public Entity(double x, double y, double maxSpeed) {
        this.x = x;
        this.y = y;
        this.maxSpeed = maxSpeed;
        // Инициализация случайной скорости
        vx = ThreadLocalRandom.current().nextDouble(-1, 1);
        vy = ThreadLocalRandom.current().nextDouble(-1, 1);
        normalizeSpeed();
    }

    protected void normalizeSpeed() {
        double speed = Math.sqrt(vx * vx + vy * vy);
        if (speed > 0) {
            vx = (vx / speed) * maxSpeed;
            vy = (vy / speed) * maxSpeed;
        }
    }

    public abstract void update(); // Логика движения

    // Геттеры и сеттеры
    public double getX() { return x; }
    public double getY() { return y; }
    public void setPosition(double x, double y) { this.x = x; this.y = y; }
}