module fi.nordicwatt {
    exports fi.nordicwatt;
    
    requires transitive javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.fxml;
    requires okhttp3;

    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires java.xml;

    opens fi.nordicwatt to javafx.fxml;
    opens fi.nordicwatt.controller to javafx.fxml;    
    opens fi.nordicwatt.model.data to com.fasterxml.jackson.databind;
    opens fi.nordicwatt.model.service to com.fasterxml.jackson.databind, com.fasterxml.jackson.datatype.jsr310;
    opens fi.nordicwatt.model.datamodel to com.fasterxml.jackson.databind;

    exports fi.nordicwatt.utils to com.fasterxml.jackson.databind;
    exports fi.nordicwatt.model.data;
    exports fi.nordicwatt.model.datamodel;
    exports fi.nordicwatt.controller.factory;
    exports fi.nordicwatt.types;
    exports fi.nordicwatt.controller;
    exports fi.nordicwatt.model.service;
    exports fi.nordicwatt.model.session;
}
