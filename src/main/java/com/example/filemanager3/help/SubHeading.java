package com.example.filemanager3.help;

import javafx.scene.text.Text;

public class SubHeading extends Text {
    public SubHeading(String text) {
        this.setText(text);

        this.getStyleClass().add("help-sub-heading");
    }
}
