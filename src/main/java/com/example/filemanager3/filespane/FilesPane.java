package com.example.filemanager3.filespane;

import com.example.filemanager3.Config;
import javafx.scene.control.ScrollPane;

import java.io.File;
import java.util.ArrayList;

public class FilesPane extends ScrollPane {

    File file;

    public FilesPane() {
        this(new File("/home/alvin"));
    }

    public FilesPane(File file) {
        this.file = file;
        Config.currentFolder = file;
        Config.scrollPane = this;

        Files files = new Files(file);

        this.setFitToWidth(true);
        this.setFitToHeight(true);

        this.setContent(files);
    }

    public void navigateTo(File file) {
        if (Config.currentFileIndex < Config.traversedFiles.size() - 1) {
            Config.traversedFiles = new ArrayList<>(Config.traversedFiles.subList(0, Config.currentFileIndex + 1));
        }

        this.setContent(new Files(file));
        Config.currentFolder = file;
        Config.topBar.setAddress();
        changeConfig(file);
    }

    private void changeConfig(File file) {
        // Avoid adding duplicate consecutive entries
        if (Config.traversedFiles.isEmpty() || !Config.traversedFiles.get(Config.traversedFiles.size() - 1).equals(file)) {
            Config.traversedFiles.add(file);
        }

        // Correct index management
        Config.currentFileIndex = Config.traversedFiles.size() - 1;
        System.out.println(Config.traversedFiles);
        System.out.println(Config.currentFileIndex);
    }

    public void navigateBack() {
        if (Config.currentFileIndex > 0) {
            Config.currentFileIndex--;
            File prevFile = Config.traversedFiles.get(Config.currentFileIndex);
            this.setContent(new Files(prevFile));
            Config.currentFolder = prevFile;
            Config.topBar.setAddress();
        }
    }

    public void navigateFront() {
        if (Config.currentFileIndex < Config.traversedFiles.size() - 1) {
            Config.currentFileIndex++;
            File nextFile = Config.traversedFiles.get(Config.currentFileIndex);
            this.setContent(new Files(nextFile));
            Config.currentFolder = nextFile;
            Config.topBar.setAddress();
        }
    }


    public void refresh() {
        this.setContent(new Files(Config.currentFolder));
        Config.topBar.setAddress();
    }
}
