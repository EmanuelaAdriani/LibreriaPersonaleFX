module org.example.libreriapersonalefx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.fasterxml.jackson.databind;

    opens org.example.libreriapersonalefx to javafx.fxml;
    exports org.example.libreriapersonalefx;
    exports org.example.libreriapersonalefx.strategy;
    opens org.example.libreriapersonalefx.strategy to javafx.fxml;
    exports org.example.libreriapersonalefx.controller;
    opens org.example.libreriapersonalefx.controller to javafx.fxml;
    exports org.example.libreriapersonalefx.observer;
    opens org.example.libreriapersonalefx.observer to javafx.fxml;
    exports org.example.libreriapersonalefx.singleton;
    opens org.example.libreriapersonalefx.singleton to javafx.fxml;
    exports org.example.libreriapersonalefx.entity;
    opens org.example.libreriapersonalefx.entity to javafx.fxml;
}