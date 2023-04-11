package desktop.application.wanderingddl;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.LinkedList;


public class MuYuPageContent {
    Pane pageContent;
    LinkedList<MuYuRadio> radios;
    ToggleGroup toggleGroup;
    public MuYuPageContent() {
        super();
        toggleGroup = new ToggleGroup();
        radios = new LinkedList<MuYuRadio>();
        initPane();
    }
    public Pane getMuYuPageContent(){
        return pageContent;
    }
    private void initPane(){
        pageContent = new Pane();
        pageContent.setPrefHeight(600);
        pageContent.setPrefWidth(502);
        pageContent.getChildren().addAll(getCheckBox(),getCheckBox2(),getRadioGroup(),getStartButton());
        pageContent.setStyle("-fx-text-fill: rgba(47,79,79);-fx-font-size: 14px");
    }
    public CheckBox getCheckBox(){
        CheckBox checkBox = new CheckBox("记录功德总数");
        checkBox.setId("ifSum");
        checkBox.setLayoutX(20);
        checkBox.setLayoutY(30);
        checkBox.setAlignment(Pos.TOP_LEFT);
        return checkBox;
    }
    public CheckBox getCheckBox2(){
        CheckBox checkBox = new CheckBox("随机样式");
        checkBox.setId("ifRandome");
        checkBox.setLayoutX(20);
        checkBox.setLayoutY(70);
        checkBox.setAlignment(Pos.TOP_LEFT);
        return checkBox;
    }
    public HBox getRadioGroup(){
        HBox all = new HBox();
        all.getChildren().addAll(getLineChoices(),getColorChoices());
        all.setLayoutX(20);
        all.setAlignment(Pos.TOP_LEFT);
        all.setLayoutY(110);
        return all;
    }
    private HBox getColorChoices(){
        HBox lineChoices = new HBox();
        Label label = new Label("■极致色彩");
        VBox choices = new VBox();
        HBox sqaure = new HBox();
        sqaure.setPrefHeight(15);
        choices.getChildren().addAll(getChoice("wood","木"),getChoice("gold","金"),getChoice("red","红"));
        choices.setPadding(new Insets(0,0,0,15));
        lineChoices.getChildren().addAll(label,choices);
        return lineChoices;
    }
    private HBox getLineChoices(){
        HBox lineChoices = new HBox();
        Label label = new Label("■简约线条");
        VBox choices = new VBox();
        HBox sqaure = new HBox();
        sqaure.setPrefHeight(15);
        choices.getChildren().addAll(getChoice("line1","纯黑"),getChoice("line2","纯白"));
        choices.setPadding(new Insets(0,0,0,15));
        lineChoices.getChildren().addAll(label,choices);
        lineChoices.setPadding(new Insets(0,15,0,0));
        return lineChoices;
    }
    public HBox getChoice(String loadMode,String hint) {
        HBox choice = new HBox();
        RadioButton radioButton = new RadioButton(hint);
        radioButton.setToggleGroup(toggleGroup);
        radios.push(new MuYuRadio(radioButton,loadMode));
        if(radios.size()==1)
            radioButton.setSelected(true);

        Image img = new Image(getClass().getResource("ContentSrc/MuyuImg/muyu-"+loadMode+".png").toExternalForm());
        ImageView imageView = new ImageView(img);
        imageView.setFitWidth(70);
        imageView.setFitHeight(70);
        choice.getChildren().addAll(radioButton,imageView);
        return choice;
    }
    private void openMuYu(){
        CheckBox ifSum = (CheckBox) pageContent.getChildren().get(0);
        CheckBox ifRandomm = (CheckBox) pageContent.getChildren().get(1);
        String mode ="line1";
        for(MuYuRadio muYuRadio:radios){
            if(muYuRadio.radio.isSelected()){
                mode = muYuRadio.name;
                break;
            }
        }
        MuYuController.getInstance().newInit(ifSum.isSelected(),ifRandomm.isSelected(),mode);
    }
    private Button getStartButton(){
        Button startButton = new Button("确定");
        startButton.getStyleClass().add("start-btn");
        startButton.setOnAction(e->{
            openMuYu();
        });
        startButton.setPrefWidth(116);
        startButton.setPrefHeight(30);
        startButton.setLayoutX(180);
        startButton.setLayoutY(443);
        return startButton;
    }
}
class MuYuRadio{
    RadioButton radio;
    String name;
    public MuYuRadio(RadioButton radioButton, String name){
        super();
        this.radio = radioButton;
        this.name = name;
    }

}
