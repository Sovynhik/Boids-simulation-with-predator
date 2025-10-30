package com.prutzkow.projectlogger;

import java.io.IOException;
import java.util.logging.*;

public class ProjectLogger {
    public static final Logger logger;

    static {
        logger = Logger.getLogger("BoidsSimulation");
        logger.setLevel(Level.ALL);

        try {
            // Создаём папку logs, если её нет
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get("logs"));

            // Лог в файл: logs/simulation.log
            FileHandler fileHandler = new FileHandler("logs/simulation.log", 1024 * 1024, 5, true);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            System.err.println("Не удалось настроить файл логов: " + e.getMessage());
        }

        // Лог в консоль
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
        consoleHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(consoleHandler);

        logger.setUseParentHandlers(false); // Отключаем дублирование в System.err
    }

    private ProjectLogger() {}
}
