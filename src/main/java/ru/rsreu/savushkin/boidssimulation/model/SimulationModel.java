package ru.rsreu.savushkin.boidssimulation.model;

import ru.rsreu.savushkin.boidssimulation.config.Settings;
import ru.rsreu.savushkin.boidssimulation.dto.SimulationSnapshot;
import ru.rsreu.savushkin.boidssimulation.dto.SimulationState;
import ru.rsreu.savushkin.boidssimulation.model.entity.FishEntity;
import ru.rsreu.savushkin.boidssimulation.model.entity.PredatorEntity;
import ru.rsreu.savushkin.boidssimulation.model.entity.RunnableEntity;
import ru.rsreu.savushkin.boidssimulation.model.entity.Field;
import ru.rsreu.savushkin.boidssimulation.model.task.RespawnTask;
import ru.rsreu.savushkin.boidssimulation.view.Subscriber;

import java.awt.Point;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class SimulationModel {
    private final Set<Subscriber> subscribers = Collections.synchronizedSet(new HashSet<>());
    private final CopyOnWriteArrayList<RunnableEntity> entities = new CopyOnWriteArrayList<>();
    private PredatorEntity predator;
    private volatile boolean simulationOver = true;
    private volatile boolean paused = false;
    private RespawnTask respawnTask;
    private static int idCounter = 0;

    public synchronized void startNewSimulation() {
        stopAllEntities();
        entities.clear();

        predator = createPredator();
        entities.add(predator);
        predator.start();

        for (int i = 0; i < Settings.INITIAL_FISH_COUNT; i++) {
            FishEntity fish = new FishEntity(++idCounter, randomPointOnField(), this);
            entities.add(fish);
            fish.start();
        }

        simulationOver = false;
        paused = false;
        startRespawnTask();
        notifySubscribers();
    }

    public synchronized void loadSimulation(SimulationState state) {
        stopAllEntities();
        entities.clear();

        if (state.getPredator() != null) {
            predator = new PredatorEntity(
                    state.getPredator().getId(),
                    state.getPredator().getPosition(),
                    this
            );
            predator.setVelocity(
                    state.getPredator().getVelocityX(),
                    state.getPredator().getVelocityY()
            );
            predator.setModel(this);
            entities.add(predator);
            predator.start();
        }

        for (var fishState : state.getFishes()) {
            FishEntity fish = new FishEntity(
                    fishState.getId(),
                    fishState.getPosition(),
                    this
            );
            fish.setVelocity(
                    fishState.getVelocityX(),
                    fishState.getVelocityY()
            );
            fish.setModel(this);
            entities.add(fish);
            fish.start();
        }

        simulationOver = state.isSimulationOver();
        paused = false;
        startRespawnTask();
        notifySubscribers();
    }

    public void finishSimulation() {
        simulationOver = true;
        stopAllEntities();
        if (respawnTask != null) {
            respawnTask.cancel();
            respawnTask = null;
        }
        notifySubscribers();
    }

    public void switchPause() {
        paused = !paused;
        if (respawnTask != null) {
            respawnTask.setPaused(paused);
        }
        notifySubscribers();
    }

    public void update() {
        if (simulationOver || paused) return;
        applyCollisions();
        checkRespawn();
        notifySubscribers();
    }

    private void applyCollisions() {
        if (predator == null) return;
        List<RunnableEntity> toRemove = new ArrayList<>();
        for (RunnableEntity e : entities) {
            if (e instanceof FishEntity && predator.distanceTo(e) < Settings.EAT_RADIUS) {
                toRemove.add(e);
            }
        }
        for (RunnableEntity e : toRemove) {
            e.stop();
            entities.remove(e);
        }
    }

    private void checkRespawn() {
        int fishCount = getFishCount();
        if (fishCount < Settings.FISH_RESPAWN_THRESHOLD) {
            for (int i = 0; i < Settings.FISH_RESPAWN_AMOUNT; i++) {
                FishEntity fish = new FishEntity(++idCounter, randomPointInRespawnZone(), this);
                entities.add(fish);
                fish.start();
            }
        }
    }

    public SimulationSnapshot createSnapshot() {
        return new SimulationSnapshot(new ArrayList<>(entities), predator, Settings.GAME_FIELD_WIDTH, Settings.GAME_FIELD_HEIGHT);
    }

    public void addEntity(RunnableEntity entity) {
        entities.add(entity);
        entity.start();
        notifySubscribers();
    }

    private PredatorEntity createPredator() {
        return new PredatorEntity(++idCounter, randomPointOnField(), this);
    }

    private Point randomPointInRespawnZone() {
        Random r = new Random();
        return new Point(r.nextInt(Settings.RESPAWN_ZONE_SIZE), r.nextInt(Settings.RESPAWN_ZONE_SIZE));
    }

    private Point randomPointOnField() {
        Random r = new Random();
        return new Point(r.nextInt(Settings.GAME_FIELD_WIDTH), r.nextInt(Settings.GAME_FIELD_HEIGHT));
    }

    private void stopAllEntities() {
        for (RunnableEntity e : entities) e.stop();
        entities.clear();
        predator = null;
    }

    private void startRespawnTask() {
        if (respawnTask != null) respawnTask.cancel();
        respawnTask = new RespawnTask(this);
        new Thread(respawnTask, "Respawn-Thread").start();
    }

    private void notifySubscribers() {
        subscribers.forEach(Subscriber::notifySubscriber);
    }

    public void subscribe(Subscriber s) { subscribers.add(s); }

    public SimulationState getSimulationState() {
        Set<FishEntity> fishes = entities.stream()
                .filter(e -> e instanceof FishEntity)
                .map(e -> { FishEntity f = (FishEntity) e.clone(); f.setModel(null); return f; })
                .collect(Collectors.toSet());

        PredatorEntity pred = null;
        if (predator != null) {
            pred = (PredatorEntity) predator.clone();
            pred.setModel(null);
        }

        return SimulationState.Builder.newBuilder()
                .fishes(fishes)
                .predator(pred)
                .field(new Field(Settings.GAME_FIELD_WIDTH, Settings.GAME_FIELD_HEIGHT))
                .simulationOver(simulationOver)
                .build();
    }

    public List<RunnableEntity> getEntities() { return List.copyOf(entities); }
    public int getFishCount() { return (int) entities.stream().filter(e -> e instanceof FishEntity).count(); }
    public boolean isSimulationOver() { return simulationOver; }
    public boolean isPaused() { return paused; }
}