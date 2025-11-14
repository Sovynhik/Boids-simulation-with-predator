package ru.rsreu.savushkin.boidssimulation.model.entity;

import ru.rsreu.savushkin.boidssimulation.config.Settings;
import ru.rsreu.savushkin.boidssimulation.dto.SimulationSnapshot;

import java.awt.Point;

public abstract class RunnableEntity implements Runnable, Cloneable {
    protected final int id;
    protected Point position;
    protected double vx, vy;
    protected final double speed;
    private volatile boolean running = false;
    private Thread thread;

    public RunnableEntity(int id, Point position, double speed) {
        this.id = id;
        this.position = new Point(position);
        this.speed = speed;
        this.vx = (Math.random() - 0.5) * speed;
        this.vy = (Math.random() - 0.5) * speed;
    }

    public void start() {
        if (running) return;
        running = true;
        thread = new Thread(this, "Entity-" + id);
        thread.start();
    }

    public void stop() {
        running = false;
        if (thread != null) {
            thread.interrupt();
        }
    }

    @Override
    public void run() {
        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                SimulationSnapshot snapshot = getCurrentSnapshot();
                calculateBehavior(snapshot);

                position.x += (int) vx;
                position.y += (int) vy;

                clipToBounds(Settings.GAME_FIELD_WIDTH, Settings.GAME_FIELD_HEIGHT);

                Thread.sleep(Settings.ENTITY_TICK_DELAY);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    protected void applyBoundaryAvoidance() {
        double avoidForce = Settings.BOUNDARY_AVOIDANCE_FORCE;
        double avoidRadius = Settings.BOUNDARY_AVOIDANCE_RADIUS;

        double bx = 0, by = 0;

        if (position.x < avoidRadius) {
            bx += (avoidRadius - position.x) / avoidRadius;
        }
        if (position.x > Settings.GAME_FIELD_WIDTH - avoidRadius) {
            bx -= (avoidRadius - (Settings.GAME_FIELD_WIDTH - position.x)) / avoidRadius;
        }
        if (position.y < avoidRadius) {
            by += (avoidRadius - position.y) / avoidRadius;
        }
        if (position.y > Settings.GAME_FIELD_HEIGHT - avoidRadius) {
            by -= (avoidRadius - (Settings.GAME_FIELD_HEIGHT - position.y)) / avoidRadius;
        }

        vx += bx * avoidForce;
        vy += by * avoidForce;
    }

    protected void clipToBounds(int w, int h) {
        position.x = Math.max(0, Math.min((int)position.x, w));
        position.y = Math.max(0, Math.min((int)position.y, h));
    }

    protected void normalizeAndSetVelocity(double dx, double dy) {
        double mag = Math.hypot(dx, dy);
        if (mag > 1e-6) {
            vx = (dx / mag) * speed;
            vy = (dy / mag) * speed;

            double maxSpeed = speed * 1.3;
            double currentSpeed = Math.hypot(vx, vy);
            if (currentSpeed > maxSpeed) {
                vx = (vx / currentSpeed) * maxSpeed;
                vy = (vy / currentSpeed) * maxSpeed;
            }

            double minSpeed = speed * 0.6;
            if (currentSpeed < minSpeed) {
                double dirX = vx / (currentSpeed + 1e-6);
                double dirY = vy / (currentSpeed + 1e-6);
                vx = dirX * minSpeed;
                vy = dirY * minSpeed;
            }
        } else {
            vx = (Math.random() - 0.5) * speed;
            vy = (Math.random() - 0.5) * speed;
        }
    }


    protected abstract SimulationSnapshot getCurrentSnapshot();
    public abstract void calculateBehavior(SimulationSnapshot snapshot);

    public double distanceTo(RunnableEntity other) {
        return position.distance(other.position);
    }

    public int getId() { return id; }
    public Point getPosition() { return new Point(position); }
    public double getVelocityX() { return vx; }
    public double getVelocityY() { return vy; }

    public void setVelocity(double vx, double vy) {
        this.vx = vx;
        this.vy = vy;
    }

    @Override
    public RunnableEntity clone() {
        try {
            RunnableEntity clone = (RunnableEntity) super.clone();
            clone.position = new Point(this.position);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}