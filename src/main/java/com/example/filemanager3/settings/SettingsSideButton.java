package com.example.filemanager3.settings;

import com.example.filemanager3.Config;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.Objects;

public class SettingsSideButton extends Button {

    public SettingsSideButton(String name, EventHandler<ActionEvent> eventHandler) {
        this.setText(name);
        this.setOnAction(eventHandler);

        this.setPrefHeight(Config.sideButtonHeight);
        this.setPrefWidth(Config.sideButtonWidth);

        this.getStyleClass().add("settings-side-button");
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("settings-styles.css")).toExternalForm());
    }
}
