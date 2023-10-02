module org.example {
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires okhttp3;

    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;

    opens org.example to javafx.fxml;
    exports org.example;
    exports org.example.controller.factory;
    exports org.example.types;
    exports org.example.controller;
    exports org.example.model.data;
    exports org.example.model.services;
    opens org.example.controller to javafx.fxml;
    opens org.example.model.data to com.fasterxml.jackson.databind;
}
