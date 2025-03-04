package com.example.filemanager3;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Objects;

public class About extends HBox {
    VBox contents = new VBox(5);
    Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("hashtag.png")));
    ImageView imageView = new ImageView(image);

    Text appName = new Text("D-Files 25.1 (Community Edition)");
    Text builtOn = new Text("Built on " + Config.builtOn);
    Text javaVersionDetails = new Text("JDK Version used: 21.0.5");
    Text javacVersionDetails = new Text("javac Version used: 21.0.5");
    Text ideUsed = new Text("Built using IntelliJ IDEA");
    Text copyright = new Text("Copyright: 2025-2050");
    Text createdBy = new Text("Created by Alvin A S");
    Region spacer1 = new Region();

    Button copyAndClose = new Button("Copy And Close");
    Button close = new Button("Close");
    Region spacer = new Region();
    Region spacer2 = new Region();
    HBox buttons = new HBox(20, spacer, copyAndClose, close, spacer2);

    public About() {
        imageView.setFitHeight(Config.aboutImageDimensions);
        imageView.setFitWidth(Config.aboutImageDimensions);

        contents.getChildren().addAll(appName, builtOn, javaVersionDetails, javacVersionDetails, ideUsed,
                copyright, createdBy, spacer1, buttons);

        this.setAlignment(Pos.CENTER);
        contents.setAlignment(Pos.CENTER_LEFT);
        this.getChildren().addAll(imageView, contents);

        spacer1.setPrefHeight(10);
        spacer2.setPrefWidth(10);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        copyAndClose.setOnAction(event -> handleCopyAndClose());
        close.setOnAction(event -> handleClose());

        appName.getStyleClass().add("about-app-name");
        builtOn.getStyleClass().add("about-text");
        javaVersionDetails.getStyleClass().add("about-text");
        javacVersionDetails.getStyleClass().add("about-text");
        ideUsed.getStyleClass().add("about-text");
        copyright.getStyleClass().add("about-text");
        createdBy.getStyleClass().add("about-text");
        copyAndClose.getStyleClass().add("copy-and-close");
        close.getStyleClass().add("close");
        this.getStyleClass().add("about");
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
    }

    private void handleCopyAndClose() {
        copy();
        handleClose();
    }

    private void copy() {
        String content = """
                D-Files 25.1 (Community Edition)
                Built on 2025 March
                JDK Version Used: 21.0.5
                javac Version Used: 21.0.5
                Built using IntelliJ IDEA
                Copyright 2025-2050
                Created by: Alvin A S
                """;
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(content);
        Clipboard clipboard = Clipboard.getSystemClipboard();
        clipboard.setContent(clipboardContent);

    }

    private void handleClose() {
        ((Stage) this.getScene().getWindow()).close();
    }
}
