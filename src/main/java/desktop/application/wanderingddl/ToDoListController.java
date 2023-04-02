package desktop.application.wanderingddl;

import desktop.application.wanderingddl.tools.DragUtil;
import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.LinkedList;

//  TODO:两种模式，完成的移除/完成的仍显示
//  多种模板
public class ToDoListController extends Application {
    static private ToDoListController toDoListController;
    private final Stage stage = new Stage();
    private String bgColor;
    private LinkedList<ToDoItem> toDoItems ;
    private Text[] texts;
    private Rectangle[] deleters;
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
            toDoItems = new LinkedList<>();
            toDoItems.push(new ToDoItem("条目1"));
            toDoItems.push(new ToDoItem("条目2写作业"));
            setColor();
            setTexts();
            this.start(stage);
        }catch (Exception e){
            System.out.println(e);
        }
    }
    //  设置背景色，可透明
    public void setColor() {
        this.bgColor = "#fffaef";
    }

    public void start(Stage stage) throws IOException {
        Pane all = new Pane();
        VBox vBox = new VBox();

        //可修改颜色
        all.setStyle("-fx-background-color: "+this.bgColor+";");

        vBox.getChildren().add(getHeader());
        vBox.getChildren().addAll(getListItems());
        all.getChildren().addAll(vBox);
        Scene scene = new Scene(all, 350, 124+toDoItems.size()*50+20);
        scene.setFill(null);

        stage.setScene(scene);
        DragUtil.addDragListener(stage,all);
        if(!stage.isShowing())
            stage.show();
        MinWindow.getInstance().listen(stage);
        stage.setX(Screen.getPrimary().getBounds().getWidth() - 480);
        stage.setY(Screen.getPrimary().getBounds().getHeight() -400);
        setDeleters();
    }

    //头部图片
    private HBox getHeader() {
        HBox header = new HBox();
        Image headImg = new Image(getClass().getResource("ContentSrc/todoImg/head-1.png").toExternalForm());
        System.out.println(headImg.getHeight());
        System.out.println(headImg.getWidth());
        BackgroundImage bImg = new BackgroundImage(headImg,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(50,50,true,true,true,false));
        header.setPrefWidth(350);
        header.setPrefHeight(124);
        header.setBackground(new Background(bImg));

        return header;
    }
    private void done(int i) {
        Node root= stage.getScene().getRoot();
        Pane all = (Pane) root;
        if(toDoItems.get(i).isChecked()) {
            for(Node node:all.getChildren()) {
                System.out.println(node.getId());
            }
            System.out.println(root.lookup("deleter"+i));
            all.getChildren().remove(root.lookup("#deleter"+i));
            toDoItems.get(i).setUnCheckd();
        }else {
            all.getChildren().add(deleters[i]);
            toDoItems.get(i).setChecked();
        }

    }
    //  删除线，样式的删除线有问题，遂手写
    //  根据checked判断
    private void setDeleters() {
        deleters = new Rectangle[toDoItems.size()];
        for(int i=0;i<toDoItems.size();i++) {
            deleters[i] = new Rectangle();
            deleters[i].setX(50);
            deleters[i].setY(124+i*50+20);
            deleters[i].setWidth(texts[i].getLayoutBounds().getWidth());
            deleters[i].setHeight(2);
            deleters[i].setId("deleter"+i);
        }
        Node root= stage.getScene().getRoot();
        Pane all = (Pane) root;
        for(int i=0,j=0;i<toDoItems.size();i++) {
            if(toDoItems.get(i).isChecked()) {
               all.getChildren().add(deleters[i]);
            }
        }
    }
    //初始化所有代办事件块
    private HBox[] getListItems() {
        HBox[] items = new HBox[toDoItems.size()];
        Image lineImg = new Image(getClass().getResource("ContentSrc/todoImg/line.png").toExternalForm());

        BackgroundImage bImg = new BackgroundImage(lineImg,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(50,50,true,true,true,false));
        for (int i=0;i<texts.length;i++) {
            HBox square = new HBox();
            square.setPrefHeight(42);
            square.setPrefWidth(50);
            items[i] = new HBox(square,texts[i]);
            items[i].setPrefWidth(350);
            items[i].setPrefHeight(50);
            items[i].setBackground(new Background(bImg));
        }
       return items;
    }
    //  初始化所有代办事件文字
    private void setTexts() {
        texts = new Text[toDoItems.size()];
        Font qfont = Font.loadFont(getClass().getResource("todoListQfont.ttf").toExternalForm() ,80);

        for(int i=0;i<texts.length;i++){
            texts[i] = new Text(toDoItems.get(i).getText());
            texts[i].setFont(qfont);
            texts[i].setStyle("-fx-font-size: 30px;-fx-cursor: hand");
            int now_index =i;
            texts[i].setOnMouseClicked(event->{
                done(now_index);
            });
        }
    }

}
class ToDoItem {
    private boolean checked;
    private String item;
    public ToDoItem(String item) {
        super();
        this.item=item;
        this.checked = false;
    }
    public boolean isChecked(){
        return checked;
    }
    public void setChecked() {
        this.checked = true;
    }
    public void setUnCheckd() {
        this.checked = false;
    }
    public String getText() {
        return item;
    }

}