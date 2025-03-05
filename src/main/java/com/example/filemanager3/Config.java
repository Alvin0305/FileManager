package com.example.filemanager3;

import com.example.filemanager3.filespane.*;
import com.example.filemanager3.topbar.TopBar;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Config {
    private static java.util.Properties properties = new java.util.Properties();
    static {
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            System.out.println("Cannot open properties file");
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static void setProperty(String key, String value) {
        properties.setProperty(key, value);
        try (FileOutputStream fos = new FileOutputStream("config.properties")) {
            properties.store(fos, null);
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    private static View getView() {
        String view = getProperty("view");
        return switch (view) {
            case "TILE" -> View.TILE;
            case "SMALL" -> View.SMALL;
            case "MEDIUM" -> View.MEDIUM;
            default -> View.LARGE;
        };
    }

    private static Sort getSort() {
        String sort = getProperty("sort");
        return switch (sort) {
            case "ALPHABETIC" -> Sort.ALPHABETIC;
            case "REV_ALPHABETIC" -> Sort.REV_ALPHABETIC;
            case "LAST_MODIFIED" -> Sort.LAST_MODIFIED;
            case "FIRST_MODIFIED" -> Sort.FIRST_MODIFIED;
            default -> Sort.SIZE;
        };
    }

    public static double largeTileSize = 200;
    public static double largeIconSize = 100;
    public static double largeTileBorderRadius = 20;
    public static int largeIconViewTextThreshold = 10;

    public static double mediumTileSize = 150;
    public static double mediumIconSize = 75;
    public static double mediumTileBorderRadius = 15;
    public static int mediumIconViewTextThreshold = 8;

    public static double smallTileSize = 100;
    public static double smallIconSize = 50;
    public static double smallTileBorderRadius = 10;
    public static int smallIconViewTextThreshold = 7;

    public static double tileWidth = 200;
    public static double tileHeight = 50;
    public static double tileIconSize = 50;
    public static double tileBorderRadius = 10;
    public static int tileViewTextThreshold = 12;

    public static double descriptionWidth = 250;

    public static boolean showFileExtension = Boolean.parseBoolean(getProperty("show-file-extension"));
    public static boolean confirmBeforeDelete = Boolean.parseBoolean(getProperty("confirm-before-delete"));
    public static boolean clipboardPersistence = Boolean.parseBoolean(getProperty("clipboard-persistence"));

    public static String currentSettings = "general";
    public static double settingsSideBarWidth = 200;

    public static double filesPanePadding = 10;

    public static ArrayList<View> iconViews = new ArrayList<>(List.of(View.LARGE, View.MEDIUM, View.SMALL));

    public static Color backgroundColor = Color.TRANSPARENT;
    public static Color hoverColor = Color.gray(0.5, 0.6);
    public static Color activeColor = Color.gray(0.1, 0.5);

    public static double sideButtonWidth = 200;
    public static double sideButtonHeight = 50;

    public static double spacingAmongTiles = 10;

    public static int topButtonIconSize = 20;
    public static double addressBarHeight = 40;
    public static Color topButtonIconColor = Color.WHITE;

    public static Color fileNameColor = Color.WHITE;
    public static Color sideButtonLabelColor = Color.WHITE;

    public static boolean showHiddenFiles = Boolean.parseBoolean(getProperty("show-hidden-files"));
    public static boolean cut = false;
    public static boolean copy = false;
    public static View view = getView();
    public static Sort sort = getSort();
    public static boolean showDescription = Boolean.parseBoolean(getProperty("show-description"));

    public static double descriptionImageDimensions = 100;
    public static double propertiesImageDimensions = 150;

    public static double propertiesHeight = 650;
    public static double propertiesWidth = 500;

    public static double aboutImageDimensions = 100;
    public static String builtOn = "2025 March";

    public static ArrayList<FileTile> selectedFiles = new ArrayList<>();
    public static File currentFolder = new File(getProperty("root-folder"));
    public static File rootFolder = new File(getProperty("root-folder"));
    public static ArrayList<FileTile> clipBoard = new ArrayList<>();
    public static ArrayList<FileTile> currentFiles = new ArrayList<>();

    public static Files flowPane;
    public static FilesPane scrollPane;
    public static TopBar topBar;
    public static HBox hBox;
    public static Description description;
    public static Scene scene;
    public static VBox vBox;

    public static boolean deepSearch = Boolean.parseBoolean(getProperty("deep-search"));
    public static double searchBarWidth = 380;
    public static double searchbarHeight = 60;
    public static double searchFieldWidth = 200;
    public static double searchFieldHeight = 40;
    public static double searchButtonSize = 40;
    public static int searchIconSize = 20;
    public static File searchLocation = new File("/home/alvin/search");
    public static String wildcardFilter = "substring";
    public static String searchFilter = "file-and-folder";

    public static boolean arrowEventFiltersAdded = false;

    public static ArrayList<File> traversedFiles = new ArrayList<>(Collections.singleton(currentFolder));
    public static int currentFileIndex = 0;

    public static double shortcutWindowWidth = 700;
    public static double shortcutWindowHeight = 500;
    public static double shortcutHGap = 30;
    public static double shortcutVGap = 20;
    public static double shortcutTileHeight = 50;
    public static double shortcutTileWidth = 200;
    public static double shortcutIconWidth = 50;
    public static double shortcutIconHeight = 50;

    public static double helpWindowHeight = 600;
    public static double helpWindowWidth = 820;
    public static Color helpIconColor = Color.VIOLET;
    public static double helpSpacing = 5;
    public static double belowHeading = 10;
    public static double belowSubHeading = 8;
    public static double afterContent = 5;

    public static FileTile draggedFile;

    public static void unSelectAll() {
        List<FileTile> copy = List.copyOf(selectedFiles);
        copy.forEach(FileTile::unselect);
        selectedFiles.clear();
    }

    public static void showDescription() {
        if (hBox.getChildren().size() != 3) {
            if (Config.selectedFiles.isEmpty()) {
                hBox.getChildren().add(new Description(Config.currentFolder));
            } else {
                hBox.getChildren().add(new Description(Config.selectedFiles.getFirst().file));
            }
        } else {
            if (selectedFiles.isEmpty()) {
                System.out.println("Empty");
                hBox.getChildren().set(2, new Description(currentFolder));
            } else {
                System.out.println(selectedFiles.getFirst().file);
                hBox.getChildren().set(2, new Description(Config.selectedFiles.getFirst().file));
            }
        }
    }

    public static void hideDescription() {
        if (hBox.getChildren().size() == 3) {
            hBox.getChildren().removeLast();
        }
    }

    public static void toggleDescription() {
        if (showDescription) {
            hideDescription();
        } else {
            showDescription();
        }
        showDescription = ! showDescription;
    }

    public static void showProperties() {
        System.out.println("Showing properties");
        File file;
        if (selectedFiles.isEmpty()) {
            file = currentFolder;
        } else {
            file = selectedFiles.getFirst().file;
        }
        Scene scene = new Scene(new Properties(file), propertiesWidth, propertiesHeight);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(file.getName());
        stage.setResizable(false);
        stage.show();
    }

    public static double getTileWidth() {
        return switch (view) {
            case SMALL -> smallTileSize;
            case MEDIUM -> mediumTileSize;
            case LARGE -> largeTileSize;
            case TILE -> tileWidth;
        };
    }

    public static int getCurrentIndex() {
        for (FileTile tile: currentFiles) {
            if (selectedFiles.isEmpty()) {
                return -1;
            }
            if (tile.file == selectedFiles.getLast().file) {
                return currentFiles.indexOf(tile);
            }
        }
        return -1;
    }
}
