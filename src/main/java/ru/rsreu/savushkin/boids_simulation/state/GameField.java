package ru.rsreu.savushkin.boids_simulation.state;

import boids_simulation.entity.Entity;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Разделяемый ресурс: Хранит все сущности в симуляции.
 * Использование ConcurrentHashMap обеспечивает потокобезопасность
 * при операциях добавления, удаления и обновления.
 */
public class GameField {
    // Используем ConcurrentHashMap для потокобезопасного доступа к сущностям.
    // Ключ - уникальный ID сущности, Значение - сама сущность.
    private final Map<Integer, Entity> entities = new ConcurrentHashMap<>();

    /**
     * Добавляет сущность на игровое поле.
     */
    public void addEntity(Entity entity) {
        entities.put(entity.getId(), entity);
    }

    /**
     * Удаляет сущность с игрового поля (например, когда рыбка съедена).
     */
    public void removeEntity(int id) {
        entities.remove(id);
    }

    /**
     * Возвращает неизменяемое представление всех сущностей.
     * Это позволяет потокам-задачам читать данные без риска
     * получить исключение ConcurrentModificationException.
     */
    public Map<Integer, Entity> getAllEntities() {
        return Collections.unmodifiableMap(entities);
    }
}
