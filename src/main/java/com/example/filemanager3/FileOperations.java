package com.example.filemanager3;

import javafx.scene.image.Image;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class FileOperations {
    public static Image getImage(File file) {
        HashSet<String> musicFiles = new HashSet<>(Set.of("mp3"));
        HashSet<String> videoFiles = new HashSet<>(Set.of("mp4", "mkv", "avi"));
        HashSet<String> codeFiles = new HashSet<>(Set.of("c", "cpp", "kt", "py", "js", "dart"));

        int lastIndex = file.getName().indexOf(".");
        if (lastIndex == -1) return new Image(Objects.requireNonNull(Config.class.getResourceAsStream("folder.png")));

        String extension = file.getName().substring(lastIndex + 1);

        if (file.getName().startsWith(".")) {
            return new Image(Objects.requireNonNull(Config.class.getResourceAsStream("key.png")));
        } else if (musicFiles.contains(extension)) {
            return new Image(Objects.requireNonNull(Config.class.getResourceAsStream("music.png")));
        } else if (videoFiles.contains(extension)) {
            return new Image(Objects.requireNonNull(Config.class.getResourceAsStream("video.png")));
        } else if (codeFiles.contains(extension)) {
            return new Image(Objects.requireNonNull(Config.class.getResourceAsStream("code.png")));
        }

        return switch (extension) {
            case "txt" -> new Image(Objects.requireNonNull(Config.class.getResourceAsStream("text_file.png")));
            case "html" -> new Image(Objects.requireNonNull(Config.class.getResourceAsStream("html.png")));
            case "css" -> new Image(Objects.requireNonNull(Config.class.getResourceAsStream("css.png")));
            case "java" -> new Image(Objects.requireNonNull(Config.class.getResourceAsStream("java.png")));
            default -> new Image(Objects.requireNonNull(Config.class.getResourceAsStream("folder.png")));
        };

    }

    public static int countChildren(File file) {
        if (file.isFile()) {
            return 0;
        } else {
            File[] files = file.listFiles();
            if (files != null) return files.length;
            else return 0;
        }
    }

    public static long sizeOf(File folder) {
        long size = 0;
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file: files) {
                if (file.isFile()) {
                    size += file.length();
                } else {
                    size += sizeOf(file);
                }
            }
        }
        return size;
    }

    public static String getReadableSize(File folder) {
        long size = sizeOf(folder);
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int i = 0;
        while (size >= 1024) {
            size /= 1024;
            i++;
        }
        return size + units[i];
    }

    public static BasicFileAttributes getAttributes(File file) {
        Path filePath = Path.of(file.toURI());
        try {
            return java.nio.file.Files.readAttributes(filePath, BasicFileAttributes.class);
        } catch (IOException e) {
            System.out.println("Error");
            return null;
        }
    }

    public static String getAccessedTime(File file) {
        String dateTime = Objects.requireNonNull(getAttributes(file)).lastAccessTime().toString();
        String date = dateTime.substring(0, 10);
        String time = dateTime.substring(11, 16);
        return date + " " + time;
    }

    public static String getCreatedTime(File file) {
        String dateTime = Objects.requireNonNull(getAttributes(file)).creationTime().toString();
        String date = dateTime.substring(0, 10);
        String time = dateTime.substring(11, 16);
        return date + " " + time;
    }

    public static String getModifiedTime(File file) {
        String dateTime = Objects.requireNonNull(getAttributes(file)).lastModifiedTime().toString();
        String date = dateTime.substring(0, 10);
        String time = dateTime.substring(11, 16);
        return date + " " + time;
    }

    public static String getPermissions(File file) {
        StringBuilder permission = new StringBuilder();
        if (file.canRead()) {
            permission.append("Read ");
        }
        if (file.canWrite()) {
            permission.append("Write ");
        }
        if (file.canExecute()) {
            permission.append("Execute ");
        }
        return permission.toString();
    }

    public static ArrayList<File> search(File root, String fileName) {
        String wildcardFilter = Config.wildcardFilter;
        String searchFilter = Config.searchFilter;
        boolean deepSearch = Config.deepSearch;
        ArrayList<File> files = new ArrayList<>();
        try {
            Process process;
            switch (wildcardFilter) {
                case "substring" -> fileName = "*" + fileName + "*";
                case "starts-with" -> fileName = fileName + "*";
                case "ends-with" -> fileName = "*" + fileName;
            }
            if (deepSearch) {
                if (searchFilter.equals("file-only")) {
                    System.out.println("file only");
                    process = new ProcessBuilder("find", root.getAbsolutePath(), "-type",  "f", "-name", fileName).start();
                } else if (searchFilter.equals("folder-only")) {
                    System.out.println("folder only");
                    process = new ProcessBuilder("find", root.getAbsolutePath(), "-type",  "d", "-name", fileName).start();
                } else {
                    System.out.println("files and folders");
                    process = new ProcessBuilder("find", root.getAbsolutePath(), "-name", fileName).start();
                }
            } else {
                System.out.println("locate");
                process = new ProcessBuilder("locate", fileName).start();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
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

        return files;
    }
}
