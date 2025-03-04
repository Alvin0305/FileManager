package com.example.filemanager3.settings;

import com.example.filemanager3.Config;
import com.example.filemanager3.Sort;
import com.example.filemanager3.View;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AppearanceSettings extends VBox {
    public AppearanceSettings() {
        HBox.setHgrow(this, Priority.ALWAYS);
        this.setSpacing(10);

        HBox defaultViewHBox = new HBox(20);
        Text defaultViewLabel = new Text("Default View");
        ComboBox<View> defaultView = new ComboBox<>();
        defaultView.getItems().addAll(View.SMALL, View.MEDIUM, View.LARGE, View.TILE);
        defaultView.setValue(Config.view);
        defaultView.setOnAction(event -> {
            View value = defaultView.getValue();
            System.out.println(value);
            Config.setProperty("view", String.valueOf(value));
            Config.view = value;
        });
        defaultViewHBox.getChildren().addAll(defaultViewLabel, defaultView);
        defaultViewHBox.setAlignment(Pos.CENTER_LEFT);

        HBox sortHBox = new HBox(20);
        Text sortLabel = new Text("Default Sort");
        ComboBox<Sort> sort = new ComboBox<>();
        sort.getItems().addAll(Sort.ALPHABETIC, Sort.REV_ALPHABETIC, Sort.FIRST_MODIFIED, Sort.LAST_MODIFIED, Sort.SIZE);
        sort.setValue(Config.sort);
        sort.setOnAction(event -> {
            Sort value = sort.getValue();
            System.out.println(value);
            Config.setProperty("sort", String.valueOf(value));
            Config.sort = value;
        });
        sortHBox.getChildren().addAll(sortLabel, sort);
        sortHBox.setAlignment(Pos.CENTER_LEFT);

        CheckBox showDescription = new CheckBox("Show Description");
        showDescription.setSelected(Config.showDescription);
        showDescription.selectedProperty().addListener((obs, oldValue, newValue) -> {
            Config.showDescription = newValue;
            Config.setProperty("show-description", String.valueOf(newValue));
        });

        this.getChildren().addAll(defaultViewHBox, sortHBox, showDescription);

        defaultViewLabel.getStyleClass().add("settings-text");
        sortLabel.getStyleClass().add("settings-text");
        showDescription.getStyleClass().add("settings-text");
        this.getStyleClass().add("settings");
    }
}
