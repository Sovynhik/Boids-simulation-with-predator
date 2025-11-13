package ru.rsreu.savushkin.boidssimulation.controller;

import ru.rsreu.savushkin.boidssimulation.dto.SimulationState;
import ru.rsreu.savushkin.boidssimulation.model.SimulationModel;
import ru.rsreu.savushkin.boidssimulation.view.Subscriber;

/**
 * Контроллер — единственная точка взаимодействия между View и Model.
 * Управляет запуском, паузой, остановкой и загрузкой симуляции.
 */
public class SimulationController {

    private final SimulationModel model;

    /**
     * Конструктор — принимает модель.
     */
    public SimulationController(SimulationModel model) {
        this.model = model;
    }

    /**
     * Запуск новой симуляции.
     * Вызывается из View при нажатии "Новая игра".
     */
    public void startSimulation() {
        model.startNewSimulation();
    }

    /**
     * Переключение паузы.
     * Вызывается по SPACE.
     */
    public void pauseSimulation() {
        model.switchPause();
    }

    /**
     * Остановка симуляции и возврат в главное меню.
     * Вызывается по ESC или кнопке "В главное меню".
     */
    public void stopSimulation() {
        model.finishSimulation();
    }

    /**
     * Загрузка сохранённого состояния.
     * Вызывается после выбора файла.
     */
    public void loadSimulation(SimulationState state) {
        model.loadSimulation(state);
    }

    /**
     * Подписка View на обновления от Model.
     */
    public void subscribeOnModel(Subscriber subscriber) {
        model.subscribe(subscriber);
    }

    /**
     * Получение текущего состояния симуляции (snapshot).
     * Используется View для отрисовки и FileDialog для сохранения.
     */
    public SimulationState getSimulationState() {
        return model.getSimulationState();
    }

    /**
     * Доступ к модели — нужен SimulationView для запуска SimulationLoop.
     */
    public SimulationModel getModel() {
        return model;
    }
}