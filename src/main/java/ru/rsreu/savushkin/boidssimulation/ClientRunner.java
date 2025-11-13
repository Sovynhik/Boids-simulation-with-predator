package ru.rsreu.savushkin.boidssimulation;

import ru.rsreu.savushkin.boidssimulation.controller.SimulationController;
import ru.rsreu.savushkin.boidssimulation.model.SimulationModel;
import ru.rsreu.savushkin.boidssimulation.view.SimulationView;

public class ClientRunner {
    public static void main(String[] args) {
        SimulationModel model = new SimulationModel();
        SimulationController controller = new SimulationController(model);
        new SimulationView(controller);
    }
}