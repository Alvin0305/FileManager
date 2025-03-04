package com.example.filemanager3.topbar;

import com.example.filemanager3.Config;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class SearchBar extends HBox {
    private File root;

    FontIcon optionsIcon = new FontIcon(FontAwesomeSolid.SLIDERS_H);
    FontIcon searchIcon = new FontIcon(FontAwesomeSolid.ARROW_RIGHT);
    FontIcon closeIcon = new FontIcon(FontAwesomeSolid.TIMES);

    private TextField searchBar = new TextField();

    private Button closeButton = new Button();
    private Button enterButton = new Button();
    private Button optionsButton = new Button();

    private ContextMenu options = new ContextMenu();

    private CheckMenuItem deepSearch = new CheckMenuItem("Deep Search");

    private SeparatorMenuItem separator1 = new SeparatorMenuItem();

    private ToggleGroup wildCardFilter = new ToggleGroup();
    private RadioMenuItem subString = new RadioMenuItem("Sub String");
    private RadioMenuItem startWith = new RadioMenuItem("Starts With");
    private RadioMenuItem endsWith = new RadioMenuItem("Ends With");

    private SeparatorMenuItem separator2 = new SeparatorMenuItem();

    private ToggleGroup searchFilters = new ToggleGroup();
    private RadioMenuItem searchFileOnly = new RadioMenuItem("File Only");
    private RadioMenuItem searchFolderOnly = new RadioMenuItem("Folder Only");

    private Region spacer1 = new Region();
    private Region spacer2 = new Region();

    private ArrayList<File> files = new ArrayList<>();

    public SearchBar(File file) {

        this.setPrefSize(Config.searchBarWidth, Config.searchbarHeight);
        this.setMaxSize(Config.searchBarWidth, Config.searchbarHeight);
        this.setSpacing(10);

        this.root = file;

        setUpButtons();
        assembleOptions();
        addListeners();

        searchBar.setPrefSize(Config.searchFieldWidth, Config.searchFieldHeight);

        this.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        this.setMaxWidth(Double.MAX_VALUE);
        spacer1.setPrefWidth(20);

        this.getChildren().addAll(spacer1, optionsButton, searchBar, enterButton, closeButton, spacer2);
        Config.vBox.getChildren().addFirst(this);

        searchBar.getStyleClass().add("search-bar");
        this.getStyleClass().add("search");
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("search.css")).toExternalForm());
    }

    private void setUpButtons() {
        optionsButton.setPrefSize(Config.searchButtonSize, Config.searchButtonSize);
        enterButton.setPrefSize(Config.searchButtonSize, Config.searchButtonSize);
        closeButton.setPrefSize(Config.searchButtonSize, Config.searchButtonSize);

        optionsIcon.setIconColor(Color.WHITE);
        searchIcon.setIconColor(Color.WHITE);
        closeIcon.setIconColor(Color.WHITE);

        optionsIcon.setIconSize(Config.searchIconSize);
        searchIcon.setIconSize(Config.searchIconSize);
        closeIcon.setIconSize(Config.searchIconSize);

        optionsButton.setGraphic(optionsIcon);
        enterButton.setGraphic(searchIcon);
        closeButton.setGraphic(closeIcon);

        optionsButton.getStyleClass().add("search-button");
        enterButton.getStyleClass().add("search-button");
        closeButton.getStyleClass().add("search-button");
    }

    private void assembleOptions() {
        deepSearch.setSelected(Config.deepSearch);
        subString.setSelected(true);

        searchFileOnly.setToggleGroup(searchFilters);
        searchFolderOnly.setToggleGroup(searchFilters);

        subString.setToggleGroup(wildCardFilter);
        startWith.setToggleGroup(wildCardFilter);
        endsWith.setToggleGroup(wildCardFilter);

        options.getItems().addAll(deepSearch, separator1, subString, startWith, endsWith, separator2, searchFileOnly, searchFolderOnly);
    }

    private void addListeners() {
        deepSearch.selectedProperty().addListener(this::handleDeepSearch);
//        searchFileOnly.selectedProperty().addListener(this::handleFileOnly);
//        searchFolderOnly.selectedProperty().addListener(this::handleFolderOnly);
//        subString.selectedProperty().addListener(this::handleSubString);

        searchBar.setOnKeyPressed(this::handleSearch);
        enterButton.setOnAction(this::handleSearch);

        closeButton.setOnAction(event -> handleClose());
        optionsButton.setOnAction(event -> handleFilterOptions());
    }

    private void handleSearch(ActionEvent event) {
        event.consume();
        search(searchBar.getText());
    }

    private void handleSearch(KeyEvent event) {
        event.consume();
        if (event.getCode().equals(KeyCode.ENTER)) {
            search(searchBar.getText());
        } else if (event.getCode().equals(KeyCode.ESCAPE)) {
            handleClose();
        }
    }

    private void search(String value) {
        try {
            Process process;
            if (subString.isSelected()) {
                value = "*" + value + "*";
            } else if (startWith.isSelected()) {
                value = value + "*";
            } else if (endsWith.isSelected()) {
                value = "*" + value;
            }
            if (deepSearch.isSelected()) {
                if (searchFileOnly.isSelected()) {
                    System.out.println("file only");
                    process = new ProcessBuilder("find", root.getAbsolutePath(), "-type",  "f", "-name", value).start();
                } else if (searchFolderOnly.isSelected()) {
                    System.out.println("folder only");
                    process = new ProcessBuilder("find", root.getAbsolutePath(), "-type",  "d", "-name", value).start();
                } else {
                    System.out.println("files and folders");
                    process = new ProcessBuilder("find", root.getAbsolutePath(), "-name", value).start();
                }
            } else {
                System.out.println("locate");
                process = new ProcessBuilder("locate", value).start();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            files.clear();
            System.out.println("output is: ");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                files.add(new File(line));
            }
            int exitCode = process.waitFor();
            System.out.println("Exit Code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        Config.scrollPane.refresh();
    }

    private void handleDeepSearch(ObservableValue< ? extends Boolean> obs, Boolean oldValue, Boolean newValue) {
        Config.deepSearch = newValue;
        Config.setProperty("deep-search", String.valueOf(newValue));
    }

    private void handleFilterOptions() {
        double screenX = optionsButton.localToScreen(optionsButton.getLayoutBounds().getMaxX(), 0).getX();
        double screenY = optionsButton.localToScreen(0, optionsButton.getLayoutBounds().getMaxY()).getY();
        options.show(optionsButton, screenX, screenY);
    }

    private void handleClose() {
        Config.vBox.getChildren().remove(this);
    }
}
