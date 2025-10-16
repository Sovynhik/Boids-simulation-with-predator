// model/Fish.java
package ru.rsreu.savushkin.boidssimulation.model;

import ru.rsreu.savushkin.boidssimulation.state.GameField;
import ru.rsreu.savushkin.boidssimulation.util.ProjectLogger;

import java.util.List;

public class Fish extends Entity {
    private static final double FISH_SPEED = 2.0;
    private static final double SEPARATION_RADIUS = 20.0;
    private static final double ALIGNMENT_RADIUS = 50.0;
    private static final double COHESION_RADIUS = 50.0;
    private static final double AVOIDANCE_RADIUS = 100.0; // От хищника
    private static final double SEPARATION_WEIGHT = 1.5;
    private static final double ALIGNMENT_WEIGHT = 1.0;
    private static final double COHESION_WEIGHT = 1.0;
    private static final double AVOIDANCE_WEIGHT = 2.0;

    private final GameField field; // Ссылка на поле для доступа к соседям

    public Fish(double x, double y, GameField field) {
        super(x, y, FISH_SPEED);
        this.field = field;
    }

    @Override
    public void update() {
        List<Fish> neighbors = getNeighbors();

        double sepX = 0, sepY = 0; // Separation
        double alignX = 0, alignY = 0; // Alignment
        double cohX = 0, cohY = 0; // Cohesion
        int count = 0;

        for (Fish other : neighbors) {
            if (other != this) {
                double dx = x - other.x;
                double dy = y - other.y;
                double dist = Math.sqrt(dx * dx + dy * dy);

                if (dist < SEPARATION_RADIUS) {
                    sepX += dx / dist;
                    sepY += dy / dist;
                }
                if (dist < ALIGNMENT_RADIUS) {
                    alignX += other.vx;
                    alignY += other.vy;
                }
                if (dist < COHESION_RADIUS) {
                    cohX += other.x;
                    cohY += other.y;
                    count++;
                }
            }
        }

        if (count > 0) {
            alignX /= count;
            alignY /= count;
            cohX = (cohX / count - x) / 100;
            cohY = (cohY / count - y) / 100;
        }

        // Avoidance хищника
        Predator predator = field.getPredator();
        if (predator != null) {
            double px = x - predator.getX();
            double py = y - predator.getY();
            double pDist = Math.sqrt(px * px + py * py);
            if (pDist < AVOIDANCE_RADIUS) {
                sepX += px / pDist * AVOIDANCE_WEIGHT;
                sepY += py / pDist * AVOIDANCE_WEIGHT;
            }
        }

        vx += sepX * SEPARATION_WEIGHT + alignX * ALIGNMENT_WEIGHT + cohX * COHESION_WEIGHT;
        vy += sepY * SEPARATION_WEIGHT + alignY * ALIGNMENT_WEIGHT + cohY * COHESION_WEIGHT;

        normalizeSpeed();
        x += vx;
        y += vy;

        reflectBoundaries();

        logPosition();
    }

    private List<Fish> getNeighbors() {
        return field.getFishes(); // Оптимизировать для радиуса позже
    }
}