module com.example.best_local_area {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;


    opens com.example.best_local_area to javafx.fxml;
    exports com.example.best_local_area;
    exports controllers;
    opens controllers to javafx.fxml;
    exports repository;
    opens repository to javafx.fxml;
    exports model;
    opens model to javafx.fxml;
    exports additional;
    opens additional to javafx.fxml;
}