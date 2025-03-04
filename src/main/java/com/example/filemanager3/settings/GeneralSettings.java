package com.example.filemanager3.settings;

import com.example.filemanager3.Config;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class GeneralSettings extends VBox {
    public GeneralSettings() {
        HBox.setHgrow(this, Priority.ALWAYS);
        this.setSpacing(10);

        CheckBox showHiddenFiles = new CheckBox("Show Hidden Files");
        showHiddenFiles.setSelected(Config.showHiddenFiles);
        showHiddenFiles.selectedProperty().addListener((obs, oldValue, newValue) -> {
            Config.setProperty("show-hidden-files", String.valueOf(newValue));
            Config.showHiddenFiles = newValue;
        });

        CheckBox showFileExtension = new CheckBox("Show File Extension");
        showFileExtension.setSelected(Config.showFileExtension);
        showFileExtension.selectedProperty().addListener((obs, oldValue, newValue) -> {
            Config.setProperty("show-file-extension", String.valueOf(newValue));
            Config.showFileExtension = newValue;
        });

        CheckBox confirmBeforeDelete = new CheckBox("Confirm Before Deletion");
        confirmBeforeDelete.setSelected(Config.confirmBeforeDelete);
        confirmBeforeDelete.selectedProperty().addListener((obs, oldValue, newValue) -> {
            Config.setProperty("confirm-before-delete", String.valueOf(newValue));
            Config.confirmBeforeDelete = newValue;
        });

        showHiddenFiles.getStyleClass().add("settings-text");
        showFileExtension.getStyleClass().add("settings-text");
        confirmBeforeDelete.getStyleClass().add("settings-text");

        this.getChildren().addAll(showHiddenFiles, showFileExtension, confirmBeforeDelete);
        this.getStyleClass().add("settings");
    }
}
