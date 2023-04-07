package desktop.application.wanderingddl;

import desktop.application.wanderingddl.tools.DragUtil;
import javafx.application.Application;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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

    private LinkedList<ToDoItem> toDoItems ;
    private Text[] texts;
    private Rectangle[] deleters;
    private Label[] checks;
    private Color textColor=Color.BLACK;
    private ToDoMode nowMode;
    private double width;
    private Font qfont;
    private ToDoListController(){
        super();
        this.loadFont();
        this.setStage();
    }
    private void loadFont(){
        qfont = Font.loadFont(getClass().getResource("todoListQfont.ttf").toExternalForm() ,80);
    }
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

            setMode(2);
            setWidth(300);
            setTexts();
            this.start(stage);
        }catch (Exception e){
            System.out.println(e);
        }
    }
    //  设置背景色，可透明

    private void setMode(int mode) {
        nowMode = new ToDoMode(mode);
    }
    private  void setWidth(double width) {
        this.width = width;
    }


    public void start(Stage stage) throws IOException {
        Pane all = new Pane();
        VBox vBox = new VBox();

        //可修改颜色
        all.setStyle("-fx-background-color: "+nowMode.bgColor+";");

        vBox.getChildren().add(getHeader());
        vBox.getChildren().addAll(getListItems());
        if(nowMode.bottomImg!=null){
            vBox.getChildren().add(getFooter());
        }
        all.getChildren().addAll(vBox);
        Scene scene = new Scene(all, width, width*nowMode.headRatio+toDoItems.size()* nowMode.lineRatio*width+ nowMode.bottomRatio*width);
        scene.setFill(null);

        stage.setScene(scene);
        DragUtil.addDragListener(stage,all);
        if(!stage.isShowing())
            stage.show();
        MinWindow.getInstance().listen(stage);
        setDeleters();
    }

    //头部图片
    private HBox getHeader() {
        HBox header = new HBox();

        BackgroundImage bImg = new BackgroundImage(nowMode.headImg,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(50,50,true,true,true,false));
        header.setPrefWidth(width);
        header.setPrefHeight(width*nowMode.headRatio);
        header.setBackground(new Background(bImg));

        return header;
    }
    private HBox getFooter() {
        HBox footer = new HBox();
        BackgroundImage bImg = new BackgroundImage(nowMode.bottomImg,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(50,50,true,true,true,false));
        footer.setPrefWidth(width);
        footer.setPrefHeight(width*nowMode.headRatio);
        footer.setBackground(new Background(bImg));
        return footer;
    }
    private void done(int i) {
        Node root= stage.getScene().getRoot();
        Pane all = (Pane) root;
        if(toDoItems.get(i).isChecked()) {
            all.getChildren().remove(root.lookup("#deleter"+i));
            toDoItems.get(i).setUnCheckd();
            if(nowMode.mode==2){
                all.getChildren().remove(root.lookup("#check"+i));
            }
        }else {
            if(nowMode.mode==2) {
                all.getChildren().add(checks[i]);
            }
            all.getChildren().add(deleters[i]);
            toDoItems.get(i).setChecked();
        }

    }
    //  删除线，样式的删除线有问题，遂手写
    //  根据checked判断
    private void setDeleters() {
        if(nowMode.mode==2) {
            checks = new Label[toDoItems.size()];
            for(int i=0;i<toDoItems.size();i++) {
                checks[i] = new Label();
                checks[i].setLayoutX(nowMode.lineRatio*width-10);
                checks[i].setLayoutY(nowMode.headRatio*width+i* nowMode.lineRatio*width);
                checks[i].setPrefWidth(15);
                checks[i].setPrefHeight(nowMode.lineRatio*width);
                checks[i].setTextFill(textColor);
                checks[i].setText("√");
                checks[i].setFont(qfont);
                checks[i].setStyle("-fx-font-size: 20px");
                checks[i].setId("check"+i);
            }
        }
        deleters = new Rectangle[toDoItems.size()];
        for(int i=0;i<toDoItems.size();i++) {
            deleters[i] = new Rectangle();
            deleters[i].setX(nowMode.lineRatio*width+10);
            deleters[i].setY(nowMode.headRatio*width+i* nowMode.lineRatio*width+nowMode.lineRatio*width/2.5);
            deleters[i].setWidth(texts[i].getLayoutBounds().getWidth());
            deleters[i].setHeight(2);
            deleters[i].setId("deleter"+i);
            deleters[i].setFill(textColor);
        }
        Node root= stage.getScene().getRoot();
        Pane all = (Pane) root;
        for(int i=0,j=0;i<toDoItems.size();i++) {
            if(toDoItems.get(i).isChecked()) {
               all.getChildren().add(deleters[i]);
               if(nowMode.mode==2) {
                   all.getChildren().add(checks[i]);
               }
            }
        }
    }
    //初始化所有代办事件块
    private HBox[] getListItems() {
        HBox[] items = new HBox[toDoItems.size()];

        BackgroundImage bImg = new BackgroundImage(nowMode.lineImg,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(50,50,true,true,true,false));
        for (int i=0;i<texts.length;i++) {
            HBox square = new HBox();
            square.setPrefHeight(nowMode.lineRatio*width);
            square.setPrefWidth(nowMode.lineRatio*width+10);
            items[i] = new HBox(square,texts[i]);
            items[i].setPrefWidth(width);
            items[i].setPrefHeight(nowMode.lineRatio*width);
            items[i].setBackground(new Background(bImg));
            items[i].setStyle("-fx-cursor: hand;");
            int now_index =i;
            items[i].setOnMouseClicked(event->{
                done(now_index);
            });
        }
       return items;
    }
    //  初始化所有代办事件文字
    private void setTexts() {
        texts = new Text[toDoItems.size()];

        for(int i=0;i<texts.length;i++){
            texts[i] = new Text(toDoItems.get(i).getText());
            texts[i].setFont(qfont);
            if(nowMode.bgColor.equals("transparent")){
                texts[i].setFill(textColor);
                texts[i].setStrokeWidth(0.6);
                texts[i].setStroke(textColor);
            }
            texts[i].setStyle("-fx-font-size: 26px;");
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

class ToDoMode {
    Image headImg;
    Image lineImg;
    Image bottomImg=null;
    double picWidth;
    double headRatio;
    double lineRatio;
    double bottomRatio;
    int mode;
    String bgColor;
    public ToDoMode(int index) {
        super();
        this.mode = index;
        switch (index) {
            case 1 -> bgColor ="#fffaef";
            case 2 -> bgColor = "rgba(255,255,255,0.5)";
            default -> {
            }
        }
        loadImage();
        setSize();
    }
    private void loadImage(){
        headImg = new Image(getClass().getResource("ContentSrc/todoImg/head-"+mode+".png").toExternalForm());
        lineImg =  new Image(getClass().getResource("ContentSrc/todoImg/line-"+mode+".png").toExternalForm());
        try {
            bottomImg = new Image(getClass().getResource("ContentSrc/todoImg/bottom-"+mode+".png").toExternalForm());
        }catch (Exception e){
            //no bottom
        }
    }
    private void setSize(){
        picWidth = headImg.getWidth();
        headRatio = headImg.getHeight()/picWidth;
        lineRatio = lineImg.getHeight()/picWidth;
        if(bottomImg!=null)bottomRatio=bottomImg.getHeight()/picWidth;
        else bottomRatio = lineRatio;
    }



}