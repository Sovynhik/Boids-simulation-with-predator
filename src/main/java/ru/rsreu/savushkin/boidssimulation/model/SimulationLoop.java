package ru.rsreu.savushkin.boidssimulation.model;

import ru.rsreu.savushkin.boidssimulation.config.Settings;

public class SimulationLoop implements Runnable {
    private final SimulationModel model;

    public SimulationLoop(SimulationModel model) {
        this.model = model;
    }

    @Override
    public void run() {
        while (!model.isSimulationOver() && !Thread.currentThread().isInterrupted()) {
            model.update();
            try {
                Thread.sleep(Settings.TICK_DELAY);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}