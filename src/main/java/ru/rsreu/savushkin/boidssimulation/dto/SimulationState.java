package ru.rsreu.savushkin.boidssimulation.dto;

import ru.rsreu.savushkin.boidssimulation.model.entity.Field;

import java.io.Serializable;
import java.util.List;

public class SimulationState implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<EntityDTO.FishDTO> fishes;
    private final EntityDTO.PredatorDTO predator;
    private final Field field;
    private final boolean simulationOver;

    private SimulationState(List<EntityDTO.FishDTO> fishes,
                            EntityDTO.PredatorDTO predator,
                            Field field,
                            boolean simulationOver) {
        this.fishes = fishes;
        this.predator = predator;
        this.field = field;
        this.simulationOver = simulationOver;
    }

    public List<EntityDTO.FishDTO> getFishes() { return fishes; }
    public EntityDTO.PredatorDTO getPredator() { return predator; }
    public Field getField() { return field; }
    public boolean isSimulationOver() { return simulationOver; }

    public static class Builder {
        private List<EntityDTO.FishDTO> fishes;
        private EntityDTO.PredatorDTO predator;
        private Field field;
        private boolean simulationOver;

        public static Builder create() { return new Builder(); }

        public Builder fishes(List<EntityDTO.FishDTO> fishes) { this.fishes = fishes; return this; }
        public Builder predator(EntityDTO.PredatorDTO predator) { this.predator = predator; return this; }
        public Builder field(Field field) { this.field = field; return this; }
        public Builder simulationOver(boolean over) { this.simulationOver = over; return this; }

        public SimulationState build() {
            return new SimulationState(
                    fishes != null ? List.copyOf(fishes) : List.of(),
                    predator,
                    field != null ? field : new Field(600, 500),
                    simulationOver
            );
        }
    }
}