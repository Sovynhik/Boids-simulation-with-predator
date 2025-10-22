package ru.rsreu.savushkin.boidssimulation.view;

import ru.rsreu.savushkin.boidssimulation.controller.Controller;
import ru.rsreu.savushkin.boidssimulation.event.Event;
import ru.rsreu.savushkin.boidssimulation.event.Listener;

public class View implements Listener {
    private Controller controller;

    public void initialize() {
        this.controller.addListener(this);
    }

    private void displayState (Event event) {
    }

    @Override
    public void handle(Event event) {
        this.displayState(event);
        }
}