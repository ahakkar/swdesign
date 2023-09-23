module org.example {
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.fxml;
    requires okhttp3;

    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;

    opens org.example to javafx.fxml;
    exports org.example;
    exports org.example.factory;
    exports org.example.types;
    exports org.example.Models;
    exports org.example.Controllers;
    exports org.example.Services;
    opens org.example.Controllers to javafx.fxml;
    opens org.example.Models to com.fasterxml.jackson.databind;
}
