package com.example.filemanager3.shortcuts;

import com.example.filemanager3.Config;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Objects;

public class Shortcut extends HBox {
    String icon;
    String shortcut;
    String purpose;

    VBox shortCutAndPurpose = new VBox();
    StackPane iconContainer = new StackPane();
    Label iconText = new Label();
    Text shortcutText = new Text();
    Text purposeText = new Text();

    public Shortcut(ArrayList<String> shortcut) {
        this(shortcut.getFirst(), shortcut.get(1), shortcut.getLast());
    }

    public Shortcut(String icon, String shortcut, String purpose) {
        this.setSpacing(10);
        this.icon = icon;
        this.shortcut = shortcut;
        this.purpose = purpose;
        this.setPrefWidth(Config.shortcutTileWidth);
        this.setMaxWidth(Config.shortcutTileWidth);
        this.setPrefHeight(Config.shortcutTileHeight);
        this.setMaxHeight(Config.shortcutTileHeight);

        iconContainer.setPrefSize(Config.shortcutIconWidth, Config.shortcutIconHeight);
        iconContainer.getChildren().add(iconText);

        iconText.setText(icon);
        shortcutText.setText(shortcut);
        purposeText.setText(purpose);

        shortCutAndPurpose.getChildren().addAll(shortcutText, purposeText);
        shortCutAndPurpose.setAlignment(Pos.CENTER_LEFT);

        this.getChildren().addAll(iconContainer, shortCutAndPurpose);

        iconText.getStyleClass().add("shortcut-icon");
        iconContainer.getStyleClass().add("shortcut-icon-container");
        shortcutText.getStyleClass().add("shortcut-text");
        purposeText.getStyleClass().add("shortcut-purpose");
        this.getStyleClass().add("shortcut");
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("shortcuts-styles.css")).toExternalForm());
    }
}
