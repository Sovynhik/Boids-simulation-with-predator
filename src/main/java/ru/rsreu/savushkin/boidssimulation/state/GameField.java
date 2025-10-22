package ru.rsreu.savushkin.boidssimulation.state;

import ru.rsreu.savushkin.boidssimulation.controller.SimulationController;
import ru.rsreu.savushkin.boidssimulation.model.Fish;
import ru.rsreu.savushkin.boidssimulation.model.Predator;

import java.util.ArrayList;
import java.util.List;

public class GameField {
    private final List<Fish> fishes = new ArrayList<>();
    private Predator predator;
    private SimulationController controller;

    public void setController(SimulationController controller) {
        this.controller = controller;
    }

    public SimulationController getController() {
        return controller;
    }

    public synchronized void addFish(Fish fish) {
        fishes.add(fish);
    }

    public synchronized void removeFish(Fish fish) {
        fishes.remove(fish);
    }

    public synchronized List<Fish> getFishes() {
        return new ArrayList<>(fishes);
    }

    public synchronized int getFishCount() {
        return fishes.size();
    }

    public void setPredator(Predator predator) {
        this.predator = predator;
    }

    public Predator getPredator() {
        return predator;
    }
}