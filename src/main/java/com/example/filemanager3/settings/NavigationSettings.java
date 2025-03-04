package com.example.filemanager3.settings;

import com.example.filemanager3.Config;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class NavigationSettings extends VBox {
    NavigationSettings() {
        HBox.setHgrow(this, Priority.ALWAYS);
        this.setSpacing(10);

        CheckBox clipboardPersistence = new CheckBox("Clipboard Persistence");
        clipboardPersistence.setSelected(Config.clipboardPersistence);
        clipboardPersistence.selectedProperty().addListener((obs, oldValue, newValue) -> {
            Config.clipboardPersistence = newValue;
            Config.setProperty("clipboard-persistence", String.valueOf(newValue));
        });

        HBox startingDirectoryHBox = new HBox(10);
        Text startingDirectoryLabel = new Text("Starting Directory ");
        TextField startingDirectory = new TextField(Config.rootFolder.getAbsolutePath());
        startingDirectoryHBox.getChildren().addAll(startingDirectoryLabel, startingDirectory);
        startingDirectoryHBox.setAlignment(Pos.CENTER_LEFT);

        this.getChildren().addAll(clipboardPersistence, startingDirectoryHBox);
        clipboardPersistence.getStyleClass().add("settings-text");
        startingDirectoryLabel.getStyleClass().add("settings-text");
        startingDirectory.getStyleClass().add("settings-text-field");

        this.getStyleClass().add("settings");
    }
}
