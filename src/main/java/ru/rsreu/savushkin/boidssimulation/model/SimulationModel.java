package ru.rsreu.savushkin.boidssimulation.model;

import ru.rsreu.savushkin.boidssimulation.config.Settings;
import ru.rsreu.savushkin.boidssimulation.dto.SimulationSnapshot;
import ru.rsreu.savushkin.boidssimulation.dto.SimulationState;
import ru.rsreu.savushkin.boidssimulation.model.entity.*;
import ru.rsreu.savushkin.boidssimulation.model.task.RespawnTask;
import ru.rsreu.savushkin.boidssimulation.view.Subscriber;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class SimulationModel {
    private final Set<Subscriber> subscribers = Collections.synchronizedSet(new HashSet<>());
    private final CopyOnWriteArrayList<AbstractEntity> entities = new CopyOnWriteArrayList<>();
    private Predator predator;
    private volatile boolean simulationOver = true;
    private volatile boolean paused = false;

    private RespawnTask respawnTask;
    private static int idCounter = 0;

    public synchronized void startNewSimulation() {
        entities.clear();
        predator = new Predator(++idCounter, randomPoint());
        entities.add(predator);
        for (int i = 0; i < Settings.INITIAL_FISH_COUNT; i++) {
            entities.add(new Fish(++idCounter, randomPoint()));
        }
        simulationOver = false;
        paused = false;
        startRespawnTask();
    }

    public synchronized void loadSimulation(SimulationState state) {
        entities.clear();
        predator = state.getPredator();
        if (predator != null) entities.add(predator);
        state.getFishes().forEach(entities::add);
        simulationOver = false;
        paused = false;
        startRespawnTask();
    }

    public void finishSimulation() {
        simulationOver = true;
        if (respawnTask != null) respawnTask.cancel();
    }

    public void switchPause() {
        paused = !paused;
        if (respawnTask != null) respawnTask.setPaused(paused);
    }

    public void subscribe(Subscriber s) { subscribers.add(s); }

    public void update() {
        if (simulationOver || paused) return;

        SimulationSnapshot snapshot = createSnapshot();

        // Параллельное поведение
        entities.parallelStream().forEach(e -> e.calculateBehavior(snapshot));

        // Движение
        entities.forEach(e -> e.moveAndBounce(Settings.GAME_FIELD_WIDTH, Settings.GAME_FIELD_HEIGHT));

        // СТОЛКНОВЕНИЯ — ИСПРАВЛЕНО: без iterator.remove()
        applyCollisions();

        // Уведомление
        subscribers.forEach(Subscriber::notifySubscriber);
    }

    private SimulationSnapshot createSnapshot() {
        return new SimulationSnapshot(
                new ArrayList<>(entities),
                predator,
                Settings.GAME_FIELD_WIDTH,
                Settings.GAME_FIELD_HEIGHT
        );
    }

    // ИСПРАВЛЕНО: Удаление через removeAll()
    private void applyCollisions() {
        if (predator == null) return;

        List<AbstractEntity> toRemove = new ArrayList<>();
        for (AbstractEntity e : entities) {
            if (e instanceof Fish && predator.distanceTo(e) < Settings.EAT_RADIUS) {
                toRemove.add(e);
            }
        }
        entities.removeAll(toRemove); // Безопасно для CopyOnWriteArrayList
    }

    private void startRespawnTask() {
        if (respawnTask != null) respawnTask.cancel();
        respawnTask = new RespawnTask(this);
        new Thread(respawnTask).start();
    }

    private java.awt.Point randomPoint() {
        Random r = new Random();
        return new java.awt.Point(r.nextInt(Settings.GAME_FIELD_WIDTH), r.nextInt(Settings.GAME_FIELD_HEIGHT));
    }

    public SimulationState getSimulationState() {
        Set<Fish> fishes = entities.stream()
                .filter(e -> e instanceof Fish)
                .map(e -> (Fish) e.clone())
                .collect(Collectors.toSet());
        Predator pred = predator != null ? (Predator) predator.clone() : null;
        return SimulationState.Builder.newBuilder()
                .fishes(fishes)
                .predator(pred)
                .field(new Field(Settings.GAME_FIELD_WIDTH, Settings.GAME_FIELD_HEIGHT))
                .simulationOver(simulationOver)
                .build();
    }

    public List<AbstractEntity> getEntities() { return List.copyOf(entities); }
    public int getFishCount() { return (int) entities.stream().filter(e -> e instanceof Fish).count(); }
    public void addEntity(AbstractEntity e) { entities.add(e); }
    public boolean isSimulationOver() { return simulationOver; }
}