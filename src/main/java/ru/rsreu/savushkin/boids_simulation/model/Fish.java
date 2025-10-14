package ru.rsreu.savushkin.boids_simulation.model;

public class Fish extends Entity {
    private static final double FISH_SPEED = 2.0;

    public Fish(double x, double y) {
        super(x, y, FISH_SPEED);
    }

    @Override
    public void update() {
        // Пока простое случайное движение; позже добавить Boids правила
        x += vx;
        y += vy;
        // Обработка границ (предполагаем поле 800x600)
        if (x < 0 || x > 800) vx = -vx;
        if (y < 0 || y > 600) vy = -vy;
    }
}