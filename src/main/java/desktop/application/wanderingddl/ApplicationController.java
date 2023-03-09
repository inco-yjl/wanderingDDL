package desktop.application.wanderingddl;

import desktop.application.wanderingddl.navigation.PageFactory;
import desktop.application.wanderingddl.tools.DragUtil;

import desktop.application.wanderingddl.tools.FontLoader;
import javafx.application.Application;
import javafx.fxml.FXML;

import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
    public class ApplicationController extends Application {
        static Stage stage;
        static BorderPane window;
        public Button wanderingPage;


        @Override
    public void start(Stage stage) throws IOException {
        ApplicationController.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("Config/Window.fxml"));
        HBox windowMenu = (HBox) FXMLLoader.load(getClass().getResource("Config/Menubar.fxml"));
        VBox mainContent = (VBox) FXMLLoader.load(getClass().getResource("Config/Content.fxml"));
        Pane  wanderingPageContent = (Pane) FXMLLoader.load(getClass().getResource("MainContent/WanderingPage.fxml"));

        Node[] pages = {wanderingPageContent};

        createScene(root,windowMenu,mainContent);
        PageFactory.setPages(pages);
    }
    public void createScene(Parent root, HBox windowMenu, VBox mainContent) {
        BorderPane window = (BorderPane)root;
        ApplicationController.window = window;
        window.setTop(windowMenu);
        window.setRight(mainContent);
        Scene scene = new Scene(root, 700, 800);
        stage.setResizable(false); //固定大小
        stage.initStyle(StageStyle.TRANSPARENT);//隐藏头标题); //去除窗口样式
        scene.setFill(null);
        stage.setScene(scene);
        DragUtil.addDragListener(stage,root); //窗口拖拽
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
    @FXML
    protected void ToWanderingPage(){
        wanderingPage.setStyle("-fx-border-color: #a8ddb5;");
        routePage(0);
        //此处直接打开了第一个
        openWanderingUI();
    }
    protected void openWanderingUI(){
            WanderingController wanderingController = new WanderingController();
        try {
            wanderingController.start(new Stage());
        }catch (IOException e){
            //
        }
    }
    private void routePage(int index) {
            Node node = PageFactory.createPageService(index);
            window.setRight(node);
    }
    public static void main(String[] args) {
        launch();
    }
}
