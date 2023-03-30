package desktop.application.wanderingddl;

import desktop.application.wanderingddl.tools.DragUtil;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ToDoListController extends Application {
    static private ToDoListController toDoListController;
    private final Stage stage = new Stage();
    private ToDoListController(){
        super();
        this.loadFont();
        this.setStage();
    }
    private void loadFont(){}
    public static ToDoListController getInstance(){
        if(toDoListController==null){
            toDoListController = new ToDoListController();
        }
        return toDoListController;
    }

    private void setStage(){
        //给外层套一个透明的以不显示任务栏图标
        Stage transparentStage = new Stage();
        transparentStage.initStyle(StageStyle.UTILITY);
        //将stage的透明度设置为0
        transparentStage.setOpacity(0);
        //stage展示出来，此步骤不可少，缺少此步骤stage还是会存在任务栏
        transparentStage.show();
        stage.initOwner(transparentStage);
        stage.initStyle(StageStyle.TRANSPARENT);//隐藏头标题); //去除窗口样式
        stage.setX(Screen.getPrimary().getBounds().getWidth()-480);
        stage.setY(Screen.getPrimary().getBounds().getHeight()-262);
        stage.setAlwaysOnTop(true);//变成可选项
    }
    //设置完启动入口
    public void newInit(){
        try {
            //获取传参
            this.start(stage);
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public void start(Stage stage) throws IOException {
        Pane pane = new Pane();
        VBox vBox = new VBox();

        //可修改颜色
        pane.setStyle("-fx-background-color: #fffaef;");

        vBox.getChildren().add(getHeader());
        pane.getChildren().add(vBox);
        Scene scene = new Scene(pane, 400, 500);
        scene.setFill(null);

        stage.setScene(scene);
        DragUtil.addDragListener(stage,pane);
        if(!stage.isShowing())
            stage.show();
        MinWindow.getInstance().listen(stage);
        stage.setX(Screen.getPrimary().getBounds().getWidth() - 480);
        stage.setY(Screen.getPrimary().getBounds().getHeight() - 262);

    }
    private HBox getHeader() {
        HBox header = new HBox();
        Image headImg = new Image(getClass().getResource("todoImg/head-1.png").toExternalForm());
        System.out.println(headImg.getHeight());
        System.out.println(headImg.getWidth());

        BackgroundImage bImg = new BackgroundImage(headImg,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(50,50,true,true,true,false));
        header.setPrefWidth(400);
        header.setPrefHeight(142);
        header.setBackground(new Background(bImg));

        return header;
    }
    private HBox[] getListItems() {
        HBox[] items = new HBox[4];
        return items;
    }

}