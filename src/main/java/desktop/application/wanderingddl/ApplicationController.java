package desktop.application.wanderingddl;

import desktop.application.wanderingddl.navigation.PageFactory;
import desktop.application.wanderingddl.tools.*;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.*;
import java.net.URL;

import javafx.scene.media.*;


public class ApplicationController extends Application {
    static Stage stage;
    static BorderPane window;
    public Button wanderingPage;
    public Button toDoListPage;
    public Button answerBookPage;
    private boolean[] newRoute = new boolean[]{true, true, true};
    @Override
    public void start(Stage stage) throws IOException {
        stage.getIcons().add(new Image(getClass().getResourceAsStream("Config/source/icon.png")));
        stage.setTitle("WanderingDDL");
        ApplicationController.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("Config/Window.fxml"));
        HBox windowMenu = (HBox) FXMLLoader.load(getClass().getResource("Config/Menubar.fxml"));

        VBox mainContent = (VBox) FXMLLoader.load(getClass().getResource("Config/Content.fxml"));
        Pane wanderingPageContent = (Pane) FXMLLoader.load(getClass().getResource("MainContent/WanderingPage.fxml"));
        Pane toDoListPageContent = new ToDoPageContent().getToDoPageContent();
        Pane answerBookPageContent = (Pane) FXMLLoader.load(getClass().getResource("MainContent/AnswerBookPage.fxml"));
//        Pane live2dPageContent=(Pane) FXMLLoader.load(getClass().getResource("MainContent/Live2dPage.fxml"));
        Node[] pages = {wanderingPageContent, toDoListPageContent,answerBookPageContent};
        PageFactory.setPages(pages);

        initWanderingSelect();

        createScene(root, windowMenu, mainContent);

    }

    public void initWanderingSelect() {
        Spinner<Integer> spinner = new Spinner<>(0, 999, 0, 1);
        spinner.setEditable(true);
        spinner.setCache(true);
        spinner.setId("2");
        spinner.setLayoutX(144);
        spinner.setLayoutY(149);
        spinner.setPrefHeight(38);
        spinner.setPrefWidth(116);
        spinner.getStyleClass().add("spinner");
        spinner.getStylesheets().addAll((getClass().getResource("MainContent/style/start.css").toExternalForm()));

        //  时间额度钮
        MenuButton select_ddl = new MenuButton();

        //  没什么意义，拉长一点
        MenuItem hours = new MenuItem("小时              ");
        MenuItem days = new MenuItem("天");
        MenuItem weeks = new MenuItem("星期");
        MenuItem months = new MenuItem("月");
        select_ddl.getItems().addAll(hours, days, weeks, months);
        select_ddl.setId("3");
        select_ddl.setLayoutX(264);
        select_ddl.setLayoutY(149);
        select_ddl.setPrefHeight(38);
        select_ddl.setPrefWidth(120);
        select_ddl.setBackground(new Background(new BackgroundFill(Color.valueOf("white"), null, null)));
        select_ddl.setStyle("-fx-border-color: rgba(112,128,144,0.6); -fx-border-radius: 0;\n" +
                "-fx-border-width: 1 1 1 1;     -fx-text-fill: rgba(47,79,79,1);\n" +
                "    -fx-font-weight: bold;");
        select_ddl.setCursor(Cursor.HAND);

        TextField textField_1 = new TextField("Deadline comes in");
        textField_1.setId("4");
        textField_1.setLayoutX(52);
        textField_1.setLayoutY(200);
        textField_1.setPrefHeight(25);
        textField_1.setPrefWidth(140);
        textField_1.getStyleClass().add("start-textField");
        textField_1.getStylesheets().addAll((getClass().getResource("MainContent/style/start.css").toExternalForm()));

        Label label_x = new Label("x");
        label_x.setId("5");
        label_x.setLayoutX(203);
        label_x.setLayoutY(205);
        label_x.getStyleClass().add("hint");
        label_x.getStylesheets().addAll((getClass().getResource("MainContent/style/start.css").toExternalForm()));
        label_x.setTextFill(Paint.valueOf("#092053"));
        //label_x.setFont(Font.loadFont("Consolas", 13.0));

        Label label_days = new Label("days");
        label_days.setId("6");
        label_days.setLayoutX(230);
        label_days.setLayoutY(205);
        label_days.getStyleClass().add("hint");
        label_days.getStylesheets().addAll((getClass().getResource("MainContent/style/start.css").toExternalForm()));
        label_days.setTextFill(Paint.valueOf("#092053"));
        //label_x.setFont(Font.loadFont("Consolas", 13.0));

        TextField textField_2 = new TextField(",wait for it patiently");
        textField_2.setId("7");
        textField_2.setLayoutX(280);
        textField_2.setLayoutY(200);
        textField_2.setPrefHeight(25);
        textField_2.setPrefWidth(150);
        textField_2.getStyleClass().add("start-textField");
        textField_2.getStylesheets().addAll((getClass().getResource("MainContent/style/start.css").toExternalForm()));

        spinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                label_x.setText(newValue.toString());
            }
        });

        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                select_ddl.setText(((MenuItem) e.getSource()).getText());
                label_days.setText(Chi2Eng(((MenuItem) e.getSource()).getText()));
            }
        };

        hours.setOnAction(event);
        days.setOnAction(event);
        weeks.setOnAction(event);
        months.setOnAction(event);

        PageFactory.addNode(0,spinner,select_ddl,textField_1,label_x,label_days,textField_2);
    }

    private String connectSentences(String[] sentences) {
        String result = new String("");
        for (int i = 0; i < sentences.length; i++)
            result += sentences[i];
        return result;
    }

    public void createScene(Parent root, HBox windowMenu, VBox mainContent) {

        BorderPane window = (BorderPane) root;
        ApplicationController.window = window;
        window.setTop(windowMenu);
        window.setRight(mainContent);
        Scene scene = new Scene(root, 700, 600);
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

    private void setButtonPressed(int index){
        ButtonStyleController.setPressed(index,wanderingPage,toDoListPage,answerBookPage);
    }
    @FXML
    protected void ToWanderingPage() {
        routePage(0);
    }
    @FXML
    protected void ToAnswerBookPage() {
        routePage(2);
    }

    @FXML
    private void ToDoListPage() {
        routePage(1);
    }
    @FXML
    private void MuyuPage(){

        System.out.println("tomuyu");
//        muyuPage.setStyle("-fx-background-color: #7c9fcc;");
//        routePage(2);
        MuYuController.getInstance().newInit();
        System.out.println("tomuyu2");
    }
    @FXML
    private void Live2dPage(){
        System.out.println("tolive2d");
        Live2dController.getInstance().newInit();
        System.out.println("go to live2d");
    }
    @FXML
    protected void openWanderingUI() {
        String[] sentences = new String[5];
        sentences[0] = ((TextField) window.getRight().lookup("#0")).getText();//某某作业
        sentences[1] = ((TextField) window.getRight().lookup("#1")).getText();//还有
        sentences[2] = ((Spinner) window.getRight().lookup("#2")).getValue().toString();//x
        sentences[3] = ((MenuButton) window.getRight().lookup("#3")).getText();//天
        String strings_3 = Chi2Eng(sentences[3]);
        String strings_4 = ((TextField) window.getRight().lookup("#4")).getText();
        String strings_5 = ((TextField) window.getRight().lookup("#7")).getText();
        sentences[4] = strings_4 + " " + sentences[2] + strings_3 + " " + strings_5;
        WanderingController.getInstance().newInit(sentences);
        SaverAndLoader.tool.saveWanderingInput(new String[]{sentences[0],sentences[1],sentences[2],sentences[3],strings_4,strings_5});
    }

    @FXML
    protected void openWanderingUI_with_ji() {
        openWanderingUI();
        play_ji();
    }

    public void play_ji() {
        URL url = getClass().getResource("ji.mp3");
        Media media = new Media(url.toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }
    private String Chi2Eng(String sentences) {
        switch (sentences) {
            case "小时              ":
                return " hours";
            case "天":
                return " days";
            case "星期":
                return " weeks";
            case "月":
                return " months";
            default:
                break;
        }
        return sentences;
    }

    private void routePage(int index) {
        setButtonPressed(index);
        Node node = PageFactory.createPageService(index);
        if(newRoute[index]) {
            SaverAndLoader.tool.loadPage(node,index);
        }
        window.setRight(node);
    }
    public static void main(String[] args) {
        launch();
    }
}
