package desktop.application.wanderingddl;

import desktop.application.wanderingddl.tools.FontLoader;
import javafx.application.Application;
import javafx.css.Style;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.io.IOException;



public class WanderingController extends Application {
    private Font w_engFont;
    private Font w_znFont;
    public void start(Stage stage) throws IOException {
        Pane all = new Pane();
        HBox hbox= new HBox();
        VBox left = new VBox();
        VBox right = new VBox();
        Label text1 = new Label("距离提交作业");//text1
        Label text2 = new Label("差不多还有");//text2
        Label text3 = new Label("30");
        Label text4 = new Label(" ");
        Label text5 = new Label("天");

        w_engFont= Font.loadFont(getClass().getResource("Alte DIN 1451 Mittelschrift gepraegt Regular.ttf").toExternalForm(), 84);
        w_znFont=Font.loadFont(getClass().getResource("znFont.ttf").toExternalForm(), 36);
        setZnFont(new Label[]{text1,text2,text4,text5},text3);
        setNumFont(text3);

        left.setAlignment(Pos.TOP_RIGHT);
        right.setAlignment(Pos.TOP_RIGHT);
        hbox.setAlignment(Pos.BOTTOM_RIGHT);
        left.getChildren().addAll(text1);
        left.getChildren().addAll(text2);
        right.getChildren().addAll(text4,text5);
        hbox.getChildren().addAll(left);
        hbox.getChildren().addAll(text3);
        hbox.getChildren().addAll(right);
        all.getChildren().addAll(hbox);
        Scene scene = new Scene(all, 480,262);
        scene.setFill(null);
        stage.setScene(scene);
        stage.show();
    }
    private void setZnFont(Label[] labels,Label text){
        for (Label label:
             labels) {
            label.setFont(w_znFont);
            label.setTextFill(Color.valueOf("#ffffff"));
            label.setPrefHeight(text.getPrefHeight()/2);
        }
        setEffect(labels);
    }
    private void setEffect(Label[] labels){
        for (Label label:
                labels) {
            DropShadow ds = new DropShadow();
            ds.setOffsetY(0f);
            ds.setColor(Color.valueOf("#666666"));
            label.setEffect(ds);
        }
    }
    private void setNumFont(Label label) {

        label.setFont(w_engFont);
        label.setTextFill(Color.valueOf("#ff0000"));
        setEffect(new Label[]{label});
    }

}
