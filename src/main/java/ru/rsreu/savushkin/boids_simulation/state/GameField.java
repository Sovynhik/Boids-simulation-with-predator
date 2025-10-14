package ru.rsreu.savushkin.boids_simulation.state;

import ru.rsreu.savushkin.boids_simulation.model.Fish;
import ru.rsreu.savushkin.boids_simulation.model.Predator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class GameField {
    private final List<Fish> fishes = new CopyOnWriteArrayList<>(); // Thread-safe list для рыб
    private Predator predator;
    private final AtomicInteger fishCount = new AtomicInteger(0);

    public void addFish(Fish fish) {
        fishes.add(fish);
        fishCount.incrementAndGet();
    }

    public void removeFish(Fish fish) {
        fishes.remove(fish);
        fishCount.decrementAndGet();
    }

    public void setPredator(Predator predator) {
        this.predator = predator;
    }

    public List<Fish> getFishes() {
        return fishes;
    }

    public Predator getPredator() {
        return predator;
    }

    public int getFishCount() {
        return fishCount.get();
    }
}