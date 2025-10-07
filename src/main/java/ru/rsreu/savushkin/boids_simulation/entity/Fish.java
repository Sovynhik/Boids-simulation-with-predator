package ru.rsreu.savushkin.boids_simulation.entity;

/**
 * Представляет одну рыбку (Боида) в симуляции.
 */
public class Fish extends Entity {
    // Дополнительные параметры Boid, например:
    private double velocityX;
    private double velocityY;

    // Параметры поведения (радиус скопления, паники и т.д.)
    private final double maxSpeed = 2.0;

    public Fish(double x, double y) {
        super(x, y);
        this.velocityX = Math.random() * maxSpeed;
        this.velocityY = Math.random() * maxSpeed;
    }

    /**
     * Основная логика движения рыбки (правила Boids, реакция на Хищника).
     * @param field Игровое поле для доступа к соседям и Хищнику.
     */
    @Override
    public void move(boids_simulation.state.GameField field) {
        // TODO: Здесь будет логика:
        // 1. Separation (отделение)
        // 2. Alignment (выравнивание)
        // 3. Cohesion (сплоченность)
        // 4. Panic (реакция на Predator)

        // Временная реализация простого движения:
        x += velocityX;
        y += velocityY;

        // Проверка границ поля (TODO: Добавить логику отскока)
        if (x < 0 || x > 800) velocityX *= -1;
        if (y < 0 || y > 600) velocityY *= -1;
    }
}
