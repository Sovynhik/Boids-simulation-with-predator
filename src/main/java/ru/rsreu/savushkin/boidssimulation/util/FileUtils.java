package ru.rsreu.savushkin.boidssimulation.util;

import java.io.*;
import java.nio.file.*;

public class FileUtils {
    private FileUtils() {}

    public static void save(Object obj, File file) throws IOException {
        try (var oos = new ObjectOutputStream(Files.newOutputStream(file.toPath()))) {
            oos.writeObject(obj);
        }
    }

    public static Object load(File file) throws Exception {
        try (var ois = new ObjectInputStream(Files.newInputStream(file.toPath()))) {
            return ois.readObject();
        }
    }
}