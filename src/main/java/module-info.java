module com.example.filemanager3 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires java.desktop;

    opens com.example.filemanager3 to javafx.fxml;
    exports com.example.filemanager3;
    exports com.example.filemanager3.filespane;
    opens com.example.filemanager3.filespane to javafx.fxml;
    exports com.example.filemanager3.sidebar;
    opens com.example.filemanager3.sidebar to javafx.fxml;
    exports com.example.filemanager3.topbar;
    opens com.example.filemanager3.topbar to javafx.fxml;
    exports com.example.filemanager3.help;
    opens com.example.filemanager3.help to javafx.fxml;
}