package ru.rsreu.savushkin.boidssimulation.model;

public class Model {
    private Listeners listeners;

    public void changeState() {
        Event event = new Event();
        this.notifyListeners(event);
    }

    public void addListener(Listener listener) {

    }

    private void notifyListeners(Event event) {
        this.listeners.notify(Event);
    }
}
