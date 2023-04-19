package desktop.application.wanderingddl;


import desktop.application.wanderingddl.MinWindow;
import desktop.application.wanderingddl.tools.DragUtil;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

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

import javax.script.ScriptException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;

import java.util.LinkedList;
import java.util.Random;


public class Live2dController extends ContentController {
    static private Live2dController live2dController;
    private final Stage stage = new Stage();
//    private MuYuMode nowMode;                   //木鱼模板：可选1,2
    private double width;
    int count;
    private HBox header;
    private int ifsumMode;                      //选择是否计数总功德，默认显示

    private Live2dController(){
        super();
        this.setStage();
    }
    public static Live2dController getInstance(){
        if(live2dController==null){
            live2dController = new Live2dController();
        }
        return live2dController;
    }


    //设置完启动入口
    public void newInit(){
        try {
            setWidth(200);
            this.start(stage);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    private  void setWidth(double width) {
        this.width = width;
    }


    @Override
    public void start(Stage stage) throws IOException {
        Pane all = new Pane();
        VBox vBox = new VBox();
//        titlab=new Label("功德：");
//        cntlab=new Label(String.valueOf(count));
//        modebtn=new Button("随机木鱼");
//        soundbtn=new Button("随机音效");
//        HBox hBox=new HBox();
//        hBox.getChildren().addAll(titlab,cntlab);


        all.getChildren().addAll(vBox);
        Scene scene = new Scene(all, width + 20, width);
        scene.setFill(null);

        stage.setScene(scene);
        DragUtil.addDragListener(stage, all);
        if (!stage.isShowing())
            stage.show();
        MinWindow.getInstance().listen(stage);

        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine nashorn = scriptEngineManager.getEngineByName("nashorn");
        try {
            nashorn.eval(new FileReader("src/main/resources/desktop/application/wanderingddl/Config/load.js"));

        } catch (ScriptException e) {
            e.printStackTrace();
        }

    }
}

//class MuYuMode {
//    Image headImg;
//    Image lineImg;
//    Image bottomImg=null;
//    int count;             //敲击次数
//    double picWidth;
//    double headRatio;
//    double lineRatio;
//    double bottomRatio;
//    String mode;            //木鱼种类
//    String soundmode;       //声音种类
//    public MuYuMode(String index,String soundmode) {
//        super();
//        this.mode = index;
//        this.soundmode=soundmode;
//        this.count=0;
//        loadImage();
//        setSize();
//    }
//    public void loadImage(){
//        headImg = new Image(getClass().getResource("ContentSrc/MuyuImg/muyu-"+mode+".png").toExternalForm());
//        lineImg =  new Image(getClass().getResource("ContentSrc/MuyuImg/muyu-"+mode+".png").toExternalForm());
//        try {
//            bottomImg = new Image(getClass().getResource("ContentSrc/todoImg/bottom-"+mode+".png").toExternalForm());
//        }catch (Exception e){
//            //no bottom
//        }
//    }
//    public void updateMode(String mode){
//        this.mode=mode;
//        loadImage();
//
//    }
//    public void updateSoundMode(String mode){
//        this.soundmode=soundmode;
//    }
//    private void setSize(){
//        picWidth = headImg.getWidth();
//        headRatio = headImg.getHeight()/picWidth;
//        lineRatio = lineImg.getHeight()/picWidth;
//        if(bottomImg!=null)bottomRatio=bottomImg.getHeight()/picWidth;
//        else bottomRatio = lineRatio;
//    }
//
//
//
//}
