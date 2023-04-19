module desktop.application.wanderingddl {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires com.alibaba.fastjson2;
    requires javafx.media;
    requires java.scripting;
    requires org.openjdk.nashorn;
    opens desktop.application.wanderingddl to javafx.fxml;
    exports desktop.application.wanderingddl;
    exports desktop.application.wanderingddl.tools;
    opens desktop.application.wanderingddl.tools to javafx.fxml;


}