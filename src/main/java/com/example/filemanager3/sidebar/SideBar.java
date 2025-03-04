package com.example.filemanager3.sidebar;

import com.example.filemanager3.Config;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class SideBar extends VBox {
    SideButton home = new SideButton("Home", "/home/alvin");
    SideButton recent = new SideButton("Recent", "/home/alvin");
    SideButton starred = new SideButton("Starred", "/home/alvin");
    SideButton network = new SideButton("Network", "/home/alvin");
    SideButton trash = new SideButton("Trash", "/home/alvin");

    Separator horizontal = new Separator(Orientation.HORIZONTAL);

    SideButton documents = new SideButton("Documents", "/home/alvin/Documents");
    SideButton music = new SideButton("Music", "/home/alvin/Music");
    SideButton pictures = new SideButton("Pictures", "/home/alvin/Pictures");
    SideButton videos = new SideButton("Videos", "/home/alvin/Videos");
    SideButton downloads = new SideButton("Downloads", "/home/alvin/Downloads");

    public SideBar() {
        this.getChildren().addAll(home, recent, starred, network, trash, horizontal, documents, music, pictures, videos, downloads);

        double width = Config.sideButtonWidth;
        this.setPrefWidth(width);
        this.minWidth(width);
        this.maxWidth(width);

        this.getStyleClass().add("sidebar");
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("sidebar-styles.css")).toExternalForm());
    }


}
