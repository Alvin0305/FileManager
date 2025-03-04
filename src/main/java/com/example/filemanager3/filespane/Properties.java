package com.example.filemanager3.filespane;

import com.example.filemanager3.Config;
import com.example.filemanager3.FileOperations;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.util.Objects;

public class Properties extends VBox {
    FontIcon starIcon = new FontIcon(FontAwesomeSolid.STAR);
    FontIcon editIcon = new FontIcon(FontAwesomeSolid.EDIT);
    FontIcon permissionsIcon = new FontIcon(FontAwesomeSolid.ANGLE_RIGHT);

    Button starButton = new Button();
    Button editButton = new Button();

    Image fileImage;
    ImageView imageView;

    Text fileName = new Text();
    Text count = new Text();
    Text size = new Text();
    HBox countAndSize = new HBox(count, size);

    Text locationLabel = new Text("Location");
    Text location = new Text();
    VBox locationVBox = new VBox(locationLabel, location);
    Region spacer1 = new Region();

    Text accessedLabel = new Text("Accessed");
    Text accessed = new Text();
    VBox accessedVBox = new VBox(accessedLabel, accessed);
    Separator separator1 = new Separator(Orientation.HORIZONTAL);
    Text modifiedLabel = new Text("Modified");
    Text modified = new Text();
    VBox modifiedVBox = new VBox(modifiedLabel, modified);
    Separator separator2 = new Separator(Orientation.HORIZONTAL);
    Text createdLabel = new Text("Created");
    Text created = new Text();
    VBox createdVBox = new VBox(createdLabel, created);
    VBox dateVBox = new VBox(accessedVBox, separator1, modifiedVBox, separator2, createdVBox);
    Region spacer2 = new Region();

    Text permissionsLabel = new Text("Permissions");
    Text permissions = new Text();
    Button permissionsButton = new Button();
    Region spacer = new Region();
    HBox permissionsHBox = new HBox(permissionsLabel,  spacer, permissions, permissionsButton);

    public Properties(File file) {
        this.setPrefSize(Config.propertiesWidth, Config.propertiesHeight);
        this.setAlignment(Pos.CENTER);

        HBox starButtonWrapper = new HBox(starButton);

        starButton.setAlignment(Pos.BASELINE_LEFT);
        count.setTextAlignment(TextAlignment.CENTER);
        countAndSize.setAlignment(Pos.CENTER);
        size.setTextAlignment(TextAlignment.CENTER);
        HBox.setHgrow(spacer, Priority.ALWAYS);
        spacer1.setPrefHeight(10);
        spacer2.setPrefHeight(10);
        locationVBox.setAlignment(Pos.CENTER_LEFT);
        dateVBox.setAlignment(Pos.CENTER_LEFT);
        permissionsHBox.setAlignment(Pos.CENTER);
        VBox.setMargin(countAndSize, new Insets(10, 0, 10, 0));
        VBox.setMargin(location, new Insets(5, 0, 0, 0));
        VBox.setMargin(modifiedLabel, new Insets(5, 0, 0, 0));
        VBox.setMargin(createdLabel, new Insets(5, 0, 0, 0));
        VBox.setMargin(accessed, new Insets(5, 0, 5, 0));
        VBox.setMargin(created, new Insets(5, 0, 0, 0));
        VBox.setMargin(modified, new Insets(5, 0, 5, 0));

        starIcon.setIconColor(Color.WHITE);
        editIcon.setIconColor(Color.WHITE);
        permissionsIcon.setIconColor(Color.WHITE);

        starButton.setGraphic(starIcon);
        editButton.setGraphic(editIcon);
        permissionsButton.setGraphic(permissionsIcon);

        fileImage = FileOperations.getImage(file);
        imageView = new ImageView(fileImage);
        imageView.setFitWidth(Config.propertiesImageDimensions);
        imageView.setFitHeight(Config.propertiesImageDimensions);

        StackPane imageAndButton = new StackPane(imageView, editButton);
        imageAndButton.setPrefSize(Config.propertiesImageDimensions, Config.propertiesImageDimensions);
        imageAndButton.setMinSize(Config.propertiesImageDimensions, Config.propertiesImageDimensions);
        imageAndButton.setMaxSize(Config.propertiesImageDimensions, Config.propertiesImageDimensions);
        StackPane.setAlignment(editButton, Pos.BOTTOM_RIGHT);

        fileName.setText(file.getName());
        size.setText(FileOperations.getReadableSize(file));
        int children = FileOperations.countChildren(file);
        if (children == 1) {
            count.setText("1 child, ");
        } else {
            count.setText(children + " children, ");
        }

        location.setText(file.getAbsolutePath());
        accessed.setText(FileOperations.getAccessedTime(file));
        modified.setText(FileOperations.getModifiedTime(file));
        created.setText(FileOperations.getCreatedTime(file));
        permissions.setText(FileOperations.getPermissions(file));

        this.getChildren().addAll(starButtonWrapper, imageAndButton, fileName, countAndSize,
                locationVBox, spacer1, dateVBox, spacer2, permissionsHBox);

        starButton.getStyleClass().add("prop-button");
        editButton.getStyleClass().add("prop-button");
        fileName.getStyleClass().add("prop-file-name");
        count.getStyleClass().add("prop-text");
        size.getStyleClass().add("prop-text");
        locationLabel.getStyleClass().add("prop-label");
        accessedLabel.getStyleClass().add("prop-label");
        modifiedLabel.getStyleClass().add("prop-label");
        createdLabel.getStyleClass().add("prop-label");
        permissionsLabel.getStyleClass().add("prop-label");
        location.getStyleClass().add("prop-text");
        accessed.getStyleClass().add("prop-text");
        modified.getStyleClass().add("prop-text");
        created.getStyleClass().add("prop-text");
        permissions.getStyleClass().add("prop-text");
        locationVBox.getStyleClass().add("prop-container");
        dateVBox.getStyleClass().add("prop-container");
        permissionsHBox.getStyleClass().add("prop-container");
        permissionsHBox.getStyleClass().add("permission-container");
        permissionsButton.getStyleClass().add("prop-permission-button");
        this.getStyleClass().add("properties");
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("filespane-styles.css")).toExternalForm());
    }
}
