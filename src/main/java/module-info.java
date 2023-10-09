module org.example {
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.fxml;
    requires okhttp3;

    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires java.xml;

    opens org.example to javafx.fxml;
    exports org.example;
    exports org.example.controller.factory;
    exports org.example.types;
    exports org.example.controller;
    exports org.example.model.services;
    opens org.example.controller to javafx.fxml;
    //opens org.example.model.data to com.fasterxml.jackson.databind;
    exports org.example.model.data;
    opens org.example.model.data to com.fasterxml.jackson.databind;
    opens org.example.model.services to com.fasterxml.jackson.databind;
}
