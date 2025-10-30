package ru.rsreu.savushkin.boidssimulation.view;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private final GameField field;

    public GamePanel(GameField field) {
        this.field = field;
        setPreferredSize(Settings.GAME_FIELD_DIMENSION);
        setBackground(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Entity e : field.getEntities()) {
            Point p = e.getPosition();
            if (e instanceof Fish) {
                g.setColor(Color.CYAN);
                g.fillOval(p.x - Settings.FISH_SIZE, p.y - Settings.FISH_SIZE, Settings.FISH_SIZE * 2, Settings.FISH_SIZE * 2);
            } else if (e instanceof Predator) {
                g.setColor(Color.RED);
                g.fillOval(p.x - Settings.PREDATOR_SIZE, p.y - Settings.PREDATOR_SIZE, Settings.PREDATOR_SIZE * 2, Settings.PREDATOR_SIZE * 2);
            }
        }
    }
}