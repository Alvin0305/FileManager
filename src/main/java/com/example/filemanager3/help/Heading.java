package com.example.filemanager3.help;

import javafx.scene.text.Text;

public class Heading extends Text {

    public Heading(String text) {
        this.setText(text);

        this.getStyleClass().add("help-heading");
    }
}
