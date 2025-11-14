package ru.rsreu.savushkin.boidssimulation.model.task;

import ru.rsreu.savushkin.boidssimulation.dto.SimulationSnapshot;
import ru.rsreu.savushkin.boidssimulation.model.entity.AbstractEntity;

import java.util.concurrent.Callable;

public class EntityTask implements Callable<AbstractEntity> {
    private final AbstractEntity entity;
    private final SimulationSnapshot snapshot;

    public EntityTask(AbstractEntity entity, SimulationSnapshot snapshot) {
        this.entity = entity;
        this.snapshot = snapshot;
    }

    @Override
    public AbstractEntity call() {
        AbstractEntity copy = entity.clone();
        copy.calculateBehavior(snapshot);
        copy.moveAndBounce(snapshot.getWidth(), snapshot.getHeight());
        return copy;
    }
}