package ru.rsreu.savushkin.boidssimulation.view;

import ru.rsreu.savushkin.boidssimulation.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class View extends JFrame {
    protected final Controller controller;
    private final GamePanel panel;

    public View(Controller controller) {
        this.controller = controller;
        setTitle("Boids Simulation with Predator");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        panel = new GamePanel(controller.getField());
        add(panel, BorderLayout.CENTER);

        JPanel controls = new JPanel();
        JButton startBtn = new JButton("Start");
        JButton pauseBtn = new JButton("Pause");
        JButton stopBtn = new JButton("Stop");

        startBtn.addActionListener(e -> controller.start());
        pauseBtn.addActionListener(e -> controller.pause());
        stopBtn.addActionListener(e -> controller.stop());

        controls.add(startBtn); controls.add(pauseBtn); controls.add(stopBtn);
        add(controls, BorderLayout.SOUTH);

        controller.addListener(() -> SwingUtilities.invokeLater(panel::repaint));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controller.stop();
                dispose();
            }
        });

        setVisible(true);
    }
}