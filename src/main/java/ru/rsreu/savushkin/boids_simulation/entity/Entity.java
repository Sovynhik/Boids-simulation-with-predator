package ru.rsreu.savushkin.boids_simulation.entity;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Базовый класс для всех сущностей в симуляции (Рыбка, Хищник).
 */
public abstract class Entity {
    // Атомарный счетчик для генерации уникальных ID
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);

    private final int id;
    protected volatile double x; // Позиция X
    protected volatile double y; // Позиция Y

    public Entity(double x, double y) {
        this.id = ID_GENERATOR.incrementAndGet(); // Присвоение уникального ID
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // Методы для движения (будут реализованы в Fish и Predator)
    public abstract void move(GameField field);
}