package ru.rsreu.savushkin.boidssimulation.controller;

import ru.rsreu.savushkin.boidssimulation.dto.SimulationState;
import ru.rsreu.savushkin.boidssimulation.model.SimulationModel;
import ru.rsreu.savushkin.boidssimulation.view.Subscriber;

public class SimulationController {

    private final SimulationModel model;

    public SimulationController(SimulationModel model) {
        this.model = model;
    }

    public void startSimulation() {
        model.startNewSimulation();
    }

    public void pauseSimulation() {
        model.switchPause();
    }

    public void stopSimulation() {
        model.finishSimulation();
    }

    public void loadSimulation(SimulationState state) {
        model.loadSimulation(state);
    }

    public void subscribeOnModel(Subscriber subscriber) {
        model.subscribe(subscriber);
    }

    public SimulationState getSimulationState() {
        return model.getSimulationState();
    }

    public SimulationModel getModel() {
        return model;
    }
}