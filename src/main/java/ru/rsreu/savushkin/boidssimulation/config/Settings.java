package ru.rsreu.savushkin.boidssimulation.config;

import java.awt.*;

public final class Settings {
    public static final int INITIAL_FISH_COUNT = 35;
    public static final int FISH_RESPAWN_THRESHOLD = 20;
    public static final int FISH_RESPAWN_AMOUNT = 5;
    public static final double FISH_SPEED = 2.5;
    public static final double PREDATOR_SPEED = 3.8;

    public static final int COHESION_RADIUS = 75;
    public static final int SEPARATION_RADIUS = 15;
    public static final int ALIGNMENT_RADIUS = 60;
    public static final int PANIC_RADIUS = 110;
    public static final int EAT_RADIUS = 12;

    public static final int RESPAWN_ZONE_SIZE = 100;
    public static final int GAME_FIELD_WIDTH = 600;
    public static final int GAME_FIELD_HEIGHT = 500;
    public static final Dimension GAME_FIELD_DIMENSION = new Dimension(GAME_FIELD_WIDTH, GAME_FIELD_HEIGHT);

    public static final int FISH_SIZE = 6;
    public static final int PREDATOR_SIZE = 14;
    public static final int TARGET_FPS = 60;
    public static final int TICK_DELAY = 16;

    public static final Color BACKGROUND_COLOR = new Color(10, 25, 50);
    public static final Color FISH_COLOR = new Color(80, 200, 255);
    public static final Color PREDATOR_COLOR = new Color(220, 20, 20);
    public static final Color RESPAWN_ZONE_COLOR = new Color(0, 200, 100, 40);
    public static final boolean SHOW_RESPAWN_ZONE = true;
    public static final boolean DEBUG_SHOW_RADII = true;

    // SAVE
    public static final String SAVE_EXTENSION = "sav";
    public static final String DOT_SAVE_EXTENSION = ".sav";
    public static final String SAVE_FILE_PREFIX = "save_";
    public static final String SAVE_FILE_DATE_PATTERN = "yyyy-MM-dd_HH-mm-ss";

    private Settings() {}
}