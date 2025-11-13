package ru.rsreu.savushkin.boidssimulation.view;

import ru.rsreu.savushkin.boidssimulation.config.Settings;

import javax.swing.*;
import java.awt.*;

public class MenuManager {
    private final SimulationView view;
    private final JPanel mainMenuPanel;
    private final JPanel pauseMenuPanel;

    public MenuManager(SimulationView view) {
        this.view = view;
        this.mainMenuPanel = createMainMenu();
        this.pauseMenuPanel = createPauseMenu();

        pauseMenuPanel.setVisible(false);
    }

    public void addTo(JPanel parent) {
        parent.add(mainMenuPanel);
        parent.add(pauseMenuPanel);
    }

    public void update(boolean showMain, boolean showPause) {
        mainMenuPanel.setVisible(showMain);
        pauseMenuPanel.setVisible(showPause);
    }

    private JPanel createMainMenu() {
        JPanel panel = new JPanel(null);
        panel.setOpaque(false);
        panel.setBounds(0, 0, Settings.GAME_FIELD_WIDTH, Settings.GAME_FIELD_HEIGHT);

        JLabel title = new JLabel("BOIDS SIMULATION", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(Color.CYAN);
        title.setBounds(0, 80, Settings.GAME_FIELD_WIDTH, 50);
        panel.add(title);

        int btnY = 180;
        addMenuButton(panel, "НОВАЯ ИГРА", btnY, view::startNew); btnY += 70;
        addMenuButton(panel, "ЗАГРУЗИТЬ", btnY, view::loadGame); btnY += 70;
        addMenuButton(panel, "ВЫХОД", btnY, () -> System.exit(0));

        return panel;
    }

    private void addMenuButton(JPanel panel, String text, int y, Runnable action) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setBounds(150, y, 300, 50);
        btn.setBackground(new Color(50, 50, 100));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.addActionListener(e -> action.run());
        panel.add(btn);
    }

    private JPanel createPauseMenu() {
        JPanel panel = new JPanel(null);
        panel.setOpaque(false);
        panel.setBounds(0, 0, Settings.GAME_FIELD_WIDTH, Settings.GAME_FIELD_HEIGHT);

        JLabel pauseLabel = new JLabel("ПАУЗА", SwingConstants.CENTER);
        pauseLabel.setFont(new Font("Arial", Font.BOLD, 32));
        pauseLabel.setForeground(Color.YELLOW);
        pauseLabel.setBounds(0, 100, Settings.GAME_FIELD_WIDTH, 50);
        panel.add(pauseLabel);

        int btnY = 180;
        addPauseButton(panel, "ПРОДОЛЖИТЬ", btnY, view::togglePause); btnY += 70;
        addPauseButton(panel, "СОХРАНИТЬ", btnY, view::saveGame); btnY += 70;
        addPauseButton(panel, "В ГЛАВНОЕ МЕНЮ", btnY, view::returnToMainMenu);

        return panel;
    }

    private void addPauseButton(JPanel panel, String text, int y, Runnable action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBounds(150, y, 300, 50);
        button.setBackground(new Color(50, 50, 100));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.addActionListener(e -> action.run());
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(80, 80, 150));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 50, 100));
            }
        });
        panel.add(button);
    }
}