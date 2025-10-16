package ru.rsreu.savushkin.boidssimulation.model;

public class Predator extends Entity {
    private static final double PREDATOR_SPEED = 3.0;

    public Predator(double x, double y) {
        super(x, y, PREDATOR_SPEED);
    }

    @Override
    public void update() {
        // Пока простое движение; позже добавить поиск добычи
        x += vx;
        y += vy;
        if (x < 0 || x > 800) vx = -vx;
        if (y < 0 || y > 600) vy = -vy;
    }
}
