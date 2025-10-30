package ru.rsreu.savushkin.boidssimulation;

public class ClientRunner {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Model model = new SimulationModel();
            Controller controller = new Controller(model);
            new View(controller);
        });
    }
}