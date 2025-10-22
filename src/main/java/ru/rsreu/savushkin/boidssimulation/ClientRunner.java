package ru.rsreu.savushkin.boidssimulation;

import ru.rsreu.savushkin.boidssimulation.controller.Controller;
import ru.rsreu.savushkin.boidssimulation.controller.SimulationController;
import ru.rsreu.savushkin.boidssimulation.model.Model;
import ru.rsreu.savushkin.boidssimulation.util.ProjectLogger;
import ru.rsreu.savushkin.boidssimulation.view.SimulationView;
import ru.rsreu.savushkin.boidssimulation.view.View;

public class ClientRunner {
    public static void main(String[] args) {
        Model model = new Model();
        Controller controller = new Controller(model);
        View view = new View(controller);
    }
}