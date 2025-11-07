package ru.rsreu.savushkin.boidssimulation.view;

import ru.rsreu.savushkin.boidssimulation.config.Settings;
import ru.rsreu.savushkin.boidssimulation.entity.Entity;
import ru.rsreu.savushkin.boidssimulation.entity.Fish;
import ru.rsreu.savushkin.boidssimulation.entity.Predator;
import ru.rsreu.savushkin.boidssimulation.model.GameField;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private final GameField field;

    public GamePanel(GameField field) {
        this.field = field;
        setPreferredSize(Settings.GAME_FIELD_DIMENSION);
        setBackground(Settings.BACKGROUND_COLOR); // Тёмная вода вместо белого
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Плавные края

        if (Settings.SHOW_RESPAWN_ZONE) {
            g2d.setColor(Settings.RESPAWN_ZONE_COLOR);
            g2d.fillRect(0, 0, Settings.RESPAWN_ZONE_SIZE, Settings.RESPAWN_ZONE_SIZE);
            g2d.setColor(new Color(0, 255, 100, 120)); // Яркая рамка
            g2d.drawRect(0, 0, Settings.RESPAWN_ZONE_SIZE - 1, Settings.RESPAWN_ZONE_SIZE - 1);
        }

        for (Entity e : field.getEntities()) {
            Point p = e.getPosition();
            int x = p.x;
            int y = p.y;

            if (e instanceof Fish) {
                drawFish(g2d, x, y);
            } else if (e instanceof Predator) {
                drawPredator(g2d, x, y);
            }
        }

        if (Settings.DEBUG_SHOW_RADII && !field.getEntities().isEmpty()) {
            Predator predator = field.getPredator();
            if (predator != null) {
                Point p = predator.getPosition();
                drawDebugRadius(g2d, p.x, p.y, Settings.PANIC_RADIUS, new Color(255, 100, 100, 50));
                drawDebugRadius(g2d, p.x, p.y, Settings.EAT_RADIUS, new Color(255, 0, 0, 100));
            }
        }
    }

    private void drawFish(Graphics2D g2d, int x, int y) {
        int size = Settings.FISH_SIZE;
        int diameter = size * 2;

        g2d.setColor(Settings.FISH_COLOR);
        g2d.fillOval(x - size, y - size, diameter, diameter);

        g2d.setColor(Color.WHITE);
        g2d.fillOval(x - size + 2, y - size + 2, size / 2, size / 2);

        Polygon tail = new Polygon();
        tail.addPoint(x - size, y);
        tail.addPoint(x - size - 6, y - 4);
        tail.addPoint(x - size - 6, y + 4);
        g2d.setColor(Settings.FISH_COLOR.darker());
        g2d.fillPolygon(tail);
    }

    private void drawPredator(Graphics2D g2d, int x, int y) {
        int size = Settings.PREDATOR_SIZE;
        int diameter = size * 2;

        g2d.setColor(Settings.PREDATOR_COLOR);
        g2d.fillOval(x - size, y - size, diameter, diameter);

        g2d.setColor(Color.WHITE);
        g2d.fillOval(x - size + 3, y - size + 3, 5, 5);
        g2d.fillOval(x + size - 8, y - size + 3, 5, 5);

        g2d.setColor(Color.BLACK);
        g2d.fillOval(x - size + 4, y - size + 4, 3, 3);
        g2d.fillOval(x + size - 7, y - size + 4, 3, 3);

        g2d.setColor(Color.WHITE);
        Polygon tooth1 = new Polygon();
        tooth1.addPoint(x, y + size);
        tooth1.addPoint(x - 3, y + size + 4);
        tooth1.addPoint(x + 3, y + size + 4);
        g2d.fillPolygon(tooth1);
    }

    private void drawDebugRadius(Graphics2D g2d, int x, int y, int radius, Color color) {
        g2d.setColor(color);
        g2d.drawOval(x - radius, y - radius, radius * 2, radius * 2);
    }
}