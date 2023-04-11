package desktop.application.wanderingddl;

import desktop.application.wanderingddl.MinWindow;
import desktop.application.wanderingddl.tools.DragUtil;
import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;


public class MuYuController extends ContentController {
    static private MuYuController muYuController;
    private MuYuMode nowMode;                   //木鱼模板：可选1,2
    private double width;
    int count;
    private HBox header;
    private Label cntlab;                       //功德数标签
    private Label titlab;
    private boolean ifSum=false;     //选择是否计数总功德
    private boolean ifRandom;
    private MuYuController(){
        super();
        this.setStage();
        this.count=0;
    }
    public static MuYuController getInstance(){
        if(muYuController==null){
            muYuController = new MuYuController();
        }
        return muYuController;
    }

    /**
     *  功德计数设置更新
     */
    private void updatesumMode(){
        if(!ifSum){
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
    //设置完启动入口
    public void newInit(boolean ifSum,boolean ifRandom,String mode){
        try {
            this.ifSum = ifSum;
            this.ifRandom = ifRandom;
            setMode(mode,"2");
            setWidth(200);
            this.start(stage);
        }catch (Exception e){
            System.out.println(e);
        }
    }
    //  设置背景色，可透明
    private void setMode(String mode,String soundmode) {
        nowMode = new MuYuMode(mode,soundmode);
    }
    private  void setWidth(double width) {
        this.width = width;
    }

    /**
     * 随机木鱼
     */
    private void changemode(){
        String[] modes = new String[]{"line1","line2","gold","red","wood"};
        int index = new Random().nextInt(5);
        nowMode.updateMode(modes[index]);
        BackgroundImage bImg = new BackgroundImage(nowMode.headImg,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(45,45,true,true,true,false));
        header.setPrefWidth(width);
        header.setPrefHeight(width*nowMode.headRatio);
        header.setBackground(new Background(bImg));
        if(index==1){
            setEffect(header);
        }else header.setEffect(null);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Pane all = new Pane();
        VBox vBox = new VBox();
        initabels();

        HBox hBox=new HBox();
        hBox.getChildren().addAll(titlab,cntlab);
        if(ifRandom)
        vBox.getChildren().addAll(hBox,getModebtn(),getHeader());
        else
            vBox.getChildren().addAll(hBox,getHeader());
        all.getChildren().addAll(vBox);
        all.setStyle("-fx-background-color: transparent;");
        Scene scene = new Scene(all, width+20, width*nowMode.headRatio+150);
        scene.setFill(null);

        stage.setScene(scene);
        DragUtil.addDragListener(stage,all);
        if(!stage.isShowing())
            stage.show();
        MinWindow.getInstance().listen(stage);
        updatesumMode();
    }
    private Button getModebtn(){
        Button modebtn=new Button("随机木鱼");
        modebtn.setStyle("-fx-cursor: hand");
        modebtn.setOnAction(event -> {
            System.out.println("change"+nowMode.mode);
            changemode();
        });
        return modebtn;
    }
    private void initabels(){
        titlab=new Label("功德：");
        cntlab=new Label(String.valueOf(count));
        titlab.setStyle("-fx-background-color: rgba(255,255,255,0.5)");
        cntlab.setStyle("-fx-background-color: rgba(255,255,255,0.5)");
    }
    private HBox getHeader() {
        header = new HBox();

        BackgroundImage bImg = new BackgroundImage(nowMode.headImg,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(45,45,true,true,true,false));
        if(nowMode.mode=="line2")
            setEffect(header);
        else header.setEffect(null);
        header.setPrefWidth(width);
        header.setPrefHeight(width*nowMode.headRatio);
        header.setBackground(new Background(bImg));
        header.setAlignment(Pos.CENTER);
//        功德标签布局
        FlowPane labels=new FlowPane(Orientation.VERTICAL);
        labels.setHgap(0);
        header.getChildren().add(labels);
//        敲击木鱼事件
        header.setOnMouseClicked(event -> {
            System.out.println("plus");
            count++;cntlab.setText(String.valueOf(count));
            System.out.println("X:"+header.getLayoutX());
            System.out.println("y:"+header.getLayoutY());
            System.out.println("xl:"+header.getWidth());;
            System.out.println("yl:"+header.getHeight());
            header.setPrefHeight(200);
            header.setPrefWidth(200);
//            敲击声音
            dadada("2");
//            木鱼点击放缩动画
            ScaleTransition st = new ScaleTransition(Duration.millis(100), header);
            st.setFromX(1);
            st.setFromY(1);
            st.setToX(0.9);
            st.setToY(0.9);
            st.setCycleCount(2);
            st.setAutoReverse(true);
            st.play();

            Label label = new Label("功德+1");
            label.setStyle("-fx-background-color: rgba(255,255,255,0.5)");
            labels.getChildren().add(label);
            label.setLayoutX(40);
            label.setLayoutY(0);
//            label.setPrefWidth(100);
//            header.getChildren().add(label);
            lineAnimate(header,label);
//            label.setTranslateY(100);

//            延迟1000ms删掉功德+1标签
            Timeline timeline = new Timeline();
            timeline.setCycleCount(1);
            timeline.setAutoReverse(false);
            KeyFrame keyFrame = new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    labels.getChildren().remove(label);
                }
            });
            timeline.getKeyFrames().clear();
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();
        });
        return header;
    }

    /**
     * 播放敲木鱼音频
     * @param index 音频种类编号，可选1-5
     */
public void dadada(String index) {
    String filename = "src/main/resources/desktop/application/wanderingddl/ContentSrc/MuyuSound/muyu"+index+".mp3";
    Media hit = new Media(new File(filename).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(hit);
    mediaPlayer.play();

}
    /**
     * 功德+1动画
     * @param header
     * @param label
     */
    public void lineAnimate(HBox header,Label label){


        FadeTransition ft = new FadeTransition(Duration.millis(400), label);
        ft.setFromValue(1.0f);
        ft.setToValue(0.4f);
        ft.setCycleCount(1);
        ft.setAutoReverse(true);
        ft.play();

        Path path = new Path();
        path.getElements().add (new MoveTo(40f, 0f));

        path.getElements().add (new LineTo(40f, -150f));
        PathTransition pat=new PathTransition();
        pat.setDuration(Duration.millis(1200));
        pat.setNode(label);
        pat.setPath(path);
//        pat.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pat.setCycleCount(1);
        pat.setAutoReverse(true);
        pat.play();
//        ParallelTransition pt = new ParallelTransition(label, ft, pat);
//        pt.play();

    }

    private void setEffect(HBox muyu) {
        DropShadow ds = new DropShadow();
        ds.setOffsetY(0f);
        ds.setColor(Color.valueOf("#aaaaaa"));
        muyu.setEffect(ds);
    }


}

class MuYuMode {
    Image headImg;
    int count;             //敲击次数
    double headRatio;
    String mode;            //木鱼种类
    String soundmode;       //声音种类
    public MuYuMode(String index,String soundmode) {
        super();
        this.mode = index;
        this.soundmode=soundmode;
        this.count=0;
        loadImage();
    }
    public void loadImage(){
        headImg = new Image(getClass().getResource("ContentSrc/MuyuImg/muyu-"+mode+".png").toExternalForm());
        headRatio = headImg.getHeight()/headImg.getWidth();
    }
    public void updateMode(String mode){
        this.mode=mode;
        loadImage();

    }




}
