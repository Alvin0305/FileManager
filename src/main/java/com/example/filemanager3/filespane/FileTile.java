package com.example.filemanager3.filespane;

import com.example.filemanager3.Config;
import com.example.filemanager3.FileOperations;
import com.example.filemanager3.View;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class FileTile extends StackPane {
    public File file;
    View view;
    Rectangle background = new Rectangle();
    ImageView icon = new ImageView();
    String fileNameText;
    Text fileName = new Text();

    ContextMenu contextMenu = new ContextMenu();
    MenuItem openMenu = new MenuItem("Open");
    MenuItem openWithMenu = new MenuItem("OpenWith");
    MenuItem cutMenu = new MenuItem("Cut");
    MenuItem copyMenu = new MenuItem("Copy");
    MenuItem pasteMenu = new MenuItem("Paste");
    MenuItem renameMenu = new MenuItem("Rename");
    MenuItem compressMenu = new MenuItem("Compress");
    MenuItem deleteMenu = new MenuItem("Delete");
    MenuItem propertiesMenu = new MenuItem("Properties");

    public FileTile(File file) {
        this(file, Config.view);
    }

    public FileTile(File file, View view) {
        this.file = file;
        this.view = view;
        this.fileNameText = file.getName();

        if (Config.iconViews.contains(view)) {
            drawIconView();
        } else if (Config.view.equals(View.TILE)) {
            drawTileView();
        }

        assembleContextMenu();

        this.getStyleClass().add("file-tile");
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("filespane-styles.css")).toExternalForm());
    }

    public void select() {
        background.setFill(Config.activeColor);
        if (! Config.selectedFiles.contains(this)) {
            Config.selectedFiles.add(this);
            System.out.println("selected: " + Config.selectedFiles.getFirst().file);
        }
    }

    public void unselect() {
        background.setFill(Config.backgroundColor);
        Config.selectedFiles.remove(this);
    }

    private void drawIconView() {
        double tileDimensions;
        double borderRadius;
        double iconDimensions;
        if (view.equals(View.LARGE)) {
            tileDimensions = Config.largeTileSize;
            borderRadius = Config.largeTileBorderRadius;
            iconDimensions = Config.largeIconSize;
        } else if (view.equals(View.MEDIUM)) {
            tileDimensions = Config.mediumTileSize;
            borderRadius = Config.mediumTileBorderRadius;
            iconDimensions = Config.mediumIconSize;
        } else {
            tileDimensions = Config.smallTileSize;
            borderRadius = Config.smallTileBorderRadius;
            iconDimensions = Config.smallIconSize;
        }

        this.getChildren().clear();
        VBox content = new VBox();

        background.setHeight(tileDimensions);
        background.setWidth(tileDimensions);
        background.setFill(Config.backgroundColor);
        background.setArcHeight(borderRadius);
        background.setArcWidth(borderRadius);

        addListener();

        icon.setImage(FileOperations.getImage(file));
        icon.setFitHeight(iconDimensions);
        icon.setFitWidth(iconDimensions);

        fileName.setText(getTrimmedName());
        fileName.setFill(Config.fileNameColor);

        content.getChildren().addAll(icon, fileName);

        StackPane.setAlignment(content, Pos.CENTER);
        content.setAlignment(Pos.CENTER);
        this.getChildren().addAll(background, content);

        fileName.getStyleClass().add("filename");
        background.getStyleClass().add("background");
    }

    private void drawTileView() {
        double tileWidth = Config.tileWidth;
        double tileHeight = Config.tileHeight;
        double borderRadius = Config.tileBorderRadius;
        double iconDimensions = Config.tileIconSize;

        this.getChildren().clear();
        HBox content = new HBox();

        background.setHeight(tileHeight);
        background.setWidth(tileWidth);
        background.setFill(Config.backgroundColor);
        background.setArcHeight(borderRadius);
        background.setArcWidth(borderRadius);

        addListener();

        icon.setImage(FileOperations.getImage(file));
        icon.setFitHeight(iconDimensions);
        icon.setFitWidth(iconDimensions);

        fileName = new Text(getTrimmedName());
        fileName.setFill(Config.fileNameColor);

        content.getChildren().addAll(icon, fileName);

        StackPane.setAlignment(content, Pos.CENTER);
        content.setAlignment(Pos.CENTER_LEFT);
        this.getChildren().addAll(background, content);

        fileName.getStyleClass().add("filename");
        background.getStyleClass().add("background");
    }

    private void addListener() {

        this.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.F2)) {
                System.out.println("F2");
                handleRenameMenu();
            }
        });

        this.setOnMouseClicked(event -> {
            this.requestFocus();
            background.setFill(Config.activeColor);

            if (event.getClickCount() == 1) {
                System.out.println("pressed");
                if (event.isShiftDown()) {
                    handleShiftSelect();
                } else if (! event.isControlDown() && event.getButton() != MouseButton.SECONDARY) {
                    Config.unSelectAll();
                }
                if (event.getButton() == MouseButton.SECONDARY) {
                    System.out.println("Right button pressed");
                    showContextMenu(event);
                }
                select();
                showDetails(event);
            } else if (event.getClickCount() == 2) {
                handleDoubleClick();
            }
            event.consume();
        });

        this.setOnMouseEntered(event -> {
            if (! Config.selectedFiles.contains(this)) {
                background.setFill(Config.hoverColor);
            }
            event.consume();
        });

        this.setOnMouseExited(event -> {
            if (! Config.selectedFiles.contains(this)) {
                background.setFill(Config.backgroundColor);
            }
            event.consume();
        });

        this.setOnDragDetected(event -> {
            Config.draggedFile = this;
            Dragboard dragboard = this.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(file.getAbsolutePath());
            dragboard.setContent(content);

            event.consume();
        });

        this.setOnDragOver(event -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        this.setOnDragEntered(event -> this.setStyle("-fx-background-color: lightgray;"));
        this.setOnDragExited(event -> this.setStyle("-fx-background-color: transparent;"));


        this.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;

            if (dragboard.hasString()) {
                String filePath = dragboard.getString();
                File sourceFile = new File(filePath);
                File targetFolder = new File(this.file.getAbsolutePath());

                if (sourceFile.exists() && targetFolder.isDirectory()) {
                    File targetFile = new File(targetFolder, sourceFile.getName());
                    success = sourceFile.renameTo(targetFile);
                    Config.flowPane.getChildren().remove(Config.draggedFile);
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void handleShiftSelect() {
        if (Config.selectedFiles.isEmpty()) {
            return;
        }
        FileTile lastSelected = Config.selectedFiles.getLast();
        int start = Config.flowPane.getChildren().indexOf(lastSelected);
        int end = Config.flowPane.getChildren().indexOf(this);
        System.out.println(start + " :: " + end);
        if (start < end) {
            Config.flowPane.getChildren().subList(start, end + 1).forEach(tile -> ((FileTile) tile).select());
        } else {
            Config.flowPane.getChildren().subList(end, start + 1).forEach(tile -> ((FileTile) tile).select());
        }
    }

    public void handleDoubleClick() {
        if (file.isFile()) {
            System.out.println("opening file: " + file);
            openFile();
        } else {
            Config.scrollPane.navigateTo(file);
        }
    }

    public void openFile() {
        try {
            new ProcessBuilder("xdg-open", file.toString()).start();
        } catch (IOException e) {
            System.out.println("Sorry I can't open " + file);
        }
    }

    private void showDetails(MouseEvent event) {
        System.out.println("showing details of the file: " + file);
        System.out.println("file name: " + file.getName());
        System.out.println("file size: " + file.length());
        if (Config.showDescription) {
            Config.showDescription();
        }
    }

    private String getTrimmedName() {
        int threshold;
        if (view.equals(View.LARGE)) {
            threshold = Config.largeIconViewTextThreshold;
        } else if (view.equals(View.MEDIUM)) {
            threshold = Config.mediumIconViewTextThreshold;
        } else if (view.equals(View.SMALL)) {
            threshold = Config.smallIconViewTextThreshold;
        } else {
            threshold = Config.tileViewTextThreshold;
        }

//        fileName.setWrappingWidth(threshold * 20);
//        return fileNameText;

        if (fileNameText.length() < threshold) {
            if (Config.showFileExtension) {
                return fileNameText;
            } else {
                int lastIndex = fileNameText.lastIndexOf(".");
                if (lastIndex == -1) {
                    return fileNameText;
                } else {
                    return fileNameText.substring(0, lastIndex);
                }
            }
        } else if (Config.showFileExtension) {
            return fileNameText.substring(0, threshold) + "...";
        } else {
            int lastIndex = fileNameText.lastIndexOf(".");
            if (lastIndex == -1) {
                return fileNameText.substring(0, threshold) + "...";
            } else {
                return fileNameText.substring(0, Math.min(threshold, lastIndex)) + "...";
            }
        }
    }

    private void assembleContextMenu() {
        contextMenu.getItems().addAll(openMenu, openWithMenu, cutMenu, copyMenu, pasteMenu,
                renameMenu, compressMenu, deleteMenu, propertiesMenu);
        addMenuListeners();
    }

    private void showContextMenu(MouseEvent event) {
        contextMenu.show(this, event.getScreenX(), event.getScreenY());
    }

    private void addMenuListeners() {
        openMenu.setOnAction(event -> handleDoubleClick());
        openWithMenu.setOnAction(event -> handleOpenWithMenu());
        cutMenu.setOnAction(event -> handleCutMenu());
        copyMenu.setOnAction(event -> handleCopyMenu());
        pasteMenu.setOnAction(event -> handlePasteMenu());
        renameMenu.setOnAction(event -> handleRenameMenu());
        compressMenu.setOnAction(event -> handleCompressMenu());
        deleteMenu.setOnAction(event -> handleDeleteMenu());
        propertiesMenu.setOnAction(event -> showProperties());
    }

    private void handleOpenWithMenu() {
        System.out.println("open with...");
    }

    public void handleCutMenu() {
        System.out.println("cut...");
        Config.cut = true;
        Config.copy = false;
        Config.clipBoard.clear();
        Config.clipBoard.addAll(Config.selectedFiles);
    }

    public void handleCopyMenu() {
        System.out.println("copy...");
        Config.copy = true;
        Config.cut = false;
        Config.clipBoard.clear();
        Config.clipBoard.addAll(Config.selectedFiles);
    }

    private void handlePasteMenu() {
        System.out.println("paste...");
        if (Config.cut) {
            handleCut();
        } else {
            handleCopy();
        }
        Config.copy = Config.clipboardPersistence;
        Config.cut = false;
        Config.clipBoard.clear();
    }

    private void handleCut(ArrayList<FileTile> sourceFiles, FileTile destinationFile) {
        if (Config.cut && sourceFiles != null) {
            sourceFiles.forEach(tile -> {
                File sourceFile = tile.file;
                System.out.println("moving file: " + sourceFile + " to " + destinationFile);
                try {
                    new ProcessBuilder("mv", sourceFile.getAbsolutePath(), destinationFile.file.getAbsolutePath()).start();
                } catch (IOException e) {
                    System.out.println("Error in moving");
                }

                if (Config.currentFiles.contains(tile)) {
                    Config.flowPane.getChildren().remove(tile);
                    Config.currentFiles.remove(tile);
                }
            });

        } else if (sourceFiles == null) {
            System.out.println("clipboard is empty");
        }
    }

    private void handleCut() {
        handleCut(Config.clipBoard, this);
    }

    private void handleCopy(ArrayList<FileTile> sourceFiles, File destinationFile) {
        if (Config.copy && sourceFiles != null) {
            sourceFiles.forEach(tile -> {
                File sourceFile = tile.file;
                String source = sourceFile.getAbsolutePath();
                String destination = file.getAbsolutePath();
                System.out.println("copying file: " + source + " to " + destination);
                try {
                    if (sourceFile.isFile() && destinationFile.isDirectory()) {
                        new ProcessBuilder("cp", source, destination).start();
                    } else if (sourceFile.isDirectory() && destinationFile.isDirectory()) {
                        new ProcessBuilder("cp", "-r", source, destination).start();
                    }
                } catch (IOException e) {
                    System.out.println("Error in copying");
                }
            });
//            String source = sourceFiles.getFirst().file.getAbsolutePath();
        } else if (sourceFiles == null) {
            System.out.println("clipboard is empty");
        }
    }

    private void handleCopy() {
        handleCopy(Config.clipBoard, file);
    }

    private void handleRenameMenu() {
        System.out.println("rename...");
        showRenameDialog();
    }

    private void showRenameDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Rename");
        dialog.setHeaderText("Rename File/Folder");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(this::renameFile);
    }

    private void renameFile(String name) {
        AtomicBoolean flag = new AtomicBoolean(false);
        Config.currentFiles.forEach(tile -> {
            if (tile.file.getName().equals(name)) {
                showRenameError();
                flag.set(true);
            }
        });
        if (! flag.get()) {
            Path source = this.file.toPath();
            Path destination = Path.of(this.file.getParentFile().getAbsoluteFile() + "/" + name);
            System.out.println("renaming " + source + " to " + destination);
            try {
                java.nio.file.Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
                fileNameText = name;
                fileName.setText(getTrimmedName());
                System.out.println("renamed");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void showRenameError() {
        System.out.println("rename error");
    }

    public void handleDeleteMenu() {
        System.out.println("delete...");
        if (Config.confirmBeforeDelete) {
            boolean result = showConfirmDeleteDialogBox();
            if (! result) return;
            System.out.println("deleting file");
        }
        Config.selectedFiles.forEach(tile -> {
            File file = tile.file;
            try {
                new ProcessBuilder("rm", "-r", file.toString()).start();
                Config.currentFiles.remove(tile);
                Config.flowPane.getChildren().remove(tile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private boolean showConfirmDeleteDialogBox() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Are you sure you want to delete");
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void handleCompressMenu() {
        System.out.println("compress...");
    }

    private void showProperties() {
        System.out.println("properties...");
        Config.showProperties();
    }

    private Image getImage() {
        return new Image(Objects.requireNonNull(Config.class.getResourceAsStream("folder.png")));
    }
}
