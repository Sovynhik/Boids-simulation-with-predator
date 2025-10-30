package ru.rsreu.savushkin.boidssimulation.controller;

import ru.rsreu.savushkin.boidssimulation.event.SimulationListener;
import ru.rsreu.savushkin.boidssimulation.model.GameField;
import ru.rsreu.savushkin.boidssimulation.model.Model;

public class Controller {
    private final Model model;

    public Controller(Model model) { this.model = model; }

    public void start() { model.startSimulation(); }
    public void pause() { model.pauseSimulation(); }
    public void stop() { model.stopSimulation(); }
    public GameField getField() { return model.getGameField(); }
    public void addListener(SimulationListener listener) { model.addSimulationListener(listener); }
}