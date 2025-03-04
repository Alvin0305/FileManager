package com.example.filemanager3;

import com.example.filemanager3.filespane.Description;
import com.example.filemanager3.filespane.FilesPane;
import com.example.filemanager3.sidebar.SideBar;
import com.example.filemanager3.topbar.TopBar;
import javafx.scene.layout.*;

import java.io.File;

public class FileManager extends BorderPane {

    File file;
    TopBar topBar = new TopBar();

    public FileManager() {
        this(new File("/home/alvin/Drive/Me"));
    }

    public FileManager(File file) {
        this.file = file;

        HBox center = new HBox();
        SideBar sideBar = new SideBar();
        VBox filePaneContainer = new VBox();
        FilesPane filesPane = new FilesPane(file);
        VBox.setVgrow(filesPane, Priority.ALWAYS);
        filePaneContainer.getChildren().add(filesPane);

        Config.hBox = center;
        Config.vBox = filePaneContainer;

        HBox.setHgrow(filePaneContainer, Priority.ALWAYS);

        center.getChildren().addAll(sideBar, filePaneContainer);
        if (Config.showDescription) {
            Description description = new Description();
            center.getChildren().add(description);
        }
        this.setCenter(center);
        this.setTop(topBar);
    }
}
