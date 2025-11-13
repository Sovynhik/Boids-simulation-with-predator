package ru.rsreu.savushkin.boidssimulation.model.entity;

import java.io.Serializable;

public class Field implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int width, height;

    public Field(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
}