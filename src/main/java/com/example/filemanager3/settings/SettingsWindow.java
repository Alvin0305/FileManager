package com.example.filemanager3.settings;

import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;

import java.util.Objects;

public class SettingsWindow extends HBox {
    SettingsSideBar settingsSideBar = new SettingsSideBar(this);
    GeneralSettings generalSettings = new GeneralSettings();
    Separator separator = new Separator(Orientation.VERTICAL);

    public SettingsWindow() {
        this.getChildren().addAll(settingsSideBar, separator, generalSettings);

        this.getStyleClass().add("settings-window");
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("settings-styles.css")).toExternalForm());
    }
}
