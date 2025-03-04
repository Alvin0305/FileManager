package com.example.filemanager3.filespane;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.Objects;

public class Pair extends HBox {
    public Pair(String key, String value) {
        Text keyText = new Text(key);
        keyText.setFill(Color.WHITE);
        Text valueText = new Text(value);
        valueText.setFill(Color.WHITE);
        Region spacer = new Region();

        HBox.setHgrow(spacer, Priority.ALWAYS);

        this.getChildren().addAll(keyText, spacer, valueText);

        keyText.getStyleClass().add("key");
        valueText.getStyleClass().add("value");
        this.getStyleClass().add("pair");
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("filespane-styles.css")).toExternalForm());
    }
}
