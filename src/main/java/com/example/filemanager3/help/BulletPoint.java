package com.example.filemanager3.help;

import com.example.filemanager3.Config;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Objects;

public class BulletPoint extends HBox {
    public BulletPoint(FontAwesomeSolid icon, String heading, String content) {
        this.setSpacing(Config.helpSpacing);

        Text indent = new Text("\t");
        FontIcon fontIcon = new FontIcon(icon);
        fontIcon.setFill(Config.helpIconColor);
        fontIcon.setIconSize(15);

        Text headingText = new Text(heading);
        Text contentText = new Text(" - " + content);

        headingText.getStyleClass().add("bullet-heading-text");
        contentText.getStyleClass().add("bullet-content-text");
        this.getChildren().addAll(indent, fontIcon, headingText, contentText);
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("help-styles.css")).toExternalForm());
    }

    public BulletPoint(FontAwesomeSolid icon, String heading) {
        this.setSpacing(Config.helpSpacing);

        Text indent = new Text("\t");
        FontIcon fontIcon = new FontIcon(icon);
        fontIcon.setFill(Config.helpIconColor);
        fontIcon.setIconSize(15);

        Text headingText = new Text(heading);

        headingText.getStyleClass().add("bullet-heading-text");
        this.getChildren().addAll(indent, fontIcon, headingText);
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("help-styles.css")).toExternalForm());
    }

    public BulletPoint(FontAwesomeSolid icon, String heading, boolean flag) {
        this.setSpacing(Config.helpSpacing);

        Text indent = new Text();
        if (flag) {
            indent.setText("\t\t");
        }
        FontIcon fontIcon = new FontIcon(icon);
        fontIcon.setFill(Config.helpIconColor);
        fontIcon.setIconSize(15);

        Text headingText = new Text(heading);

        headingText.getStyleClass().add("bullet-heading-text");
        this.getChildren().addAll(indent, fontIcon, headingText);
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("help-styles.css")).toExternalForm());
    }

    public BulletPoint(String heading, boolean flag) {
        this.setSpacing(Config.helpSpacing);

        if (flag) {
            Text indent = new Text("\t");
            Text headingText = new Text(heading);

            headingText.getStyleClass().add("bullet-heading-text");
            this.getChildren().addAll(indent, headingText);
            this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("help-styles.css")).toExternalForm());
        }
    }

}
