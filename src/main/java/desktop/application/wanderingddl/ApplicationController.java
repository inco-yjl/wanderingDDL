package desktop.application.wanderingddl;

import desktop.application.wanderingddl.navigation.PageFactory;
import desktop.application.wanderingddl.tools.DragUtil;

import desktop.application.wanderingddl.tools.FontLoader;
import javafx.application.Application;
import javafx.css.Style;
import javafx.css.Stylesheet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ApplicationController extends Application {
    static Stage stage;
    static BorderPane window;
    public Button wanderingPage;

    public Pane wanderingPageContent;


    @Override
    public void start(Stage stage) throws IOException {
        ApplicationController.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("Config/Window.fxml"));
        HBox windowMenu = (HBox) FXMLLoader.load(getClass().getResource("Config/Menubar.fxml"));
        VBox mainContent = (VBox) FXMLLoader.load(getClass().getResource("Config/Content.fxml"));
        this.wanderingPageContent = (Pane) FXMLLoader.load(getClass().getResource("MainContent/WanderingPage.fxml"));
        initWanderingSelect();
        Node[] pages = {wanderingPageContent};

        createScene(root, windowMenu, mainContent);
        PageFactory.setPages(pages);
    }

    public void initWanderingSelect() {
        MenuButton select_ddl = new MenuButton();
        MenuItem hours = new MenuItem("小时");
        MenuItem days = new MenuItem("天");
        MenuItem weeks = new MenuItem("星期");
        MenuItem months = new MenuItem("月");
        select_ddl.getItems().addAll(hours, days, weeks, months);
        select_ddl.setId("3");
        select_ddl.setLayoutX(264);
        select_ddl.setLayoutY(149);
        select_ddl.setPrefHeight(38);
        select_ddl.setPrefWidth(120);
        select_ddl.setBackground(new Background(new BackgroundFill(Color.valueOf("white"),null,null)));
        select_ddl.setStyle("-fx-border-color: rgba(112,128,144,0.6); -fx-border-radius: 0;\n" +
                "-fx-border-width: 1 1 1 1;");
        select_ddl.setCursor(Cursor.HAND);
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                select_ddl.setText(((MenuItem) e.getSource()).getText());
            }
        };
        hours.setOnAction(event);
        days.setOnAction(event);
        weeks.setOnAction(event);
        months.setOnAction(event);
        this.wanderingPageContent.getChildren().add(select_ddl);
    }

    public void createScene(Parent root, HBox windowMenu, VBox mainContent) {
        BorderPane window = (BorderPane) root;
        ApplicationController.window = window;
        window.setTop(windowMenu);
        window.setRight(mainContent);
        Scene scene = new Scene(root, 700, 450);
        stage.setResizable(false); //固定大小
        stage.initStyle(StageStyle.TRANSPARENT);//隐藏头标题); //去除窗口样式
        scene.setFill(null);
        stage.setScene(scene);
        DragUtil.addDragListener(stage, window); //窗口拖拽
        stage.show();
}

    @FXML
    protected void closeWindow() {
        stage.hide();
    }

    @FXML
    protected void minizeWindow() {
        stage.setIconified(true);
    }

    @FXML
    protected void ToWanderingPage() {
        //选中
        wanderingPage.setStyle("-fx-background-color: #7c9fcc;");
        routePage(0);
        System.out.println("wanderingDDL");
    }
    @FXML
    private void ToDoListPage() {
        routePage(1);
        System.out.println("todoList");
    }
    @FXML
    protected void openWanderingUI() {
        String[] sentences = new String[5];
        sentences[0] = ((TextField) window.getRight().lookup("#" + Integer.toString(0))).getText();//某某作业
        sentences[1] = ((TextField) window.getRight().lookup("#" + Integer.toString(1))).getText();//还有
        sentences[2] = ((Spinner) window.getRight().lookup("#" + Integer.toString(2))).getValue().toString();//x
        sentences[3] = ((MenuButton) window.getRight().lookup("#" + Integer.toString(3))).getText();//天
        String strings_3 = "";
        switch (sentences[3]) {
            case "小时":
                strings_3 = " hours";
                break;
            case "天":
                strings_3 = " days";
                break;
            case "星期":
                strings_3 = " weeks";
                break;
            case "月":
                strings_3 = " months";
                break;
            default:
                break;
        }
        sentences[4] = "deadline comes in " + sentences[2] + strings_3 + ", wait for it patiently";
        WanderingController.getInstance().newInit(sentences);
    }

    private void routePage(int index) {
        Node node = PageFactory.createPageService(index);
        window.setRight(node);
    }

    public static void main(String[] args) {
        launch();
    }
}
