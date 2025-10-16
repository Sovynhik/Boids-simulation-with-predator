package ru.rsreu.savushkin.boidssimulation;

import ru.rsreu.savushkin.boidssimulation.controller.SimulationController;
import ru.rsreu.savushkin.boidssimulation.util.ProjectLogger;
import ru.rsreu.savushkin.boidssimulation.view.SimulationView;

public class ClientRunner {
    public static void main(String[] args) {
        ProjectLogger.logger.info("Application starting");
        SimulationController controller = new SimulationController();
        SimulationView view = new SimulationView(controller);
        view.initialize();
        ProjectLogger.logger.info("Application initialized");
    }
}