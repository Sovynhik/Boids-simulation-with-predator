package ru.rsreu.savushkin.boidssimulation;

import ru.rsreu.savushkin.boidssimulation.controller.Controller;
import ru.rsreu.savushkin.boidssimulation.model.Model;
import ru.rsreu.savushkin.boidssimulation.model.SimulationModel;
import ru.rsreu.savushkin.boidssimulation.view.View;

public class ClientRunner {
    public static void main(String[] args) {
            Model model = new SimulationModel();
            Controller controller = new Controller(model);
            new View(controller);
    }
}