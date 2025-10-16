package ru.rsreu.savushkin.boidssimulation.view;

import ru.rsreu.savushkin.boidssimulation.controller.SimulationController;
import ru.rsreu.savushkin.boidssimulation.util.ProjectLogger;

import javax.swing.*;
import java.awt.*;

public class SimulationView {
    private final SimulationController controller;
    private final JFrame frame;
    private final JPanel canvasPanel;
    private final JTextField fishCountField;
    private final JButton startButton;
    private final JButton stopButton;

    public SimulationView(SimulationController controller) {
        this.controller = controller;
        this.frame = new JFrame("Boids Simulation with Predator");
        this.canvasPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                controller.render(g2d);
            }
        };
        this.fishCountField = new JTextField("50", 5);
        this.startButton = new JButton("Start");
        this.stopButton = new JButton("Stop");
    }

    public void initialize() {
        canvasPanel.setPreferredSize(new Dimension(800, 600));
        canvasPanel.setBackground(Color.WHITE);

        startButton.addActionListener(e -> {
            int initialFish = Integer.parseInt(fishCountField.getText());
            controller.initialize(initialFish);
            controller.startSimulation();
            startRendering();
            ProjectLogger.logger.info("Simulation started from View");
        });

        stopButton.addActionListener(e -> {
            controller.stopSimulation();
            ProjectLogger.logger.info("Simulation stopped from View");
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Initial Fishes:"));
        controlPanel.add(fishCountField);
        controlPanel.add(startButton);
        controlPanel.add(stopButton);

        frame.setLayout(new BorderLayout());
        frame.add(controlPanel, BorderLayout.NORTH);
        frame.add(canvasPanel, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                controller.stopSimulation();
                ProjectLogger.logger.info("Application closed");
            }
        });
    }

    private void startRendering() {
        new Thread(() -> {
            while (controller.isRunning()) {
                canvasPanel.repaint();
                try {
                    Thread.sleep(50); // ~20 FPS
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    ProjectLogger.logger.severe("Rendering thread interrupted: " + e.getMessage());
                }
            }
        }).start();
    }
}