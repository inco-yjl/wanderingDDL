package desktop.application.wanderingddl;

import desktop.application.wanderingddl.tools.DragUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

//TODO:联系时钟
public class WanderingController extends ContentController {
    private Font w_engFont;
    private Font w_znFont;
    private Font w_engTextFont;
    private String[] strings=new String[5];
    static private WanderingController wanderingController;
    private Label[] texts = new Label[5]; //距离xxx,还有大概,30,space,天
    private  double startX ;
    private double engLength;

    private WanderingController(){
        super();
        this.loadFont();
        this.setStage();
    }
    public static WanderingController getInstance(){
        if(wanderingController==null){
            wanderingController=new WanderingController();
        }
        return wanderingController;
    }
    public void countDown(){
        int originNumber = Integer.parseInt(texts[2].getText());
        if(originNumber>0)originNumber--;
        if(stage.isShowing()) {
            Pane all = (Pane) stage.getScene().getRoot();
            ((Label)all.lookup("#countDownNumber")).setText(originNumber+"");//x
        }
    }
    private void loadFont() {
        w_engFont = Font.loadFont(Objects.requireNonNull(getClass().getResource("MainContent/font/Alte DIN 1451 Mittelschrift gepraegt Regular.ttf")).toExternalForm(), 90);
        w_znFont = Font.loadFont(getClass().getResource("MainContent/font/znFont.ttf").toExternalForm(), 36);
        w_engTextFont = Font.loadFont(getClass().getResource("MainContent/font/znFont.ttf").toExternalForm(), 18);
    }

    //传参初始化
    private void initText() {
        for (int i = 0; i < 4; i++) {
            if (strings[i].length() > 0) {
                texts[i] = new Label(strings[i]);
            }else texts[i]= new Label("11");
        }
        texts[4] = new Label(" ");
        setZnFont(new Label[]{texts[0], texts[1], texts[3], texts[4]});//text4是空的占位label
        setNumFont(texts[2]);
    }
    @Override
    public void newInit(String[] sentences){
        try {
            for(int i =0;i<5;i++)
                strings[i]=sentences[i];
            this.start(stage);
            addTimer(sentences[3]);
        }catch (Exception e){
            System.out.println(e);
        }
    }
    protected void addTimer(String timeCount) {
        int period = 3600*1000;
        if(timeCount.equals("天")) {
            System.out.println(1);
            period = period*24;
        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        countDown();
                    }

                });
            }
        }, period, period);

    }
    @Override
    public void start(Stage stage) throws IOException {
        //设置字体
        this.initText();
        //建立节点
        Pane all = new Pane();
        //获取实际宽度
        initLengths(all);


        all.getChildren().addAll(getEngText(),getSeparater());

        all.setStyle("-fx-background-color: transparent;");

        DragUtil.addDragListener(stage,all);
        if(!stage.isShowing())
            stage.show();
        MinWindow.getInstance().listen(stage);
        stage.setX(Screen.getPrimary().getBounds().getWidth() - 480);
        stage.setY(Screen.getPrimary().getBounds().getHeight() - 262);

    }

    private HBox getHbox() {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.TOP_RIGHT);
        hbox.getChildren().addAll(getLeft());
        hbox.getChildren().addAll(texts[2]);
        hbox.getChildren().addAll(getRight());
        hbox.setLayoutX(10);

        return hbox;
    }
    private VBox getLeft() {
        VBox left = new VBox();
        Rectangle bottom = new Rectangle();
        bottom.setHeight(17);
        left.setAlignment(Pos.BOTTOM_RIGHT);
        left.getChildren().addAll(texts[0], texts[1],bottom);
        return left;
    }

    private VBox getRight() {
        VBox right = new VBox();
        Rectangle bottom = new Rectangle();
        bottom.setHeight(17);
        right.setAlignment(Pos.BOTTOM_RIGHT);
        right.getChildren().addAll(texts[4], texts[3],bottom);
        return right;
    }

    private void setZnFont(Label[] labels) {
        for (Label label :
                labels) {
            label.setFont(w_znFont);
            label.setAlignment(Pos.TOP_RIGHT);
            label.setPrefHeight(36);
            label.setTextFill(Color.valueOf("#ffffff"));
        }
        setEffect(labels);

    }

    private void setNumFont(Label label) {
        label.setId("countDownNumber");
        label.setAlignment(Pos.TOP_RIGHT);
        label.setFont(w_engFont);
        label.setPrefHeight(80);
        label.setTextFill(Color.valueOf("#ff0000"));
        setEffect(label);
    }

    private void setTextFont(Label label) {
        label.setFont(w_engTextFont);
        label.setTextFill(Color.valueOf("#ffffff"));
        setEffect(label);
    }

    private void setEffect(Label[] labels) {
        for (Label label :
                labels) {
            setEffect(label);
        }
    }

    private void setEffect(Label label) {
        DropShadow ds = new DropShadow();
        ds.setOffsetY(0f);
        ds.setColor(Color.valueOf("#333333"));
        label.setEffect(ds);
    }

    //此方法为快速设置一遍文字的显示，以获取文字的实际长度和位置，从而设置分割线和英文的位置
    private void initLengths(Pane pane) {
        HBox hBox = getHbox();
        pane.getChildren().add(hBox);

        Scene scene = new Scene(pane, 480, 262);
        scene.setFill(null);
        stage.setScene(scene);
        stage.show();
        VBox leftVbox = (VBox)hBox.getChildren().get(0);//left
        VBox rightbox = (VBox)hBox.getChildren().get(2);
        startX = leftVbox.getChildren().get(1).getBoundsInParent().getMaxX()-leftVbox.getChildren().get(1).getLayoutBounds().getWidth();
        engLength = rightbox.getBoundsInParent().getMaxX()-startX;

    }
    private Rectangle getSeparater() {
        Rectangle rectangle = new Rectangle();
        double evH = strings[4].length() * 18/ (engLength+30);
        //计算理想高度
        rectangle.setHeight(evH  * 18 + 30);
        rectangle.setWidth(6);
        rectangle.setFill(Paint.valueOf("#ff0000"));

        if (startX <= 0) {
            rectangle.setX(0);
        } else {
            rectangle.setX(startX-10);
        }

        rectangle.setY(58);
        return rectangle;
    }

    private Label getEngText() {
        Label label = new Label(strings[4].toUpperCase());
        setTextFont(label);
        label.setWrapText(true);
        label.setPrefWidth(engLength+30);//设置固定宽度，以控制自动换行
        if (startX <= 0) {
            label.setLayoutX(10);
        }else {
            label.setLayoutX(startX);
        }
        label.setLayoutY(86);
        return label;
    }
}
