package com.example.filemanager3.filespane;

import com.example.filemanager3.Config;
import com.example.filemanager3.Sort;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Files extends FlowPane {
    File file;
    ArrayList<File> files = new ArrayList<>();

    ContextMenu contextMenu = new ContextMenu();
    MenuItem newMenu = new MenuItem("New File/Folder");
    MenuItem openTerminalMenu = new MenuItem("Open in Console");
    MenuItem pasteMenu = new MenuItem("Paste");
    MenuItem selectAllMenu = new MenuItem("Select All");
    MenuItem invertSelectionMenu = new MenuItem("Invert Selection");
    MenuItem propertiesMenu = new MenuItem("Properties");

    public Files() {
        this(new File("/home/alvin"));
    }

    public Files(File file) {
        this.file = file;
        System.out.println("current: " + file);
        Config.currentFileIndex = 0;

        populateFiles();
        draw();

        init();

        Config.currentFiles.forEach(tile -> System.out.println(tile.file));
    }

    public Files(ArrayList<File> files) {
        this.files = files;
        draw();

        init();
    }

    private void init() {
        this.setHgap(Config.spacingAmongTiles);
        this.setVgap(Config.spacingAmongTiles);

        Config.flowPane = this;
        Config.currentFolder = file;

        assembleContextMenu();
        addListeners();

        this.getStyleClass().add("files-pane");
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("filespane-styles.css")).toExternalForm());
    }

    private void draw() {
        Sort sort = Config.sort;
        switch (sort) {
            case ALPHABETIC -> files.sort(Comparator.comparing(File::getName));
            case REV_ALPHABETIC -> files.sort(Comparator.comparing(File::getName).reversed());
            case LAST_MODIFIED -> files.sort(Comparator.comparing(File::lastModified));
            case FIRST_MODIFIED -> files.sort(Comparator.comparing(File::lastModified).reversed());
            case SIZE -> files.sort(Comparator.comparing(File::length));
        }
        files.forEach(f -> {
            FileTile fileTile = new FileTile(f, Config.view);
            this.getChildren().add(fileTile);
            Config.currentFiles.add(fileTile);
        });
    }

    private void addListeners() {
        this.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                Config.unSelectAll();
            } else if (event.getButton().equals(MouseButton.SECONDARY)) {
                contextMenu.show(this, event.getScreenX(), event.getScreenY());
            }
            event.consume();
        });
    }

    private void populateFiles() {
        Config.currentFiles.clear();
        File[] list = file.listFiles();
        if (list != null) {
            for (File file: list) {
                if (Config.showHiddenFiles) {
                    files.add(file);
                } else if (! file.isHidden()) {
                    files.add(file);
                }
            }
        }
    }

    private void assembleContextMenu() {
        contextMenu.getItems().addAll(newMenu, openTerminalMenu, pasteMenu, selectAllMenu, invertSelectionMenu, propertiesMenu);
        addMenuListeners();
    }

    private File getCurrentFolder() {
        return Config.currentFolder;
    }

    private boolean shortcutHandlerAdded = false;

    private void addMenuListeners() {
        newMenu.setOnAction(event -> handleNewMenu());
        openTerminalMenu.setOnAction(event -> handleOpenTerminal());
        pasteMenu.setOnAction(event -> handlePasteMenu());
        selectAllMenu.setOnAction(event -> handleSelectAllMenu());
        invertSelectionMenu.setOnAction(event -> handleInvertSelectionMenu());
        propertiesMenu.setOnAction(event -> handleShowProperties());

        Platform.runLater(() -> {
            Scene scene = this.getScene();
            if (scene == null) {
                System.out.println("No scene available");
                return;
            }

            if (! Config.arrowEventFiltersAdded) {
                scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                    ArrayList<KeyCode> arrowKeys = new ArrayList<>(List.of(KeyCode.RIGHT, KeyCode.DOWN));
                    if (Config.selectedFiles.isEmpty()) {
                        if (arrowKeys.contains(event.getCode())) {
                            System.out.println("selecting first file");
                            Config.currentFiles.getFirst().select();
                            Config.currentFiles.getFirst().requestFocus();
                            return;
                        }
                    }
                    if (event.isShiftDown()) {
                        System.out.println("shift selection");
                        switch (event.getCode()) {
                            case RIGHT -> shiftSelection(1);
                            case LEFT -> shiftSelection(-1);
                            case UP -> shiftSelection(-getRowSize());
                            case DOWN -> shiftSelection(getRowSize());
                        }
                    } else if (event.isControlDown()) {
                        System.out.println("control selection");
                        switch (event.getCode()) {
                            case RIGHT -> controlSelection(1);
                            case LEFT -> controlSelection(-1);
                            case UP -> controlSelection(-getRowSize());
                            case DOWN -> controlSelection(getRowSize());
                        }
                    } else {
                        System.out.println("move selection");
                        switch (event.getCode()) {
                            case RIGHT -> moveSelection(1);
                            case LEFT -> moveSelection(-1);
                            case UP -> moveSelection(-getRowSize());
                            case DOWN -> moveSelection(getRowSize());
                        }
                    }
                    if (event.getCode().equals(KeyCode.ESCAPE)) {
                        System.out.println("escape");
                        Config.unSelectAll();
                    }
                });
                Config.arrowEventFiltersAdded = true;
            }

            if (!shortcutHandlerAdded) {
                shortcutHandlerAdded = true; // Mark as added

                scene.setOnKeyPressed(event -> {
                    if (event.isControlDown()) {
                        event.consume(); // Prevent duplicate executions
                        switch (event.getCode()) {
                            case N -> {
                                Config.currentFolder = getCurrentFolder(); // Ensure correct folder
                                System.out.println("New menu: " + Config.currentFolder);
                                handleNewMenu();
                            }
                            case T -> handleOpenTerminal();
                            case V -> handlePasteMenu();
                            case A -> handleSelectAllMenu();
                            case I -> handleInvertSelectionMenu();
                            case P -> handleShowProperties();
                            case C -> handleCopy();
                            case X -> handleCut();
                        }
                    } else if (event.getCode().equals(KeyCode.DELETE)) {
                        handleDelete();
                    } else if (event.getCode().equals(KeyCode.ENTER)) {
                        handleOpen();
                    } else if (event.getCode().equals(KeyCode.BACK_SPACE)) {
                        Config.topBar.handleBackEvent(new ActionEvent());
                    } else if (event.isAltDown()) {
                        if (event.getCode().equals(KeyCode.LEFT)) {
                            Config.topBar.handleLeftEvent(new ActionEvent());
                        } else if (event.getCode().equals(KeyCode.RIGHT)) {
                            Config.topBar.handleRightEvent(new ActionEvent());
                        }
                    }
                });

                System.out.println("Keyboard shortcuts added successfully.");
            }
        });

        newMenu.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
        openTerminalMenu.setAccelerator(KeyCombination.keyCombination("Ctrl+T"));
        pasteMenu.setAccelerator(KeyCombination.keyCombination("Ctrl+V"));
        selectAllMenu.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
        invertSelectionMenu.setAccelerator(KeyCombination.keyCombination("Ctrl+I"));
        propertiesMenu.setAccelerator(KeyCombination.keyCombination("Ctrl+P"));
    }

    private void handleOpen() {
        if (Config.selectedFiles.size() == 1) {
            FileTile tile = Config.selectedFiles.getFirst();
            if (tile.file.isFile()) {
                tile.openFile();
            } else {
                Config.scrollPane.navigateTo(tile.file);
            }
//            tile.handleDoubleClick();
        }
    }

    private void handleCopy() {
        int n = Config.selectedFiles.size();
        int i = 0;
        while (i < n) {
            System.out.println("copying");
            Config.selectedFiles.get(i).handleCopyMenu();
            i++;
        }
    }

    private void controlSelection(int direction) {
        int newIndex = Config.getCurrentIndex() + direction;

        if (newIndex >= Config.currentFiles.size()) {
            Config.currentFiles.getLast().select();
            return;
        }

        if (newIndex < 0) return;

        if (Config.selectedFiles.contains(Config.currentFiles.get(newIndex))) {
            Config.currentFiles.get(newIndex).unselect();
        } else {
            Config.currentFiles.get(newIndex).select();
        }

        Config.currentFiles.get(newIndex).requestFocus();
    }

    private int prevDirection = 0;

    private void shiftSelection(int direction) {
        int newIndex = Config.getCurrentIndex() + direction;

        if (newIndex >= Config.currentFiles.size()) {
            int i = Config.getCurrentIndex();
            while (i < Config.currentFiles.size()) {
                Config.currentFiles.get(i).select();
            }
            return;
        }

        if (newIndex < 0) return;

        int i;
        if (direction > 0) {
            i = Config.getCurrentIndex() + 1;
        } else {
            i = Config.getCurrentIndex() - 1;
        }

        while (i <= newIndex) {
            if (Config.selectedFiles.contains(Config.currentFiles.get(i))) {
                Config.currentFiles.get(i).unselect();
            } else {
                Config.currentFiles.get(i).select();
            }
            i++;
        }
        Config.currentFiles.get(newIndex).requestFocus();

        prevDirection = direction;
    }

    private void moveSelection(int direction) {
        System.out.println(Config.getCurrentIndex());
        int newIndex = Config.getCurrentIndex() + direction;

        if (newIndex < 0) {
            if (! Config.selectedFiles.contains(Config.currentFiles.getFirst())) {
                Config.unSelectAll();
                Config.currentFiles.getFirst().select();
            }
        } else if (newIndex >= Config.currentFiles.size()) {
            if (! Config.selectedFiles.contains(Config.currentFiles.getLast())) {
                Config.unSelectAll();
                Config.currentFiles.getLast().select();
            }
        } else {
            Config.unSelectAll();
            Config.currentFiles.get(newIndex).select();
            Config.currentFiles.get(newIndex).requestFocus();
        }
        System.out.println(Config.getCurrentIndex());
    }

    private int getRowSize() {
        double panelWidth = this.getWidth() - 2 * Config.filesPanePadding;
        int i = 2;
        int j = 1;
        while (panelWidth >= i * Config.getTileWidth() + j * Config.spacingAmongTiles) {
            i++;
            j++;
        }
        System.out.println("j: " + j);
        return j;
    }

    private void handleCut() {
        int n = Config.selectedFiles.size();
        int i = 0;
        while (i < n) {
            System.out.println("cutting");
            Config.selectedFiles.get(i).handleCutMenu();
            i++;
        }
    }

    private void handleDelete() {
        Config.selectedFiles.getFirst().handleDeleteMenu();
    }

    private void handleNewMenu() {
        System.out.println("new...");
        showNewFileDialog();
    }

    private void showNewFileDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New File/Folder");
        dialog.setHeaderText("Create New File/Folder");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(this::createFile);
    }

    private void createFile(String name) {
        System.out.println("creating file");
        AtomicBoolean flag = new AtomicBoolean(false);
        Config.currentFiles.forEach(tile -> {
            System.out.println("files in folder: " + tile.file);
            if (tile.file.getName().equals(name)) {
                flag.set(true);
            }
        });
        if (flag.get()) {
            showFileAlreadyExistError();
        } else {
            if (name.contains(".")) {
                try {
                    new ProcessBuilder("touch", name)
                            .directory(file)
                            .inheritIO()
                            .start()
                            .waitFor();
                    File newFile = new File(file.getAbsolutePath() + "/" + name);
                    FileTile newTile = new FileTile(newFile);
                    this.getChildren().add(newTile);
                    Config.currentFiles.add(newTile);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    new ProcessBuilder("mkdir", name)
                            .directory(file)
                            .inheritIO()
                            .start()
                            .waitFor();
                    File newFile = new File(file.getAbsolutePath() + "/" + name);
                    FileTile newTile = new FileTile(newFile);
                    this.getChildren().add(newTile);
                    Config.currentFiles.add(newTile);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void showFileAlreadyExistError() {
        System.out.println("File already exist");
    }

    private void handleOpenTerminal() {
        System.out.println("terminal...");
        try {
            System.out.println("opening");
            new ProcessBuilder("bash", "-c", "gnome-terminal --working-directory=" + file.getAbsolutePath()).start();
            System.out.println("opened");
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    private void handlePasteMenu() {
        System.out.println("paste...");
        if (Config.clipBoard != null) {
            if (Config.copy) {
                Config.clipBoard.forEach(tile -> {
                    File sourceFile = tile.file;
                    String source = sourceFile.getAbsolutePath();
                    String destination = file.getAbsolutePath() + "/" + sourceFile.getName();
                    File destinationFile = new File(destination);
                    if (sourceFile.isFile()) {
                        try {
                            new ProcessBuilder("cp", source, destination).start();
                            this.getChildren().add(new FileTile(destinationFile));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        try {
                            new ProcessBuilder("cp", "-r", source, destination).start();
                            this.getChildren().add(new FileTile(destinationFile));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    Config.copy = false;
                });
//                File sourceFile = Config.clipBoard.getFirst().file;
            } else if (Config.cut) {
                Config.clipBoard.forEach(tile -> {
                    File sourceFile = tile.file;
                    String source = sourceFile.getAbsolutePath();
                    String destination = file.getAbsolutePath() + "/" + sourceFile.getName();
                    File destinationFile = new File(destination);
                    try {
                        new ProcessBuilder("mv", source, destination).start();
                        this.getChildren().add(new FileTile(destinationFile));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Config.cut = false;
                });
            }
        }
    }

    private void handleSelectAllMenu() {
        System.out.println("select all...");
        this.getChildren().forEach(tile -> ((FileTile) tile).select());
    }

    private void handleInvertSelectionMenu() {
        System.out.println("invert selection...");
        System.out.println(Config.selectedFiles);
        this.getChildren().forEach(tile -> {
            FileTile t = ((FileTile) tile);

            if (Config.selectedFiles.contains(t)) {
                t.unselect();
            } else {
                t.select();
            }
        });
    }

    private void handleShowProperties() {
        System.out.println("properties...");
        Config.showProperties();
    }

}
