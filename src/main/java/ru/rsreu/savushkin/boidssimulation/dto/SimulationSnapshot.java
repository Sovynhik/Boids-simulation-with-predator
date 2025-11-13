package ru.rsreu.savushkin.boidssimulation.dto;

import ru.rsreu.savushkin.boidssimulation.model.entity.AbstractEntity;
import ru.rsreu.savushkin.boidssimulation.model.entity.Predator;

import java.util.List;

public class SimulationSnapshot {
    private final List<AbstractEntity> entities;
    private final Predator predator;
    //private final int width, height;

    public SimulationSnapshot(List<AbstractEntity> entities, Predator predator, int w, int h) {
        this.entities = List.copyOf(entities.stream().map(AbstractEntity::clone).toList());
        this.predator = predator != null ? (Predator)predator.clone() : null;
        //this.width = w;
        //this.height = h;
    }

    public List<AbstractEntity> getEntities() { return entities; }
    public Predator getPredator() { return predator; }
    //public int getWidth() { return width; }
    //public int getHeight() { return height; }
}