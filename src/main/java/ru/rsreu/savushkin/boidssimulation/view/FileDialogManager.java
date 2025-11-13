package ru.rsreu.savushkin.boidssimulation.view;

import ru.rsreu.savushkin.boidssimulation.config.Settings;
import ru.rsreu.savushkin.boidssimulation.dto.SimulationState;
import ru.rsreu.savushkin.boidssimulation.model.SimulationLoop;
import ru.rsreu.savushkin.boidssimulation.model.SimulationModel;
import ru.rsreu.savushkin.boidssimulation.util.FileUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileDialogManager {
    private final SimulationView view;
    private final JFileChooser chooser;

    public FileDialogManager(SimulationView view) {
        this.view = view;
        this.chooser = new JFileChooser(System.getProperty("user.dir"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Сохранения (*.sav)", Settings.SAVE_EXTENSION);
        chooser.setFileFilter(filter);
    }

    public void showLoadDialog() {
        if (chooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (!file.getName().endsWith(Settings.DOT_SAVE_EXTENSION)) {
                file = new File(file.getPath() + Settings.DOT_SAVE_EXTENSION);
            }
            try {
                SimulationState state = (SimulationState) FileUtils.load(file);
                view.getController().loadSimulation(state);

                view.setShowMainMenu(false);
                view.setShowPauseMenu(false);
                view.updateMenu();

                SimulationModel model = view.getController().getModel();
                SimulationLoop loop = new SimulationLoop(model);
                Thread thread = new Thread(loop);
                thread.start();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Ошибка загрузки: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void showSaveDialog(SimulationState state) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(Settings.SAVE_FILE_DATE_PATTERN));
        File suggested = new File(Settings.SAVE_FILE_PREFIX + timestamp + Settings.DOT_SAVE_EXTENSION);
        chooser.setSelectedFile(suggested);

        if (chooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (!file.getName().endsWith(Settings.DOT_SAVE_EXTENSION)) {
                file = new File(file.getPath() + Settings.DOT_SAVE_EXTENSION);
            }
            try {
                FileUtils.save(state, file);
                JOptionPane.showMessageDialog(view, "Игра сохранена: " + file.getName(), "Сохранено", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Ошибка сохранения: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}