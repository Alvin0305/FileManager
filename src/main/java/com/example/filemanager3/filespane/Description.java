package com.example.filemanager3.filespane;

import com.example.filemanager3.Config;
import com.example.filemanager3.FileOperations;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.util.Objects;

public class Description extends VBox {
    File file;
    Image fileImage;
    ImageView imageView;
    Text fileName;
    Text fileCount;
    Text detailsLabel = new Text("Details");
    Button properties = new Button("Properties");

    public Description() {
        Config.description = this;
        this.setPrefWidth(Config.descriptionWidth);
        this.setSpacing(5);

        drawPlaneDescription();

        this.getStyleClass().add("description");
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("filespane-styles.css")).toExternalForm());
    }

    public Description(File file) {
        this.file = file;
        Config.description = this;
        draw();

        fileName.getStyleClass().add("file-name");
        detailsLabel.getStyleClass().add("details");

        this.getStyleClass().add("description");
        properties.getStyleClass().add("description-button");
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("filespane-styles.css")).toExternalForm());
    }

    public void setFile(File file) {
        this.file = file;
        draw();
    }

    private void draw() {
        Config.description = this;
        System.out.println("drawing description");
        this.setSpacing(5);

        Region spacer1 = new Region();
        spacer1.setPrefHeight(10);
        Region spacer2 = new Region();
        spacer2.setPrefHeight(5);
        Region spacer3 = new Region();
        spacer3.setPrefHeight(20);

        fileImage = FileOperations.getImage(file);
        imageView = new ImageView(fileImage);
        imageView.setFitHeight(Config.descriptionImageDimensions);
        imageView.setFitWidth(Config.descriptionImageDimensions);
        StackPane image = new StackPane(imageView);
        StackPane.setAlignment(image, Pos.CENTER);

        fileName = new Text(file.getName());
        fileName.setFill(Color.WHITE);

        detailsLabel.setFill(Color.WHITE);

        this.getChildren().clear();

        if (file.isFile()) {
            Pair type = new Pair("Type", "File");
            this.getChildren().addAll(image, fileName, spacer1, detailsLabel, spacer2, type);
        } else {
            Pair type = new Pair("Type", "Folder");
            Pair count = new Pair("Children", String.valueOf(Objects.requireNonNull(file.listFiles()).length));
            this.getChildren().addAll(image, fileName, spacer1, detailsLabel, spacer2, type, count);
        }

        Pair size = new Pair("Size", FileOperations.getReadableSize(file));
        Pair location = new Pair("File Location", file.getAbsolutePath());
        Pair modifiedDate = new Pair("Date Modified", FileOperations.getModifiedTime(file));
        Pair createdDate = new Pair("Created Date", FileOperations.getCreatedTime(file));

        properties.setOnAction(event -> Config.showProperties());

        this.getChildren().addAll(size, location, modifiedDate, createdDate, spacer3, properties);
    }


    private void drawPlaneDescription() {
        FontIcon icon = new FontIcon(FontAwesomeSolid.INFO_CIRCLE);
        icon.setIconColor(Color.WHITE);
        Region spacer1 = new Region();
        spacer1.setPrefWidth(50);
        Text label = new Text("Select A Single File/Folder \n to get more information");
        label.setFill(Color.WHITE);
        HBox iconAndLabel = new HBox(icon, spacer1, label);
        iconAndLabel.setAlignment(Pos.CENTER);

        fileImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("folder.png")));
        imageView = new ImageView(fileImage);
        imageView.setFitWidth(Config.descriptionImageDimensions);
        imageView.setFitHeight(Config.descriptionImageDimensions);

        file = Config.currentFolder;

        fileName = new Text(file.getName());
        fileName.setFill(Color.WHITE);
        fileCount = new Text(Objects.requireNonNull(file.listFiles()).length + " items");
        fileCount.setFill(Color.WHITE);

        Region spacer2 = new Region();
        spacer2.setPrefHeight(10);

        this.getChildren().addAll(imageView, fileName, fileCount, spacer2, iconAndLabel);
        this.setAlignment(Pos.CENTER);

        label.getStyleClass().add("label");
        iconAndLabel.getStyleClass().add("icon-and-label");
    }
}
