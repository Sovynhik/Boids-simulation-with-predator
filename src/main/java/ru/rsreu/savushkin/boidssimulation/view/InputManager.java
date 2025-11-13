package ru.rsreu.savushkin.boidssimulation.view;

import ru.rsreu.savushkin.boidssimulation.controller.SimulationController;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputManager {
    private final SimulationView view;
    private final SimulationController controller;

    public InputManager(SimulationView view, SimulationController controller) {
        this.view = view;
        this.controller = controller;
    }

    public void setup() {
        view.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    view.togglePause();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE && !view.isShowMainMenu()) {
                    view.returnToMainMenu();
                }
            }
        });
    }
}