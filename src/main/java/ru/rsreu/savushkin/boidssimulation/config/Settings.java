package ru.rsreu.savushkin.boidssimulation.config;

import java.awt.Dimension;

public final class Settings {

    /** Начальное количество рыбок */
    public static final int INITIAL_FISH_COUNT = 35;

    /** Порог респавна */
    public static final int FISH_RESPAWN_THRESHOLD = 20;

    /** Количество рыбок за один респавн — заметно, но не переполняет */
    public static final int FISH_RESPAWN_AMOUNT = 5;

    /** Скорость рыбок */
    public static final double FISH_SPEED = 2.5;

    /** Скорость хищника */
    public static final double PREDATOR_SPEED = 3.8;

    /** Радиус когезии — стая держится вместе */
    public static final int COHESION_RADIUS = 75;

    /** Радиус сепарации */
    public static final int SEPARATION_RADIUS = 15;

    /** Радиус выравнивания — стая движется синхронно */
    public static final int ALIGNMENT_RADIUS = 60;

    /** Радиус паники — стая разбегается задолго до хищника */
    public static final int PANIC_RADIUS = 110;

    /** Радиус поедания */
    public static final int EAT_RADIUS = 12;

    /** Зона респавна — квадрат в левом верхнем углу (0..99) */
    public static final int RESPAWN_ZONE_SIZE = 100;

    /** Ширина игрового поля */
    public static final int GAME_FIELD_WIDTH = 600;

    /** Высота игрового поля */
    public static final int GAME_FIELD_HEIGHT = 500;

    /** Размер поля как Dimension */
    public static final Dimension GAME_FIELD_DIMENSION = new Dimension(GAME_FIELD_WIDTH, GAME_FIELD_HEIGHT);

    /** Размер рыбки */
    public static final int FISH_SIZE = 6;

    /** Размер хищника */
    public static final int PREDATOR_SIZE = 14;

    /** Целевая частота кадров — плавная анимация */
    public static final int TARGET_FPS = 60;

    // ДОПОЛНИТЕЛЬНЫЕ НАСТРОЙКИ ДЛЯ ВИЗУАЛИЗАЦИИ

    /** Цвет фона */
    public static final java.awt.Color BACKGROUND_COLOR = new java.awt.Color(10, 25, 50);

    /** Цвет рыбок */
    public static final java.awt.Color FISH_COLOR = new java.awt.Color(80, 200, 255);

    /** Цвет хищника */
    public static final java.awt.Color PREDATOR_COLOR = new java.awt.Color(220, 20, 20);

    /** Подсветка зоны респавна */
    public static final java.awt.Color RESPAWN_ZONE_COLOR = new java.awt.Color(0, 200, 100, 40);

    /** Показывать ли зону респавна (true = да) */
    public static final boolean SHOW_RESPAWN_ZONE = true;

    /** Показывать ли радиусы (отладка) */
    public static final boolean DEBUG_SHOW_RADII = true;

    private Settings() {}
}