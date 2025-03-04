package com.example.filemanager3.topbar;

import com.example.filemanager3.Config;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import org.kordamp.ikonli.javafx.FontIcon;

public class TopButton extends Button {

    public TopButton(FontIcon icon, EventHandler<ActionEvent> action) {
        icon.setIconSize(Config.topButtonIconSize);
        icon.setIconColor(Config.topButtonIconColor);
        this.setGraphic(icon);

        this.setOnAction(action);

        this.getStyleClass().add("top-button");
    }
}
