package ru.rsreu.savushkin.boidssimulation.dto;

import ru.rsreu.savushkin.boidssimulation.model.entity.RunnableEntity;
import ru.rsreu.savushkin.boidssimulation.model.entity.PredatorEntity;

import java.util.List;

public class SimulationSnapshot {
    private final List<RunnableEntity> entities;
    private final PredatorEntity predator;
    private final int width, height;

    public SimulationSnapshot(List<RunnableEntity> entities, PredatorEntity predator, int w, int h) {
        this.entities = List.copyOf(entities);
        this.predator = predator;
        this.width = w;
        this.height = h;
    }

    public List<RunnableEntity> getEntities() { return entities; }
    public PredatorEntity getPredator() { return predator; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}