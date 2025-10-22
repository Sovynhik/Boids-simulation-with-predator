package ru.rsreu.savushkin.boidssimulation.controller;

public class Controller {
    private Model model;

    public void changeModel() {
        this.model.changeState();
    }

    public void addListener(Listener listener) {
        this.model.addListener(listener);
    }
}
