package com.example.filemanager3.help;

import javafx.scene.control.ScrollPane;

public class HelpWindow extends ScrollPane {

    public HelpWindow() {
        this.setContent(new Help(this));
        this.setFitToWidth(true);
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
    }
}
