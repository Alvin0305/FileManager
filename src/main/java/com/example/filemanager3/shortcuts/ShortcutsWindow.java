package com.example.filemanager3.shortcuts;

import com.example.filemanager3.Config;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShortcutsWindow extends FlowPane {

    public ShortcutsWindow() {
        draw();
    }

    private void draw() {
        this.setHgap(Config.shortcutHGap);
        this.setVgap(Config.shortcutVGap);

        ArrayList<ArrayList<String>> shortcuts = new ArrayList<>(List.of(
                new ArrayList<>(List.of("X", "Ctrl+X", "Move a File")),
                new ArrayList<>(List.of("C", "Ctrl+C", "Copy a File")),
                new ArrayList<>(List.of("V", "Ctrl+V", "Paste a File")),
                new ArrayList<>(List.of("N", "Ctrl+N", "Create new File/Folder")),
                new ArrayList<>(List.of("T", "Ctrl+T", "Open Terminal")),
                new ArrayList<>(List.of("P", "Ctrl+P", "Open Properties")),
                new ArrayList<>(List.of("DEL", "DELETE", "Delete a File")),
                new ArrayList<>(List.of("F2", "F2", "Rename a File")),
                new ArrayList<>(List.of("⌫", "BACKSPACE", "Go to Parent Folder")),
                new ArrayList<>(List.of("←", "ALT+←", "Go to Previous Folder")),
                new ArrayList<>(List.of("→", "ALT+→", "Go to Next Folder")),
                new ArrayList<>(List.of("←", "LEFT", "Select Left File")),
                new ArrayList<>(List.of("↑", "UP", "Select Above File")),
                new ArrayList<>(List.of("→", "RIGHT", "Select Right File")),
                new ArrayList<>(List.of("↓", "DOWN", "Select Below File")),
                new ArrayList<>(List.of("⏎", "ENTER", "Open File/Folder"))
                ));

        shortcuts.forEach(shortcut -> this.getChildren().add(new Shortcut(shortcut)));

        this.getStyleClass().add("shortcut-window");
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("shortcuts-styles.css")).toExternalForm());
    }
}
