package ru.rsreu.savushkin.boidssimulation.model;

import ru.rsreu.savushkin.boidssimulation.config.Settings;
import ru.rsreu.savushkin.boidssimulation.dto.EntityDTO;
import ru.rsreu.savushkin.boidssimulation.dto.SimulationSnapshot;
import ru.rsreu.savushkin.boidssimulation.dto.SimulationState;
import ru.rsreu.savushkin.boidssimulation.model.entity.*;
import ru.rsreu.savushkin.boidssimulation.view.Subscriber;

import java.awt.Point;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class SimulationModel {
    private final Set<Subscriber> subscribers = Collections.synchronizedSet(new HashSet<>());
    private final CopyOnWriteArrayList<RunnableEntity> entities = new CopyOnWriteArrayList<>();
    private final AtomicInteger idCounter = new AtomicInteger(0);

    private volatile boolean simulationOver = true;
    private volatile boolean paused = false;

    public synchronized void startNewSimulation() {
        stopAllEntities();
        entities.clear();

        PredatorEntity predator = createPredator();
        entities.add(predator);
        predator.start();

        for (int i = 0; i < Settings.INITIAL_FISH_COUNT; i++) {
            FishEntity fish = createFish();
            entities.add(fish);
            fish.start();
        }

        simulationOver = false;
        paused = false;
        notifySubscribers();
    }

    public synchronized void loadSimulation(SimulationState state) {
        stopAllEntities();
        entities.clear();

        if (state.getPredator() != null) {
            var dto = state.getPredator();
            PredatorEntity p = new PredatorEntity(dto.id(), new Point(dto.position()), this);
            p.setVelocity(dto.vx(), dto.vy());
            entities.add(p);
            p.start();
        }

        for (EntityDTO.FishDTO dto : state.getFishes()) {
            FishEntity fish = new FishEntity(dto.id(), new Point(dto.position()), this);
            fish.setVelocity(dto.vx(), dto.vy());
            entities.add(fish);
            fish.start();
        }

        simulationOver = state.isSimulationOver();
        paused = false;
        notifySubscribers();
    }

    public void finishSimulation() {
        simulationOver = true;
        stopAllEntities();
        notifySubscribers();
    }

    public void switchPause() {
        paused = !paused;
        notifySubscribers();
    }

    public void update() {
        if (simulationOver || paused) return;
        applyCollisions();
        checkAndRespawnFish();
        notifySubscribers();
    }

    private void applyCollisions() {
        PredatorEntity predator = entities.stream()
                .filter(e -> e instanceof PredatorEntity)
                .map(e -> (PredatorEntity) e)
                .findFirst()
                .orElse(null);

        if (predator == null) return;

        List<RunnableEntity> toRemove = new ArrayList<>();
        for (RunnableEntity e : entities) {
            if (e instanceof FishEntity && predator.distanceTo(e) < Settings.EAT_RADIUS) {
                toRemove.add(e);
            }
        }
        toRemove.forEach(e -> {
            e.stop();
            entities.remove(e);
        });
    }

    private void checkAndRespawnFish() {
        long fishCount = entities.stream().filter(e -> e instanceof FishEntity).count();
        if (fishCount < Settings.FISH_RESPAWN_THRESHOLD) {
            int toAdd = Settings.FISH_RESPAWN_AMOUNT;
            for (int i = 0; i < toAdd; i++) {
                FishEntity fish = createFish();
                entities.add(fish);
                fish.start();
            }
        }
    }

    private FishEntity createFish() {
        return new FishEntity(idCounter.incrementAndGet(), randomPointOnField(), this);
    }

    private PredatorEntity createPredator() {
        return new PredatorEntity(idCounter.incrementAndGet(), randomPointOnField(), this);
    }

    private Point randomPointOnField() {
        Random r = new Random();
        return new Point(r.nextInt(Settings.GAME_FIELD_WIDTH), r.nextInt(Settings.GAME_FIELD_HEIGHT));
    }

    private void stopAllEntities() {
        entities.forEach(RunnableEntity::stop);
        entities.clear();
    }

    public SimulationSnapshot createSnapshot() {
        PredatorEntity predator = entities.stream()
                .filter(e -> e instanceof PredatorEntity)
                .map(e -> (PredatorEntity) e)
                .findFirst()
                .orElse(null);

        return new SimulationSnapshot(
                new ArrayList<>(entities),
                predator,
                Settings.GAME_FIELD_WIDTH,
                Settings.GAME_FIELD_HEIGHT
        );
    }

    public SimulationState getSimulationState() {
        List<EntityDTO.FishDTO> fishDTOs = entities.stream()
                .filter(e -> e instanceof FishEntity)
                .map(e -> {
                    FishEntity f = (FishEntity) e;
                    return new EntityDTO.FishDTO(f.getId(), f.getPosition(), f.getVelocityX(), f.getVelocityY());
                })
                .collect(Collectors.toList());

        EntityDTO.PredatorDTO predatorDTO = null;
        PredatorEntity predator = entities.stream()
                .filter(e -> e instanceof PredatorEntity)
                .map(e -> (PredatorEntity) e)
                .findFirst()
                .orElse(null);

        if (predator != null) {
            predatorDTO = new EntityDTO.PredatorDTO(
                    predator.getId(),
                    predator.getPosition(),
                    predator.getVelocityX(),
                    predator.getVelocityY()
            );
        }

        return SimulationState.Builder.create()
                .fishes(fishDTOs)
                .predator(predatorDTO)
                .field(new Field(Settings.GAME_FIELD_WIDTH, Settings.GAME_FIELD_HEIGHT))
                .simulationOver(simulationOver)
                .build();
    }

    public void subscribe(Subscriber s) { subscribers.add(s); }
    private void notifySubscribers() { subscribers.forEach(Subscriber::notifySubscriber); }

    public boolean isSimulationOver() { return simulationOver; }
    public boolean isPaused() { return paused; }
}