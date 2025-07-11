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
    requires java.desktop;

    opens org.example.libreriapersonalefx to javafx.fxml;
    exports org.example.libreriapersonalefx;
    exports org.example.libreriapersonalefx.strategy;
    opens org.example.libreriapersonalefx.strategy to javafx.fxml;
}