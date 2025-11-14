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
                moveAndBounce(Settings.GAME_FIELD_WIDTH, Settings.GAME_FIELD_HEIGHT);
                Thread.sleep(Settings.ENTITY_TICK_DELAY);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    protected abstract SimulationSnapshot getCurrentSnapshot();
    public abstract void calculateBehavior(SimulationSnapshot snapshot);

    protected void moveAndBounce(int w, int h) {
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

    protected void normalizeAndSetVelocity(double dx, double dy) {
        double mag = Math.hypot(dx, dy);
        if (mag > 1e-6) {
            vx = (dx / mag) * speed;
            vy = (dy / mag) * speed;
        } else {
            vx = (Math.random() - 0.5) * speed;
            vy = (Math.random() - 0.5) * speed;
        }
    }

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