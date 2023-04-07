package desktop.application.wanderingddl;

import javafx.css.Stylesheet;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.LinkedList;


public class ToDoPageContent {
    Pane pageContent;
    ScrollPane scrollPane;
    VBox allItems;
    LinkedList<String> items;
    LinkedList<TextField> listInputs;
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
        listInputs = new LinkedList<TextField>();
    }
    private void initPane() {
        pageContent = new Pane();
        pageContent.setMaxHeight(600);
        pageContent.setMaxWidth(502);
        setScrollPane();

        pageContent.getChildren().addAll(scrollPane,getStartButton());
        pageContent.getStylesheets().add((getClass().getResource("MainContent/style/start.css").toExternalForm()));

    }
    private void setScrollPane() {
        scrollPane = new ScrollPane();
        scrollPane.setPrefViewportHeight(350);
        scrollPane.setPrefViewportWidth(483);
        setAllItems();
        scrollPane.setContent(allItems);
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
        title.setPrefWidth(144);
        return title;
    }
    private Label getSpace() {
        Label title = new Label();
        title.setPrefWidth(144);
        return title;
    }
    private TextField getInput(){
        TextField input = new TextField();
        listInputs.push(input);
        items.add("");
        return input;
    }
    private Button getAddButton(){
        Button addButton = new Button("+");
        addButton.getStyleClass().add("add-btn");
        addButton.setOnAction(e->{
            addItem();
        });
        return addButton;
    }
    private Button getMinusButton(HBox item) {
        Button addButton = new Button("-");
        addButton.getStyleClass().add("add-btn");
        addButton.setOnAction(e->{
            allItems.getChildren().remove(item);
        });
        return addButton;
    }
    private void addItem(){
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
        for(int i=0;i<listInputs.size();i++) {
            strings[i] = listInputs.get(i).getText();
        }
        ToDoListController.getInstance().newInit(strings);
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
        startButton.setLayoutY(423);
        return startButton;
    }

}
