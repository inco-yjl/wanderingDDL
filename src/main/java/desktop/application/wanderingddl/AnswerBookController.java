package desktop.application.wanderingddl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import desktop.application.wanderingddl.navigation.PageFactory;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.Random;

public class AnswerBookController {
    static JSONArray answers;
    private Font font;
    public AnswerBookController(){
        super();
        this.loadFont();
    }
    private void setAnimation(Label label){
        Group group = new Group();
        FadeTransition ft = new FadeTransition(Duration.millis(2000),label);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }
    public static String[] getAnswer() {
        Random random = new Random();
        int index = random.nextInt(1096);
        JSONObject answerOj = JSONObject.from(answers.get(index));
        String chineText = answerOj.getString("chinese");
        String engText = answerOj.getString("english");
        return new String[]{chineText, engText};
    }
    public static void setAnswers(JSONArray jsa){
        answers =jsa;
    }
    public void loadFont(){
        font = Font.loadFont(getClass().getResource("MainContent/font/Acme.ttf").toExternalForm() ,27);
    }
    public void showAnswer(){
        Pane all = new Pane();
        all.setPrefHeight(600);
        all.setPrefWidth(502);
        all.getChildren().addAll(getText(),getNext());
        ApplicationController.window.setRight(all);
    }
    private VBox getText(){
        VBox vBox = new VBox();
        vBox.setPrefWidth(352);
        vBox.setStyle("-fx-background-color: white;-fx-text-fill: black;");
        vBox.getChildren().addAll(getLabels());
        vBox.setLayoutX(75);
        vBox.setLayoutY(150);
        vBox.setPadding(new Insets(10));
        vBox.setAlignment(Pos.CENTER);
        vBox.setMinHeight(160);
        setEffect(vBox);
        return vBox;
    }
    private Label[] getLabels(){
        String[] answerTexts = getAnswer();
        if(answerTexts[1].length()>0){
            return getLabels(answerTexts[1],answerTexts[0]);
        }else return new Label[]{getLabel(answerTexts[0])};

    }
    private Label[] getLabels(String engText, String znText) {
        Label label = new Label();
        label.setAlignment(Pos.CENTER);
        label.setText(engText);
        label.setFont(font);
        label.setWrapText(true);
        setAnimation(label);
        return new Label[]{label,getLabel(znText)};
    }
    private Label getLabel(String znText){
        Label label = new Label();
        label.setAlignment(Pos.CENTER);
        label.setText(znText);
        label.setStyle("-fx-font-size: 27");
        label.setWrapText(true);
        setAnimation(label);
        return label;
    }
    private Label getNext(){
        Label label = new Label("获取下一个答案>>");
        label.setPrefWidth(150);
        label.setAlignment(Pos.CENTER);
        label.setLayoutX(176);
        label.setLayoutY(400);
        label.setStyle("-fx-cursor: hand");
        label.setOnMouseClicked(event -> {
          toNext();
        });
        return label;
    }
    private void toNext(){
        ApplicationController.window.setRight(PageFactory.createPageService(2));
    }
    private void setEffect(VBox vBox) {
        DropShadow ds = new DropShadow();
        ds.setOffsetY(0f);
        ds.setColor(Color.valueOf("#666666"));
        vBox.setEffect(ds);
    }

}
