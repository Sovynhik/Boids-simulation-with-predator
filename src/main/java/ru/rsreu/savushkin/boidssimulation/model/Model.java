package ru.rsreu.savushkin.boidssimulation.model;

import ru.rsreu.savushkin.boidssimulation.event.SimulationListener;

public interface Model {
    void startSimulation();
    void pauseSimulation();
    void stopSimulation();
    GameField getGameField();
    void addSimulationListener(SimulationListener listener);
}