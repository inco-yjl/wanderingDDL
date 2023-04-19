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
    private final Stage stage = new Stage();
    private MuYuMode nowMode;                   //木鱼模板：可选1,2
    private double width;
    int count;
    private HBox header;
    private int ifsumMode;                      //选择是否计数总功德，默认显示
    private Label cntlab;                       //功德数标签
    private Label titlab;
    private Button modebtn;
    private Button soundbtn;
    private MuYuController(){
        super();
        this.setStage();
        this.count=0;
        this.ifsumMode=1;
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
    //设置完启动入口
    public void newInit(){
        try {
            setMode("line1","2");
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
    }

    /**
     * 随机声音
     */
    private void changesound(){
        String[] soundmodes = new String[]{"1","2","3","4","5"};
        int index = new Random().nextInt(5);
        nowMode.updateSoundMode(soundmodes[index]);
    }
    @Override
    public void start(Stage stage) throws IOException {
        Pane all = new Pane();
        VBox vBox = new VBox();
        titlab=new Label("功德：");
        cntlab=new Label(String.valueOf(count));
        modebtn=new Button("随机木鱼");
        soundbtn=new Button("随机音效");
        HBox hBox=new HBox();
        hBox.getChildren().addAll(titlab,cntlab);

        vBox.getChildren().addAll(hBox,modebtn,soundbtn,getHeader());
        modebtn.setOnAction(event -> {
            System.out.println("change"+nowMode.mode);
            changemode();
        });
        soundbtn.setOnAction(event -> {
            System.out.println("changesound"+nowMode.soundmode);
            changesound();
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
        header = new HBox();

        BackgroundImage bImg = new BackgroundImage(nowMode.headImg,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(45,45,true,true,true,false));
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
            dadada(nowMode.soundmode);
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
    int count;             //敲击次数
    double picWidth;
    double headRatio;
    double lineRatio;
    double bottomRatio;
    String mode;            //木鱼种类
    String soundmode;       //声音种类
    public MuYuMode(String index,String soundmode) {
        super();
        this.mode = index;
        this.soundmode=soundmode;
        this.count=0;
        loadImage();
        setSize();
    }
    public void loadImage(){
        headImg = new Image(getClass().getResource("ContentSrc/MuyuImg/muyu-"+mode+".png").toExternalForm());
        lineImg =  new Image(getClass().getResource("ContentSrc/MuyuImg/muyu-"+mode+".png").toExternalForm());
        try {
            bottomImg = new Image(getClass().getResource("ContentSrc/todoImg/bottom-"+mode+".png").toExternalForm());
        }catch (Exception e){
            //no bottom
        }
    }
    public void updateMode(String mode){
        this.mode=mode;
        loadImage();

    }
    public void updateSoundMode(String mode){
        this.soundmode=soundmode;
    }
    private void setSize(){
        picWidth = headImg.getWidth();
        headRatio = headImg.getHeight()/picWidth;
        lineRatio = lineImg.getHeight()/picWidth;
        if(bottomImg!=null)bottomRatio=bottomImg.getHeight()/picWidth;
        else bottomRatio = lineRatio;
    }



}
