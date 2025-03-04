package com.example.filemanager3.sidebar;

import com.example.filemanager3.Config;
import javafx.scene.control.Button;

import java.io.File;
import java.util.Objects;

public class SideButton extends Button {

    public SideButton(String name, String path) {
        this.setText(name);
        this.setTextFill(Config.sideButtonLabelColor);

        this.setPrefWidth(Config.sideButtonWidth);
        this.setPrefHeight(Config.sideButtonHeight);

        this.setOnAction(event -> Config.scrollPane.navigateTo(new File(path)));

        this.getStyleClass().add("side-button");
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("sidebar-styles.css")).toExternalForm());
    }
}
