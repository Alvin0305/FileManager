package com.example.filemanager3.settings;

import com.example.filemanager3.Config;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;


public class SettingsSideBar extends VBox {
    SettingsWindow parent;

    SettingsSideButton generalSettingsButton = new SettingsSideButton("General", this::handleGeneralSettings);
    SettingsSideButton appearanceSettingsButton = new SettingsSideButton("Appearance", this::handleAppearanceSettings);
    SettingsSideButton navigationSettingsButton = new SettingsSideButton("Navigation", this::handleNavigationSettings);


    public SettingsSideBar(SettingsWindow parent) {
        this.parent = parent;

        this.getChildren().addAll(generalSettingsButton, appearanceSettingsButton, navigationSettingsButton);
        this.setPrefWidth(Config.settingsSideBarWidth);

        this.getStyleClass().add("settings-sidebar");
    }

    private void handleGeneralSettings(ActionEvent event) {
        if (! Config.currentSettings.equals("general")) {
            parent.getChildren().removeLast();
            parent.getChildren().addLast(new GeneralSettings());
            Config.currentSettings = "general";
        }
    }

    private void handleAppearanceSettings(ActionEvent event) {
        if (! Config.currentSettings.equals("appearance")) {
            parent.getChildren().removeLast();
            parent.getChildren().addLast(new AppearanceSettings());
            Config.currentSettings = "appearance";
        }
    }

    private void handleNavigationSettings(ActionEvent event) {
        if (! Config.currentSettings.equals("navigation")) {
            parent.getChildren().removeLast();
            parent.getChildren().addLast(new NavigationSettings());
            Config.currentSettings = "navigation";
        }
    }
}
