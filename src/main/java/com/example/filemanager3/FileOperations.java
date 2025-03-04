package com.example.filemanager3;

import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
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
}
