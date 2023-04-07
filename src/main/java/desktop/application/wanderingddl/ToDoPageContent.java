package desktop.application.wanderingddl;

import javafx.css.Stylesheet;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.LinkedList;


public class ToDoPageContent {
    Pane pageContent;
    ScrollPane scrollPane;
    VBox allItems;
    LinkedList<String> items;
    int mode;
    public ToDoPageContent() {
        super();
        initItems();
        initPane();
    }
    public Pane getToDoPageContent(){
        return pageContent;
    }
    private void initItems(){
        items  = new LinkedList<String>();
    }
    private void initPane() {
        pageContent = new Pane();
        pageContent.setMaxHeight(600);
        pageContent.setMaxWidth(502);
        setScrollPane();

        pageContent.getChildren().addAll(scrollPane,getRadioGroup(),getStartButton());
        pageContent.getStylesheets().add((getClass().getResource("MainContent/style/start.css").toExternalForm()));

    }
    private void setScrollPane() {
        scrollPane = new ScrollPane();
        scrollPane.setLayoutY(20);
        scrollPane.setLayoutX(0);
        scrollPane.setPrefViewportHeight(230);
        scrollPane.setPrefViewportWidth(483);
        setAllItems();
        scrollPane.setContent(allItems);
    }
    private HBox getRadioGroup(){
        HBox radiogroup = new HBox();
        VBox mode1 = new VBox();
        VBox separator = new VBox();
        VBox mode2 = new VBox();
        RadioButton[] radioButtons = getRadios();
        mode1.getChildren().addAll(radioButtons[0],getModeImg()[0]);
        mode2.getChildren().addAll(radioButtons[1],getModeImg()[1]);

        radiogroup.setLayoutX(140);
        radiogroup.setLayoutY(310);
        separator.setPrefWidth(70);
        radiogroup.getChildren().addAll(mode2,separator,mode1);
        return radiogroup;
    }
    private RadioButton getRadio(int index){
        RadioButton radioButton = new RadioButton();
        radioButton.setOnMouseClicked(event->{
            if(radioButton.isSelected())
                mode = index;
        });
        return radioButton;
    }
    private ImageView[] getModeImg(){
        Image[] images = new Image[2];
        ImageView[] imageViews = new ImageView[2];
        for(int i=0;i<images.length;i++) {
            images[i] = new Image(getClass().getResource("ContentSrc/todoImg/mode"+(i+1)+".png").toExternalForm());
            imageViews[i] = new ImageView(images[i]);
            imageViews[i].setFitHeight(90);
            imageViews[i].setFitWidth(70);
        }

        return imageViews;
    }
    private RadioButton[] getRadios(){
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton[] radioButtons = new RadioButton[2];
        for (int i = 0; i < 2; i++) {
            radioButtons[i] = getRadio(i+1);
            radioButtons[i].setToggleGroup(toggleGroup);
        }
        radioButtons[0].setText("极致色彩");
        radioButtons[1].setText("简约线条");

        radioButtons[1].setSelected(true);
        mode = 2;

        return radioButtons;
    }
    private void setAllItems() {
        allItems = new VBox();
        allItems.setPrefWidth(483);
        allItems.getChildren().add(getHeader());
        allItems.setPrefHeight(items.size()*40);
    }
    private HBox getHeader() {
        HBox header= new HBox();
        header.setAlignment(Pos.CENTER);
        header.setMinHeight(40);

        HBox square = new HBox();
        square.setPrefWidth(20);

        header.getChildren().addAll(getTitle(),getInput(),square,getAddButton());
        return header;
    }
    private Label getTitle(){
        Label title = new Label("待办事项列表");
        title.setStyle("    -fx-font-family: Microsoft YaHei;\n" +
                "-fx-font-size: 14px;-fx-text-fill: #092053");
        title.setAlignment(Pos.CENTER);
        title.setPrefWidth(110);
        return title;
    }
    private Label getSpace() {
        Label title = new Label();
        title.setPrefWidth(110);
        return title;
    }
    private TextField getInput(){
        TextField input = new TextField();
        items.add("");
        return input;
    }
    private Button getAddButton(){
        Button addButton = new Button("+");
        addButton.setPrefHeight(30);
        addButton.setPrefWidth(30);
        addButton.getStyleClass().add("add-btn");
        addButton.setOnAction(e->{
            addItem();
        });
        return addButton;
    }
    private Button getMinusButton(HBox item) {
        Button minusButton = new Button("-");
        minusButton.setPrefWidth(30);
        minusButton.setPrefHeight(30);
        minusButton.getStyleClass().add("add-btn");
        int index =items.size()-1;
        minusButton.setOnAction(e->{
            allItems.getChildren().remove(item);
            items.remove(index);
        });
        return minusButton;
    }
    private void addItem(){
        if (items.size()==10) {
            return;
        }
        HBox item= new HBox();
        item.setAlignment(Pos.CENTER);
        item.setMinHeight(40);

        HBox square = new HBox();
        square.setPrefWidth(20);

        item.getChildren().addAll(getSpace(),getInput(),square,getMinusButton(item));
        allItems.getChildren().add(item);
    }
    private void openToDoList(){
        String[] strings = new String[items.size()];
        for(int i=0;i<items.size();i++) {
            HBox node = (HBox) allItems.getChildren().get(i);
            TextField textField = (TextField) node.getChildren().get(1);
            strings[i]=textField.getText();
            if(strings[i]==null) {
                strings[i]="";
            }
        }
        ToDoListController.getInstance().newInit(strings,mode);
    }

    private Button getStartButton(){
        Button startButton = new Button("确定");
        startButton.getStyleClass().add("start-btn");
        startButton.setOnAction(e->{
            openToDoList();
        });
        startButton.setPrefWidth(116);
        startButton.setPrefHeight(30);
        startButton.setLayoutX(180);
        startButton.setLayoutY(443);
        return startButton;
    }

}
