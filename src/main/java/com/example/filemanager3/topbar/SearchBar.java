package com.example.filemanager3.topbar;

import com.example.filemanager3.Config;
import com.example.filemanager3.filespane.Files;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.util.ArrayList;
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
    private RadioMenuItem searchFileAndFolder = new RadioMenuItem("File And Folder");

    private Region spacer1 = new Region();
    private Region spacer2 = new Region();

    private ArrayList<File> files = new ArrayList<>();

    private boolean flag;

    public SearchBar(File file, boolean flag) {
        this.flag = flag;

        this.setPrefSize(Config.searchBarWidth, Config.searchbarHeight);
        this.setMaxSize(Config.searchBarWidth, Config.searchbarHeight);
        this.setSpacing(10);

        this.root = file;

        switch (Config.wildcardFilter) {
            case "substring" -> subString.setSelected(true);
            case "starts-with" -> startWith.setSelected(true);
            case "ends-with" -> endsWith.setSelected(true);
        }

        switch (Config.searchFilter) {
            case "file-only" -> searchFileOnly.setSelected(true);
            case "folder-only" -> searchFolderOnly.setSelected(true);
            case "file-and-folder" -> searchFileAndFolder.setSelected(true);
        }

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
        searchFileAndFolder.setToggleGroup(searchFilters);

        subString.setToggleGroup(wildCardFilter);
        startWith.setToggleGroup(wildCardFilter);
        endsWith.setToggleGroup(wildCardFilter);

        options.getItems().addAll(deepSearch, separator1, subString, startWith, endsWith, separator2,
                searchFileOnly, searchFolderOnly, searchFileAndFolder);
    }

    private void addListeners() {
        deepSearch.selectedProperty().addListener(this::handleDeepSearch);
        searchFileOnly.setOnAction(event -> Config.searchFilter = "file-only");
        searchFolderOnly.setOnAction(event -> Config.searchFilter = "folder-only");
        searchFileAndFolder.setOnAction(event -> Config.searchFilter = "file-and-folder");
        subString.setOnAction(event -> Config.wildcardFilter = "substring");
        startWith.setOnAction(event -> Config.wildcardFilter = "starts-with");
        endsWith.setOnAction(event -> Config.wildcardFilter = "ends-with");

        enterButton.setOnAction(this::handleSearch);
        addFileNameListener();

        closeButton.setOnAction(event -> handleClose());
        optionsButton.setOnAction(event -> handleFilterOptions());
    }

    private void addFileNameListener() {
        if (flag) {
            searchBar.textProperty().addListener((obs, oldText, newText) -> {
                if (!newText.isEmpty()) {
                    search(newText);
                }
            });
            searchBar.setOnKeyPressed(event -> {
                if (event.getCode().equals(KeyCode.ESCAPE)) {
                    handleClose();
                }
            });
        } else {
            searchBar.setOnKeyPressed(this::handleSearch);
        }
    }

    private void handleSearch(ActionEvent event) {
        event.consume();
        search(searchBar.getText());
    }

    private void handleSearch(KeyEvent event) {
        event.consume();
        if (event.getCode().equals(KeyCode.ENTER)) {
            search(searchBar.getText());
            handleClose();
        } else if (event.getCode().equals(KeyCode.ESCAPE)) {
            handleClose();
        }
    }

    private void search(String value) {
        Config.scrollPane.setContent(new Files(Config.currentFolder, value));
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
