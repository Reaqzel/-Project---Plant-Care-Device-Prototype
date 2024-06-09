module com.project {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.media;
    
    opens com.project to javafx.fxml;
    exports com.project;
}
