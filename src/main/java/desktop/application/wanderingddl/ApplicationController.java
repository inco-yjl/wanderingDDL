package desktop.application.wanderingddl;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Node;
import desktop.application.wanderingddl.tools.DragUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

public class ApplicationController extends Application {
    static Stage stage;
    @FXML
    private Node windowMenu;
    @Override
    public void start(Stage stage) throws IOException {
        ApplicationController.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(ApplicationController.class.getResource("Config/wanderingDDL.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 750);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        DragUtil.addDragListener(stage,scene.lookup("#windowMenu"));
        stage.show();
    }

    @FXML
    protected void closeWindow() {
        stage.close();
    }
    @FXML
    protected void minizeWindow() {
        stage.setIconified(true);
    }
    public static void main(String[] args) {
        launch();
    }
}
