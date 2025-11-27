package ru.rsreu.savushkin.boidssimulation.dto;

import java.awt.Point;
import java.io.Serializable;

public class EntityDTO {
    public record FishDTO(int id, Point position, double vx, double vy) implements Serializable {}
    public record PredatorDTO(int id, Point position, double vx, double vy) implements Serializable {}
}