package ru.rsreu.savushkin.boidssimulation.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ProjectLogger {
	public static final Logger logger = ProjectLogger.configureLogger();

	private static Logger configureLogger() {
		ProjectLogger.setLogRecordFormat();
		var logger = Logger.getLogger("ru.rsreu.savushkin.boidssimulation"); // Обновлён для нового пакета
		logger.setLevel(Level.CONFIG); // Для файла
		logger.setUseParentHandlers(false);

		Handler handler = ProjectLogger.configureConsoleHandler();
		if (handler != null) {
			logger.addHandler(handler);
		}

		handler = ProjectLogger.configureFileHandler();
		if (handler != null) {
			logger.addHandler(handler);
		}

		return logger;
	}

	private static Handler configureFileHandler() {
		Handler handler = null;
		try {
			File logDir = new File("./logs");
			if (!logDir.exists()) {
				logDir.mkdirs();
			}

			var formatter = new SimpleFormatter();
			handler = new FileHandler("./logs/boids%u.log", false);
			handler.setFormatter(formatter);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
			System.err.println("Failed to configure file handler: " + e.getMessage());
		}
		return handler;
	}

	private static Handler configureConsoleHandler() {
		var handler = new ConsoleHandler();
		handler.setLevel(Level.INFO);
		return handler;
	}

	private static void setLogRecordFormat() {
		System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tc] %5$s%n");
		try {
			LogManager.getLogManager().readConfiguration();
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}
}