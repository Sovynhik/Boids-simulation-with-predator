package ru.rsreu.savushkin.boidssimulation.dto;

import ru.rsreu.savushkin.boidssimulation.model.entity.FishEntity;
import ru.rsreu.savushkin.boidssimulation.model.entity.PredatorEntity;
import ru.rsreu.savushkin.boidssimulation.model.entity.Field;

import java.io.Serializable;
import java.util.Set;

public class SimulationState implements Serializable {
    private static final long serialVersionUID = 1L;

    private Set<FishEntity> fishes;
    private PredatorEntity predator;
    private Field field;
    private boolean simulationOver;

    private SimulationState() {}

    public Set<FishEntity> getFishes() { return fishes; }
    public PredatorEntity getPredator() { return predator; }
    public Field getField() { return field; }
    public boolean isSimulationOver() { return simulationOver; }

    public static class Builder {
        private final SimulationState state = new SimulationState();

        public static Builder newBuilder() { return new Builder(); }

        public Builder fishes(Set<FishEntity> fishes) { state.fishes = fishes; return this; }
        public Builder predator(PredatorEntity predator) { state.predator = predator; return this; }
        public Builder field(Field field) { state.field = field; return this; }
        public Builder simulationOver(boolean over) { state.simulationOver = over; return this; }

        public SimulationState build() { return state; }
    }
}