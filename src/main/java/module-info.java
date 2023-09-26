module org.example {
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.fxml;
    requires okhttp3;

    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;

    opens org.example to javafx.fxml;
    exports org.example;
    exports org.example.contoller.factory;
    exports org.example.types;
    exports org.example.model;
    exports org.example.contoller;
    exports org.example.model.services;
    opens org.example.contoller to javafx.fxml;
    opens org.example.model to com.fasterxml.jackson.databind;
    exports org.example.model.data;
    opens org.example.model.data to com.fasterxml.jackson.databind;
}
