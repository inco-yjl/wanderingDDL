package desktop.application.wanderingddl;

import desktop.application.wanderingddl.tools.DragUtil;
import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;


public class ContentController extends Application {
    static private ContentController contentController;
    private final Stage stage = new Stage();
    private MuYuMode nowMode;                   //木鱼模板：可选1,2
    private Color textColor=Color.BLACK;
    private double width;
    private Font qfont;
    int count;
    private int ifsumMode;                      //选择是否计数总功德，默认显示
    private Label cntlab;                       //功德数标签
    private Label titlab;
    private Button modebtn;
    private ContentController(){
        super();
        this.loadFont();
        this.setStage();
        this.count=0;
        this.ifsumMode=1;
    }
    private void loadFont(){
        qfont = Font.loadFont(getClass().getResource("todoListQfont.ttf").toExternalForm() ,80);
    }
    public static ContentController getInstance(){
        if(contentController==null){
            contentController = new ContentController();
        }
        return contentController;
    }

    /**
     *  功德计数设置更新
     */
    private void updatesumMode(){
        if(ifsumMode==0){
            cntlab.setVisible(false);
            cntlab.setManaged(false);
            titlab.setVisible(false);
            titlab.setManaged(false);
        }else{
            cntlab.setVisible(true);
            cntlab.setManaged(true);
            titlab.setVisible(true);
            titlab.setManaged(true);
        }
    }

    /**
     *  打开功德计数
     */
    private void opencnt(){
        this.ifsumMode=1;
        updatesumMode();
    }

    /**
     *  关闭功德计数
     */
    private void closecnt(){
        this.ifsumMode=0;
        updatesumMode();
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
        stage.setX(Screen.getPrimary().getBounds().getWidth()-450);
        stage.setY(Screen.getPrimary().getBounds().getHeight()-242);
        stage.setAlwaysOnTop(true);//变成可选项
    }
    //设置完启动入口
    public void newInit(){
        try {
            setMode(1);
            setWidth(200);
            this.start(stage);
        }catch (Exception e){
            System.out.println(e);
        }
    }
    //  设置背景色，可透明
    private void setMode(int mode) {
        nowMode = new MuYuMode(mode);
    }
    private  void setWidth(double width) {
        this.width = width;
    }
    private void changemode(int mode){
        mode=(mode+1)%5;
        if(mode==0) mode=5;
        nowMode.updateMode(mode);
    }

    public void start(Stage stage) throws IOException {
        Pane all = new Pane();
        VBox vBox = new VBox();
        titlab=new Label("功德：");
        cntlab=new Label(String.valueOf(count));
        modebtn=new Button("随机木鱼");
        HBox hBox=new HBox();
        hBox.getChildren().addAll(titlab,cntlab);
        //可修改颜色
        all.setStyle("-fx-background-color: "+nowMode.bgColor+";");

        vBox.getChildren().addAll(hBox,modebtn,getHeader());
        modebtn.setOnAction(event -> {
            System.out.println("change"+nowMode.mode);
            changemode(nowMode.mode);
        });
        if(nowMode.bottomImg!=null){
            vBox.getChildren().add(getFooter());
        }
        all.getChildren().addAll(vBox);
        Scene scene = new Scene(all, width+20, width*nowMode.headRatio+150);
        scene.setFill(null);

        stage.setScene(scene);
        DragUtil.addDragListener(stage,all);
        if(!stage.isShowing())
            stage.show();
        MinWindow.getInstance().listen(stage);
    }


    private HBox getHeader() {
        HBox header = new HBox();

        BackgroundImage bImg = new BackgroundImage(nowMode.headImg,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(45,45,true,true,true,false));
        header.setPrefWidth(width);
        header.setPrefHeight(width*nowMode.headRatio);
        header.setBackground(new Background(bImg));
        header.setAlignment(Pos.CENTER);


        header.setOnMouseClicked(event -> {
            System.out.println("plus");
            count++;cntlab.setText(String.valueOf(count));
//            木鱼点击放缩动画
            ScaleTransition st = new ScaleTransition(Duration.millis(100), header);
            st.setByX(0.15f);
            st.setByY(0.15f);
            st.setCycleCount(2);
            st.setAutoReverse(true);
            st.play();

            Label label = new Label("功德+1");
            header.getChildren().add(label);
            lineAnimate(header,label);
//            延迟1000ms删掉功德+1标签
            Timeline timeline = new Timeline();
            timeline.setCycleCount(1);
            timeline.setAutoReverse(false);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    header.getChildren().remove(label);
                }
            });
            timeline.getKeyFrames().clear();
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();
        });
        return header;
    }

    /**
     * 功德+1动画
     * @param header
     * @param label
     */
    public void lineAnimate(HBox header,Label label){


        FadeTransition ft = new FadeTransition(Duration.millis(1000), label);
        ft.setFromValue(1.0f);
        ft.setToValue(0.1f);
        ft.setCycleCount(1);
        ft.setAutoReverse(true);
        ft.play();

        Path path = new Path();
        path.getElements().add (new MoveTo(20f, 0f));
        path.getElements().add (new LineTo(20f, -180f));

        PathTransition pat=new PathTransition();
        pat.setDuration(Duration.millis(1000));
        pat.setNode(label);
        pat.setPath(path);
        pat.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pat.setCycleCount(1);
        pat.setAutoReverse(true);
        pat.play();
//        ParallelTransition pt = new ParallelTransition(label, ft, pat);
//        pt.play();

    }
    private HBox getFooter() {
        HBox footer = new HBox();
        BackgroundImage bImg = new BackgroundImage(nowMode.bottomImg,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(45,45,true,true,true,false));
        footer.setPrefWidth(width);
        footer.setPrefHeight(width*nowMode.headRatio);
        footer.setBackground(new Background(bImg));
        return footer;
    }



}

class MuYuMode {
    Image headImg;
    Image lineImg;
    Image bottomImg=null;
    int count;
    double picWidth;
    double headRatio;
    double lineRatio;
    double bottomRatio;
    int mode;
    String bgColor;
    public MuYuMode(int index) {
        super();
        this.mode = index;
        this.count=0;
        switch (index) {
            case 1 -> bgColor ="rgba(255,255,255,0.3)";
            case 2 -> bgColor = "rgba(255,255,255,0.5)";
            default -> {
            }
        }
        loadImage();
        setSize();
    }
    public void loadImage(){
        headImg = new Image(getClass().getResource("MuyuImg/muyu"+mode+".png").toExternalForm());
        lineImg =  new Image(getClass().getResource("MuyuImg/muyu"+mode+".png").toExternalForm());
        try {
            bottomImg = new Image(getClass().getResource("ContentSrc/todoImg/bottom-"+mode+".png").toExternalForm());
        }catch (Exception e){
            //no bottom
        }
    }
    public void updateMode(int mode){
        this.mode=mode;
        headImg = new Image(getClass().getResource("MuyuImg/muyu"+mode+".png").toExternalForm());
    }
    private void setSize(){
        picWidth = headImg.getWidth();
        headRatio = headImg.getHeight()/picWidth;
        lineRatio = lineImg.getHeight()/picWidth;
        if(bottomImg!=null)bottomRatio=bottomImg.getHeight()/picWidth;
        else bottomRatio = lineRatio;
    }



}
