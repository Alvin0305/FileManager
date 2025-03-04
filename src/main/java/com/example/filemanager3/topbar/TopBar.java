package com.example.filemanager3.topbar;

import com.example.filemanager3.*;
import com.example.filemanager3.filespane.FilesPane;
import com.example.filemanager3.help.HelpWindow;
import com.example.filemanager3.settings.SettingsWindow;
import com.example.filemanager3.shortcuts.ShortcutsWindow;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.awt.image.renderable.ContextualRenderedImageFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

public class TopBar extends HBox {
    FontIcon backIcon = new FontIcon(FontAwesomeSolid.ARROW_UP);
    FontIcon leftIcon = new FontIcon(FontAwesomeSolid.ARROW_LEFT);
    FontIcon rightIcon = new FontIcon(FontAwesomeSolid.ARROW_RIGHT);
    FontIcon searchIcon = new FontIcon(FontAwesomeSolid.SEARCH);
    FontIcon menuIcon = new FontIcon(FontAwesomeSolid.BARS);
    FontIcon searchInsideIcon = new FontIcon(FontAwesomeSolid.SEARCH_LOCATION);
    FontIcon viewIcon = new FontIcon(FontAwesomeSolid.TH_LARGE);

    TopButton backButton = new TopButton(backIcon, this::handleBackEvent);
    TopButton searchButton = new TopButton(searchIcon, this::handleSearchEvent);
    TopButton menuButton = new TopButton(menuIcon, this::handleMenuEvent);
    TopButton leftButton = new TopButton(leftIcon, this::handleLeftEvent);
    TopButton rightButton = new TopButton(rightIcon, this::handleRightEvent);
    TopButton searchInsideButton = new TopButton(searchInsideIcon, this::handleSearchInsideEvent);
    TopButton viewButton = new TopButton(viewIcon, this::handleViewEvent);

    TextField addressBar = new TextField();

    ContextMenu menu = new ContextMenu();
    ContextMenu viewMenu = new ContextMenu();

    MenuItem newWindowMenu = new MenuItem("New Window");
    SeparatorMenuItem separator1 = new SeparatorMenuItem();
    MenuItem undoMenu = new MenuItem("Undo");
    MenuItem redoMenu = new MenuItem("Redo");
    SeparatorMenuItem separator2 = new SeparatorMenuItem();
    MenuItem preferencesMenu = new MenuItem("Preferences");
    MenuItem shortcutsMenu = new MenuItem("Keyboard Shortcuts");
    MenuItem helpMenu = new MenuItem("Help");
    MenuItem aboutMenu = new MenuItem("About");

    MenuItem viewLabel = new MenuItem("Views");
    ToggleGroup viewGroup = new ToggleGroup();
    RadioMenuItem smallViewMenu = new RadioMenuItem("Small Icons");
    RadioMenuItem mediumViewMenu = new RadioMenuItem("Medium Icons");
    RadioMenuItem largeViewMenu = new RadioMenuItem("Large Icons");
    RadioMenuItem tileViewMenu = new RadioMenuItem("Tile View");
    SeparatorMenuItem separator3 = new SeparatorMenuItem();
    MenuItem sortLabel = new MenuItem("Sort");
    ToggleGroup sortGroup = new ToggleGroup();
    RadioMenuItem AtoZMenu = new RadioMenuItem("A-Z");
    RadioMenuItem ZtoAMenu = new RadioMenuItem("Z-A");
    RadioMenuItem lastModifiedMenu = new RadioMenuItem("Last Modified");
    RadioMenuItem firstModifiedMenu = new RadioMenuItem("First Modified");
    RadioMenuItem sizeMenu = new RadioMenuItem("Size");
    RadioMenuItem typeMenu = new RadioMenuItem("Type");
    SeparatorMenuItem separator4 = new SeparatorMenuItem();
    CheckMenuItem showHiddenFilesMenu = new CheckMenuItem("Show Hidden Files");
    SeparatorMenuItem separator5 = new SeparatorMenuItem();
    CheckMenuItem showDescriptionMenu = new CheckMenuItem("Show Description");

    public TopBar() {
        Config.topBar = this;
        setAddress();
        addAddressListener();
        assembleMenu();
        assembleViewMenu();

        addressBar.setPrefHeight(Config.addressBarHeight);

        HBox.setHgrow(addressBar, Priority.ALWAYS);
        this.getChildren().addAll(backButton, searchButton, menuButton, leftButton, rightButton, addressBar,
                searchInsideButton, viewButton);
        this.setAlignment(Pos.CENTER);

        this.getChildren().forEach(child -> {
            if (! (child instanceof TextField)) {
                System.out.println("adding margin");
                HBox.setMargin(child, new Insets(5));
            }
        });

        addressBar.getStyleClass().add("address-bar");
        this.getStyleClass().add("topbar");
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("topbar-styles.css")).toExternalForm());
    }

    private void assembleMenu() {
        menu.getItems().addAll(newWindowMenu, separator1, undoMenu, redoMenu,
                separator2, preferencesMenu, shortcutsMenu, helpMenu, aboutMenu);
    }

    private void assembleViewMenu() {
        viewLabel.setDisable(true);
        sortLabel.setDisable(true);

        smallViewMenu.setToggleGroup(viewGroup);
        mediumViewMenu.setToggleGroup(viewGroup);
        largeViewMenu.setToggleGroup(viewGroup);
        tileViewMenu.setToggleGroup(viewGroup);

        AtoZMenu.setToggleGroup(sortGroup);
        ZtoAMenu.setToggleGroup(sortGroup);
        lastModifiedMenu.setToggleGroup(sortGroup);
        firstModifiedMenu.setToggleGroup(sortGroup);
        sizeMenu.setToggleGroup(sortGroup);
        typeMenu.setToggleGroup(sortGroup);

        selectView();
        selectSort();
        showHiddenFilesMenu.setSelected(Config.showHiddenFiles);
        showDescriptionMenu.setSelected(Config.showDescription);

        addViewMenuListeners();
        addMenuListeners();

        viewMenu.getItems().addAll(viewLabel, smallViewMenu, mediumViewMenu, largeViewMenu, tileViewMenu,
                separator3, sortLabel, AtoZMenu, ZtoAMenu, firstModifiedMenu, lastModifiedMenu, sizeMenu, typeMenu,
                separator4, showHiddenFilesMenu, separator5, showDescriptionMenu);
    }

    private void selectView() {
        switch (Config.view) {
            case TILE -> tileViewMenu.setSelected(true);
            case SMALL -> smallViewMenu.setSelected(true);
            case MEDIUM -> mediumViewMenu.setSelected(true);
            default -> largeViewMenu.setSelected(true);
        }
    }

    private void selectSort() {
        switch (Config.sort) {
            case ALPHABETIC -> AtoZMenu.setSelected(true);
            case REV_ALPHABETIC -> ZtoAMenu.setSelected(true);
            case LAST_MODIFIED -> lastModifiedMenu.setSelected(true);
            case FIRST_MODIFIED -> firstModifiedMenu.setSelected(true);
            case SIZE -> sizeMenu.setSelected(true);
            case TYPE -> typeMenu.setSelected(true);
        }
    }

    private void addMenuListeners() {
        newWindowMenu.setOnAction(event -> handleNewWindow());
        aboutMenu.setOnAction(event -> handleAboutMenu());
        shortcutsMenu.setOnAction(event -> handleShowShortcuts());
        helpMenu.setOnAction(event -> handleShowHelp());
        preferencesMenu.setOnAction(event -> handlePreferences());
    }

    private void addViewMenuListeners() {
        addViewListeners();
        addSortListeners();
        showHiddenFilesMenu.setOnAction(event -> handleShowHiddenFiles());
        showDescriptionMenu.setOnAction(event -> handleShowDescription());
    }

    private void addViewListeners() {
        smallViewMenu.setOnAction(event -> handleSmallIcons());
        mediumViewMenu.setOnAction(event -> handleMediumIcons());
        largeViewMenu.setOnAction(event -> handleLargeIcons());
        tileViewMenu.setOnAction(event -> handleTileView());
    }

    private void addSortListeners() {
        AtoZMenu.setOnAction(event -> handleAtoZ());
        ZtoAMenu.setOnAction(event -> handleZtoA());
        lastModifiedMenu.setOnAction(event -> handleLastModified());
        firstModifiedMenu.setOnAction(event -> handleFirstModified());
        sizeMenu.setOnAction(event -> handleSize());
    }

    public void handleBackEvent(ActionEvent event) {
        File parentFile = Config.currentFolder.getParentFile();
        if (parentFile != null && parentFile.exists()) {
            Config.scrollPane.navigateTo(parentFile);
        }
    }

    private void handleSearchEvent(ActionEvent event) {
        System.out.println("searching");
        if (Config.vBox.getChildren().size() != 2) {
            new SearchBar(new File("/"));
            File searchLocation = Config.searchLocation;
            if (searchLocation.exists()) {
                Config.scrollPane.navigateTo(searchLocation);
            } else {
                System.out.println("file not exist");
                try {
                    new ProcessBuilder("mkdir", Config.searchLocation.getAbsolutePath()).start();
                    Config.scrollPane.navigateTo(searchLocation);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void handleMenuEvent(ActionEvent event) {
        double screenX = menuButton.localToScreen(menuButton.getLayoutBounds().getMaxX(), 0).getX();
        double screenY = menuButton.localToScreen(0, menuButton.getLayoutBounds().getMaxY()).getY();
        menu.show(menuButton, screenX, screenY);
    }

    public void handleLeftEvent(ActionEvent event) {
        System.out.println("..");
        System.out.println(Config.traversedFiles);
        System.out.println(Config.currentFileIndex);
        System.out.println("moving back");
        Config.scrollPane.navigateBack();
    }

    public void handleRightEvent(ActionEvent event) {
        System.out.println(".");
        System.out.println(Config.traversedFiles);
        System.out.println(Config.currentFileIndex);
        System.out.println("moving front");
        Config.scrollPane.navigateFront();
    }

    private void handleSearchInsideEvent(ActionEvent event) {}

    private void handleViewEvent(ActionEvent event) {
        System.out.println("Pressed View Button");
        double screenX = viewButton.localToScreen(viewButton.getLayoutBounds().getMaxX(), 0).getX();
        double screenY = viewButton.localToScreen(0, viewButton.getLayoutBounds().getMaxY()).getY();
        viewMenu.show(viewButton, screenX, screenY);
    }

    private void addAddressListener() {
        addressBar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                System.out.println("pressed ENTER");
                File newFile = new File(addressBar.getText());
                if (newFile.exists()) {
                    Config.scrollPane.navigateTo(newFile);
                } else {
                    setAddress();
                }
            }
        });
    }

    private void handleSmallIcons() {
        Config.view = View.SMALL;
        Config.scrollPane.refresh();
    }

    private void handleMediumIcons() {
        Config.view = View.MEDIUM;
        Config.scrollPane.refresh();
    }

    private void handleLargeIcons() {
        Config.view = View.LARGE;
        Config.scrollPane.refresh();
    }

    private void handleTileView() {
        Config.view = View.TILE;
        Config.scrollPane.refresh();
    }

    private void handleAtoZ() {
        Config.sort = Sort.ALPHABETIC;
        Config.scrollPane.refresh();
    }

    private void handleZtoA() {
        Config.sort = Sort.REV_ALPHABETIC;
        Config.scrollPane.refresh();
    }

    private void handleLastModified() {
        Config.sort = Sort.LAST_MODIFIED;
        Config.scrollPane.refresh();
    }

    private void handleFirstModified() {
        Config.sort = Sort.FIRST_MODIFIED;
        Config.scrollPane.refresh();
    }

    private void handleSize() {
        Config.sort = Sort.SIZE;
        Config.scrollPane.refresh();
    }

    private void handleShowHiddenFiles() {
        Config.showHiddenFiles = ! Config.showHiddenFiles;
        showHiddenFilesMenu.setSelected(Config.showHiddenFiles);
        Config.scrollPane.refresh();
    }

    private void handleShowDescription() {
        Config.toggleDescription();
    }

    private void handleNewWindow() {
        Scene scene = new Scene(new FileManager(Config.currentFolder), 1100, 700);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Files!");
        stage.show();
    }

    public void handleAboutMenu() {
        Scene scene = new Scene(new About(), 450, 220);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("About");
        stage.setResizable(false);
        stage.show();
    }

    public void handleShowShortcuts() {
        Scene scene = new Scene(new ShortcutsWindow(), Config.shortcutWindowWidth, Config.shortcutWindowHeight);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Shortcuts");
        stage.setResizable(false);
        stage.show();
    }

    public void handlePreferences() {
        Scene scene = new Scene(new SettingsWindow(), Config.shortcutWindowWidth, Config.shortcutWindowHeight);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Preferences");
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> {
            Config.scrollPane.refresh();
            if (Config.showDescription) {
                Config.showDescription();
            } else {
                Config.hideDescription();
            }
        });
        stage.show();
    }

    private void handleShowHelp() {
        Scene scene = new Scene(new HelpWindow(), Config.helpWindowWidth, Config.helpWindowHeight);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Help");
        stage.setResizable(false);
        stage.show();
    }

    public void setAddress() {
        addressBar.setText(Config.currentFolder.getAbsolutePath());
    }
}
