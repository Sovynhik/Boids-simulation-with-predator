package ru.rsreu.savushkin.boidssimulation.view;

import ru.rsreu.savushkin.boidssimulation.config.Settings;
import ru.rsreu.savushkin.boidssimulation.dto.SimulationState;
import ru.rsreu.savushkin.boidssimulation.model.entity.FishEntity;
import ru.rsreu.savushkin.boidssimulation.model.entity.PredatorEntity;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Renderer {

    public void render(Graphics2D g, SimulationState state) {
        g.setColor(Settings.BACKGROUND_COLOR);
        g.fillRect(0, 0, Settings.GAME_FIELD_WIDTH, Settings.GAME_FIELD_HEIGHT);

        for (FishEntity fish : state.getFishes()) {
            drawFish(g, fish);
        }

        if (state.getPredator() != null) {
            drawPredator(g, state.getPredator());
        }

        drawStats(g, state.getFishes().size());
    }

    private void drawFish(Graphics2D g, FishEntity fish) {
        Point p = fish.getPosition();
        int size = Settings.FISH_SIZE;

        g.setColor(Settings.FISH_COLOR);
        g.fill(new Ellipse2D.Double(p.x - size / 2, p.y - size / 2, size, size));

        double vx = fish.getVelocityX();
        double vy = fish.getVelocityY();
        double mag = Math.hypot(vx, vy);

        if (mag > 0.1) {
            double angle = Math.atan2(vy, vx);
            int eyeX = (int) (p.x + Math.cos(angle) * size / 3);
            int eyeY = (int) (p.y + Math.sin(angle) * size / 3);

            g.setColor(Color.WHITE);
            g.fillOval(eyeX - 2, eyeY - 2, 4, 4);
            g.setColor(Color.BLACK);
            g.fillOval(eyeX - 1, eyeY - 1, 2, 2);
        }

        if (Settings.DEBUG_SHOW_RADII) {
            drawDebugRadii(g, p, Settings.FISH_COLOR);
        }
    }

    private void drawPredator(Graphics2D g, PredatorEntity predator) {
        Point p = predator.getPosition();
        int size = Settings.PREDATOR_SIZE;

        g.setColor(Settings.PREDATOR_COLOR);
        g.fill(new Ellipse2D.Double(p.x - size / 2, p.y - size / 2, size, size));

        double vx = predator.getVelocityX();
        double vy = predator.getVelocityY();
        double mag = Math.hypot(vx, vy);

        if (mag > 0.1) {
            double angle = Math.atan2(vy, vx);

            g.setColor(Color.WHITE);
            int eye1X = (int) (p.x + Math.cos(angle + 0.4) * size / 3);
            int eye1Y = (int) (p.y + Math.sin(angle + 0.4) * size / 3);
            int eye2X = (int) (p.x + Math.cos(angle - 0.4) * size / 3);
            int eye2Y = (int) (p.y + Math.sin(angle - 0.4) * size / 3);

            g.fillOval(eye1X - 3, eye1Y - 3, 6, 6);
            g.fillOval(eye2X - 3, eye2Y - 3, 6, 6);

            g.setColor(Color.BLACK);
            g.fillOval(eye1X - 1, eye1Y - 1, 3, 3);
            g.fillOval(eye2X - 1, eye2Y - 1, 3, 3);
        }

        if (Settings.DEBUG_SHOW_RADII) {
            drawPanicRadius(g, p);
        }
    }

    private void drawDebugRadii(Graphics2D g, Point p, Color base) {
        g.setColor(new Color(base.getRed(), base.getGreen(), base.getBlue(), 40));
        g.drawOval(p.x - Settings.COHESION_RADIUS, p.y - Settings.COHESION_RADIUS, Settings.COHESION_RADIUS * 2, Settings.COHESION_RADIUS * 2);
        g.drawOval(p.x - Settings.ALIGNMENT_RADIUS, p.y - Settings.ALIGNMENT_RADIUS, Settings.ALIGNMENT_RADIUS * 2, Settings.ALIGNMENT_RADIUS * 2);
        g.drawOval(p.x - Settings.SEPARATION_RADIUS, p.y - Settings.SEPARATION_RADIUS, Settings.SEPARATION_RADIUS * 2, Settings.SEPARATION_RADIUS * 2);
    }

    private void drawPanicRadius(Graphics2D g, Point p) {
        g.setColor(new Color(255, 50, 50, 40));
        g.drawOval(p.x - Settings.PANIC_RADIUS, p.y - Settings.PANIC_RADIUS, Settings.PANIC_RADIUS * 2, Settings.PANIC_RADIUS * 2);
    }

    private void drawStats(Graphics2D g, int fishCount) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas", Font.BOLD, 14));
        g.drawString("Рыбок: " + fishCount, 10, 20);
        g.drawString("SPACE — пауза | ESC — меню", 10, Settings.GAME_FIELD_HEIGHT - 10);
    }
}