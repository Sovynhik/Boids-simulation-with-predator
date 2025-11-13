package com.prutzkow.projectlogger;

import java.io.IOException;
import java.util.logging.*;

/**
 * Настроенный логгер для Boids-Simulation.
 * <p>
 * Особенности:
 * - Выводит только имя класса (без пакета)
 * - Цветной вывод в консоль
 * - Запись в файл: BoidsSimulation%u.log
 * - Формат: [INFO] SimulationModel - Simulation start
 * </p>
 */
public final class ProjectLogger {
    public static final Logger logger = configureLogger();

    private ProjectLogger() {} // Запрещаем создание

    private static Logger configureLogger() {
        Logger logger = Logger.getLogger("BoidsSimulation");
        logger.setLevel(Level.CONFIG);           // Всё в файл
        logger.setUseParentHandlers(false);      // Отключаем дефолтный вывод

        // Консоль: только INFO и выше
        Handler consoleHandler = createConsoleHandler();
        logger.addHandler(consoleHandler);

        // Файл: всё (CONFIG и выше)
        Handler fileHandler = createFileHandler();
        if (fileHandler != null) {
            logger.addHandler(fileHandler);
        }

        return logger;
    }

    /** Цветной вывод в консоль */
    private static Handler createConsoleHandler() {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.INFO);
        handler.setFormatter(new ColoredConsoleFormatter());
        return handler;
    }

    /** Запись в файл */
    private static Handler createFileHandler() {
        try {
            String logDir = "logs";
            java.io.File dir = new java.io.File(logDir);
            if (!dir.exists()) {
                dir.mkdirs(); // ← создаёт папку logs
            }

            FileHandler handler = new FileHandler(logDir + "/BoidsSimulation%u.log", true);
            handler.setLevel(Level.CONFIG);
            handler.setFormatter(new SimpleFormatter() {
                @Override
                public String format(LogRecord record) {
                    String className = record.getSourceClassName();
                    if (className != null) {
                        className = className.substring(className.lastIndexOf('.') + 1);
                    }
                    return String.format("[%1$tF %1$tT] [%2$s] %3$s - %4$s%n",
                            new java.util.Date(record.getMillis()),
                            record.getLevel(),
                            className,
                            record.getMessage());
                }
            });
            return handler;
        } catch (IOException | SecurityException e) {
            System.err.println("Не удалось создать лог-файл: " + e.getMessage());
            return null;
        }
    }

    /** Форматтер: [INFO] SimulationModel - Simulation start */
    private static class ColoredConsoleFormatter extends Formatter {
        private static final String RESET = "\u001B[0m";
        private static final String GREEN = "\u001B[32m";
        private static final String YELLOW = "\u001B[33m";
        private static final String RED = "\u001B[31m";
        private static final String CYAN = "\u001B[36m";

        @Override
        public String format(LogRecord record) {
            String level = record.getLevel().getName();
            String color = switch (level) {
                case "SEVERE" -> RED;
                case "WARNING" -> YELLOW;
                case "INFO" -> GREEN;
                case "CONFIG" -> CYAN;
                default -> RESET;
            };

            String className = record.getSourceClassName();
            className = className.substring(className.lastIndexOf('.') + 1); // Только имя класса

            return String.format("%s[%s] %s - %s%s%n",
                    color, level, className, record.getMessage(), RESET);
        }
    }
}