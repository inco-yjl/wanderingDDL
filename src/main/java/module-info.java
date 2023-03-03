module desktop.application.wanderingddl {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens desktop.application.wanderingddl to javafx.fxml;
    exports desktop.application.wanderingddl;
}