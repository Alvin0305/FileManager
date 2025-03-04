package com.example.filemanager3.help;

import com.example.filemanager3.Config;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.util.Objects;

public class Help extends VBox {

    public Help(ScrollPane parent) {
        this.setSpacing(5);
        this.prefWidthProperty().bind(parent.widthProperty());

        Heading h1 = new Heading("1. Navigation Options");
        Region r1 = new Region();
        r1.setPrefHeight(Config.belowHeading);

        SubHeading s1 = new SubHeading("Top Bar Options");
        Region r2 = new Region();
        r2.setPrefHeight(Config.belowSubHeading);

        BulletPoint b11 = new BulletPoint(FontAwesomeSolid.ARROW_UP,
                "Parent Folder", "Move to the Parent Directory");
        BulletPoint b12 = new BulletPoint(FontAwesomeSolid.SEARCH,
                "System Search", "Search for file/folder across the entire system");
        BulletPoint b13 = new BulletPoint(FontAwesomeSolid.BARS,
                "Menu Button", "Access Undo, Redo, Preferences, Keyboard Shortcuts, Help and About");
        BulletPoint b14 = new BulletPoint(FontAwesomeSolid.ARROW_LEFT,
                "Navigate Back", "Return to last opened folder");
        BulletPoint b15 = new BulletPoint(FontAwesomeSolid.ARROW_RIGHT,
                "Navigate Front", "Move to the next opened folder");
        BulletPoint b16 = new BulletPoint(FontAwesomeSolid.LOCATION_ARROW,
                "Address Bar", "Enter or view current folder path");
        BulletPoint b17 = new BulletPoint(FontAwesomeSolid.SEARCH_LOCATION,
                "Search Current Folder", "Search files/folders within the current directory");
        BulletPoint b18 = new BulletPoint(FontAwesomeSolid.TH_LARGE,
                "View Button", "Change the view and sorting method of the files");
        Region r3 = new Region();
        r3.setPrefHeight(Config.afterContent);

        SubHeading s2 = new SubHeading("Side Bar (Quick Access)");
        Region r4 = new Region();
        r4.setPrefHeight(Config.belowSubHeading);

        Text t1 = new Text("Provide shortcuts to frequently used locations like");
        BulletPoint b21 = new BulletPoint(FontAwesomeSolid.HOME, "Home");
        BulletPoint b22 = new BulletPoint(FontAwesomeSolid.HOME, "Recent");
        BulletPoint b23 = new BulletPoint(FontAwesomeSolid.STAR, "Starred");
        BulletPoint b24 = new BulletPoint(FontAwesomeSolid.NETWORK_WIRED, "Network");
        BulletPoint b25 = new BulletPoint(FontAwesomeSolid.TRASH, "Trash");
        BulletPoint b26 = new BulletPoint(FontAwesomeSolid.FILE_ALT, "Documents");
        BulletPoint b27 = new BulletPoint(FontAwesomeSolid.MUSIC, "Music");
        BulletPoint b28 = new BulletPoint(FontAwesomeSolid.VIDEO, "Videos");
        BulletPoint b29 = new BulletPoint(FontAwesomeSolid.IMAGE, "Pictures");
        BulletPoint b210 = new BulletPoint(FontAwesomeSolid.DOWNLOAD, "Downloads");
        Region r5 = new Region();
        r5.setPrefHeight(Config.afterContent);

        Heading h2 = new Heading("2. View Options");
        Region r6 = new Region();
        r6.setPrefHeight(Config.belowHeading);
        Text t2 = new Text("You can customize how files are displayed using the View Button");

        SubHeading s3 = new SubHeading( "Icon Size");
        Region r7 = new Region();
        r7.setPrefHeight(Config.belowSubHeading);

        BulletPoint b32 = new BulletPoint(FontAwesomeSolid.TH, "Small Icon");
        BulletPoint b33 = new BulletPoint(FontAwesomeSolid.TH_LARGE, "Medium Icon");
        BulletPoint b34 = new BulletPoint(FontAwesomeSolid.BORDER_ALL, "Large Icon");
        BulletPoint b35 = new BulletPoint(FontAwesomeSolid.LIST, "Tile View");
        Region r8 = new Region();
        r8.setPrefHeight(Config.afterContent);

        SubHeading s4 = new SubHeading("Sorting Options");
        Region r9 = new Region();
        r9.setPrefHeight(Config.belowSubHeading);

        BulletPoint b41 = new BulletPoint(FontAwesomeSolid.SORT_ALPHA_DOWN, "Alphabetic");
        BulletPoint b42 = new BulletPoint(FontAwesomeSolid.SORT_ALPHA_DOWN_ALT, "Reverse Alphabetic");
        BulletPoint b43 = new BulletPoint(FontAwesomeSolid.CLOCK, "Last Modified");
        BulletPoint b44 = new BulletPoint(FontAwesomeSolid.HISTORY, "First Modified");
        BulletPoint b45 = new BulletPoint(FontAwesomeSolid.SORT_NUMERIC_DOWN, "Size");
        Region r10 = new Region();
        r10.setPrefHeight(Config.afterContent);

        SubHeading s5 = new SubHeading("Other View Options");
        Region r11 = new Region();
        r11.setPrefHeight(Config.belowSubHeading);

        BulletPoint b51 = new BulletPoint(FontAwesomeSolid.EYE_SLASH, "Show Hidden Files");
        BulletPoint b52 = new BulletPoint(FontAwesomeSolid.INFO_CIRCLE, "Show Description Panel");
        BulletPoint b53 = new BulletPoint("Displays file/folder details on the right panel, including", true);
        BulletPoint b54 = new BulletPoint(FontAwesomeSolid.FILE_ALT, "Name", true);
        BulletPoint b55 = new BulletPoint(FontAwesomeSolid.FOLDER, "Type", true);
        BulletPoint b56 = new BulletPoint(FontAwesomeSolid.TH, "Size", true);
        BulletPoint b57 = new BulletPoint(FontAwesomeSolid.CLOCK, "Created and Modified Date", true);
        BulletPoint b58 = new BulletPoint(FontAwesomeSolid.LOCATION_ARROW, "Absolute Path", true);
        BulletPoint b59 = new BulletPoint(FontAwesomeSolid.SEARCH, "Properties Button for more details", true);
        Region r12 = new Region();
        r12.setPrefHeight(Config.afterContent);

        Button aboutButton = new Button("About");
        Button shortcutsButton = new Button("Shortcuts");
        Button closeButton = new Button("Close");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox buttons = new HBox(10, spacer, aboutButton, shortcutsButton, closeButton);

        this.getChildren().addAll(h1, r1, s1, r2, b11, b12, b13, b14, b15, b16, b17, b18, r3, s2, r4, t1, b21, b22, b23, b24, b25,
                b26, b27, b28, b29, b210, r5, h2, r6, t2, s3, r7, b32, b33, b34, b35, r8, s4, r9, b41, b42, b43, b44, b45, r10, s5, r11,
                b51, b52, b53, b54, b55, b56, b57, b58, b59, r12, buttons);

        this.setFillWidth(true);

        aboutButton.setOnAction(event -> Config.topBar.handleAboutMenu());
        shortcutsButton.setOnAction(event -> Config.topBar.handleShowShortcuts());
        closeButton.setOnAction(event -> handleClose());

        t1.getStyleClass().add("help-text");
        t2.getStyleClass().add("help-text");
        aboutButton.getStyleClass().add("help-button");
        closeButton.getStyleClass().add("help-button");
        shortcutsButton.getStyleClass().add("help-button");
        this.getStyleClass().add("help");
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("help-styles.css")).toExternalForm());
    }

    private void handleClose() {
        ((Stage) this.getScene().getWindow()).close();
    }
}
