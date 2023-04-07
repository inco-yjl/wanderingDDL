package desktop.application.wanderingddl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONPathTypedMultiNamesPrefixIndex1;
import desktop.application.wanderingddl.navigation.PageFactory;
import desktop.application.wanderingddl.tools.CreateFileUtil;
import desktop.application.wanderingddl.tools.DragUtil;

import desktop.application.wanderingddl.tools.FontLoader;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.Style;
import javafx.css.Stylesheet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.scene.media.*;

//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;

public class ApplicationController extends Application {
    static Stage stage;
    static BorderPane window;
    public Button wanderingPage;
    public Button toDoListPage;

    @Override
    public void start(Stage stage) throws IOException {
        ApplicationController.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("Config/Window.fxml"));
        HBox windowMenu = (HBox) FXMLLoader.load(getClass().getResource("Config/Menubar.fxml"));

        VBox mainContent = (VBox) FXMLLoader.load(getClass().getResource("Config/Content.fxml"));
        Pane wanderingPageContent = (Pane) FXMLLoader.load(getClass().getResource("MainContent/WanderingPage.fxml"));
        Pane toDoListPageContent = new ToDoPageContent().getToDoPageContent();


        Node[] pages = {wanderingPageContent, toDoListPageContent};
        PageFactory.setPages(pages);

        initWanderingSelect();



        createScene(root, windowMenu, mainContent);

    }

    public void initWanderingSelect() {
        Spinner<Integer> spinner = new Spinner<>(0, 999, 0, 1);
        spinner.setEditable(true);
        spinner.setCache(true);
        spinner.setId("2");
        spinner.setLayoutX(144);
        spinner.setLayoutY(149);
        spinner.setPrefHeight(38);
        spinner.setPrefWidth(116);
        spinner.getStyleClass().add("spinner");
        spinner.getStylesheets().addAll((getClass().getResource("MainContent/style/start.css").toExternalForm()));

        //  时间额度钮
        MenuButton select_ddl = new MenuButton();

        //导入文件选择按钮
        MenuButton import_btn = new MenuButton();

        //  没什么意义，拉长一点
        MenuItem hours = new MenuItem("小时              ");
        MenuItem days = new MenuItem("天");
        MenuItem weeks = new MenuItem("星期");
        MenuItem months = new MenuItem("月");
        select_ddl.getItems().addAll(hours, days, weeks, months);
        select_ddl.setId("3");
        select_ddl.setLayoutX(264);
        select_ddl.setLayoutY(149);
        select_ddl.setPrefHeight(38);
        select_ddl.setPrefWidth(120);
        select_ddl.setBackground(new Background(new BackgroundFill(Color.valueOf("white"), null, null)));
        select_ddl.setStyle("-fx-border-color: rgba(112,128,144,0.6); -fx-border-radius: 0;\n" +
                "-fx-border-width: 1 1 1 1;     -fx-text-fill: rgba(47,79,79,1);\n" +
                "    -fx-font-weight: bold;");
        select_ddl.setCursor(Cursor.HAND);


        String basePath="src/main/resources/desktop/application/wanderingddl/MyModels";
        String[] list=new File(basePath).list();
        ArrayList<MenuItem> mlist=new ArrayList<>();
        for(String el:list){
            MenuItem m=new MenuItem(el);
            mlist.add(m);
            import_btn.getItems().add(m);
        }
        import_btn.setId("8");
        import_btn.setLayoutX(150);
        import_btn.setLayoutY(300);
        import_btn.setPrefHeight(38);
        import_btn.setPrefWidth(120);
        import_btn.setBackground(new Background(new BackgroundFill(Color.valueOf("white"), null, null)));
        import_btn.setStyle("-fx-border-color: rgba(112,128,144,0.6); -fx-border-radius: 0;\n" +
                "-fx-border-width: 1 1 1 1;     -fx-text-fill: rgba(47,79,79,1);\n" +
                "    -fx-font-weight: bold;");
        import_btn.setCursor(Cursor.HAND);

        //
        //  预设一排 ———— 英文|数字|单位|英文
        TextField textField_1 = new TextField("Deadline comes in");
        textField_1.setId("4");
        textField_1.setLayoutX(52);
        textField_1.setLayoutY(200);
        textField_1.setPrefHeight(25);
        textField_1.setPrefWidth(140);
        textField_1.getStyleClass().add("start-textField");
        textField_1.getStylesheets().addAll((getClass().getResource("MainContent/style/start.css").toExternalForm()));

        Label label_x = new Label("x");
        label_x.setId("5");
        label_x.setLayoutX(203);
        label_x.setLayoutY(205);
        label_x.getStyleClass().add("hint");
        label_x.getStylesheets().addAll((getClass().getResource("MainContent/style/start.css").toExternalForm()));
        label_x.setTextFill(Paint.valueOf("#092053"));
        //label_x.setFont(Font.loadFont("Consolas", 13.0));

        Label label_days = new Label("days");
        label_days.setId("6");
        label_days.setLayoutX(230);
        label_days.setLayoutY(205);
        label_days.getStyleClass().add("hint");
        label_days.getStylesheets().addAll((getClass().getResource("MainContent/style/start.css").toExternalForm()));
        label_days.setTextFill(Paint.valueOf("#092053"));
        //label_x.setFont(Font.loadFont("Consolas", 13.0));

        TextField textField_2 = new TextField(",wait for it patiently");
        textField_2.setId("7");
        textField_2.setLayoutX(280);
        textField_2.setLayoutY(200);
        textField_2.setPrefHeight(25);
        textField_2.setPrefWidth(150);
        textField_2.getStyleClass().add("start-textField");
        textField_2.getStylesheets().addAll((getClass().getResource("MainContent/style/start.css").toExternalForm()));

        spinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                label_x.setText(newValue.toString());
            }
        });

        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                select_ddl.setText(((MenuItem) e.getSource()).getText());
                label_days.setText(Chi2Eng(((MenuItem) e.getSource()).getText()));
            }
        };
        EventHandler<ActionEvent> eventimport = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                import_btn.setText(((MenuItem) e.getSource()).getText());
            }
        };
        hours.setOnAction(event);
        days.setOnAction(event);
        weeks.setOnAction(event);
        months.setOnAction(event);
        for(MenuItem el:mlist){
            el.setOnAction(eventimport);
        }

        PageFactory.addNode(0,spinner,select_ddl,import_btn,textField_1,label_x,label_days,textField_2);
    }

    private String connectSentences(String[] sentences) {
        String result = new String("");
        for (int i = 0; i < sentences.length; i++)
            result += sentences[i];
        return result;
    }

    public void createScene(Parent root, HBox windowMenu, VBox mainContent) {


        BorderPane window = (BorderPane) root;
        ApplicationController.window = window;
        window.setTop(windowMenu);
        window.setRight(mainContent);
        Scene scene = new Scene(root, 700, 600);
        stage.setResizable(false); //固定大小
        stage.initStyle(StageStyle.TRANSPARENT);//隐藏头标题); //去除窗口样式
        scene.setFill(null);
        stage.setScene(scene);
        DragUtil.addDragListener(stage, window); //窗口拖拽
        stage.show();
    }

    @FXML
    protected void closeWindow() {
        stage.hide();
    }

    @FXML
    protected void minizeWindow() {
        stage.setIconified(true);
    }


    @FXML
    private void importJson() throws Exception {

//      import1.0版
        /*
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择需要的打开的文件");
        Stage s = new Stage();
        File inputFile = fileChooser.showOpenDialog(s);
        FileInputStream input = new FileInputStream(inputFile);
        */
//      import2.0版本
        String filename=((MenuButton) window.getRight().lookup("#" + Integer.toString(8))).getText();//导入文件名称
        String path="src/main/resources/desktop/application/wanderingddl/MyModels/"+filename;
        System.out.println(path);
        File inputFile = new File(path);
        FileInputStream input = new FileInputStream(inputFile);

        StringBuilder text = new StringBuilder("");


        int readLen = 0;
        //定义一个字节数组
        byte[] buf = new byte[8];
        try {
            //创建FileInputStream对象，用于读取文件
            while ((readLen = input.read(buf)) != -1) {
                String tmp = new String(buf, 0, readLen);
                System.out.println("tmp:" + tmp);
                text.append(tmp);
            }
            System.out.println("text:" + text);
            //从该输入流读取一个字节。如果没有输入可以读取，此方法将被阻止，然后返回-1
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭文件流，释放资源
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String[] sentences = new String[5];

        JSONArray arr = JSON.parseArray(text.toString());
        for (int i = 0; i < arr.size(); i++) {
            System.out.println("arr:" + arr.get(i));
            DataFormat data = JSON.parseObject(arr.get(i).toString(), DataFormat.class);
            sentences[i] = data.content;
        }
        String strings_3 = "";
        switch (sentences[3]) {
            case " hours":
                strings_3 = "小时";
                break;
            case " days":
                strings_3 = "天";
                break;
            case " weeks":
                strings_3 = "星期";
                break;
            case " months":
                strings_3 = "月";
                break;
            default:
                break;
        }
        ((TextField) window.getRight().lookup("#" + Integer.toString(0))).setText(sentences[0]);
        ((TextField) window.getRight().lookup("#" + Integer.toString(1))).setText(sentences[1]);//还有
        ((Spinner) window.getRight().lookup("#" + Integer.toString(2))).setPromptText(sentences[2]);
        ((MenuButton) window.getRight().lookup("#" + Integer.toString(3))).setText(strings_3);//天
//            sentences[3]="";


//            System.out.println("data"+data);


////            写入数据

        WanderingController.getInstance().newInit(sentences);


    }
//  获取导出的jsonstr
    String getSentences(){
        String[] sentences = new String[6];
        sentences[0] = ((TextField) window.getRight().lookup("#" + Integer.toString(0))).getText();//某某作业
        sentences[1] = ((TextField) window.getRight().lookup("#" + Integer.toString(1))).getText();//还有
        sentences[2] = ((Spinner) window.getRight().lookup("#" + Integer.toString(2))).getValue().toString();//x
        sentences[3] = ((MenuButton) window.getRight().lookup("#" + Integer.toString(3))).getText();//天
        String strings_3 = "";
        switch (sentences[3]) {
            case "小时":
                strings_3 = " hours";
                break;
            case "天":
                strings_3 = " days";
                break;
            case "星期":
                strings_3 = " weeks";
                break;
            case "月":
                strings_3 = " months";
                break;
            default:
                break;
        }
        List<DataFormat> list = new ArrayList<>();
        DataFormat f = new DataFormat(0, sentences[0]);
        list.add(new DataFormat(0, sentences[0]));
        list.add(new DataFormat(1, sentences[1]));
        list.add(new DataFormat(2, sentences[2]));
        list.add(new DataFormat(3, strings_3));
        System.out.println("haha");

        String jsonstr = JSON.toJSONString(list);
        System.out.println(jsonstr);
        return jsonstr;
    }
    @FXML
    void exportJson() throws IOException {
        String[] sentences = new String[6];
        sentences[0] = ((TextField) window.getRight().lookup("#" + Integer.toString(0))).getText();//某某作业
        sentences[1] = ((TextField) window.getRight().lookup("#" + Integer.toString(1))).getText();//还有
        sentences[2] = ((Spinner) window.getRight().lookup("#" + Integer.toString(2))).getValue().toString();//x
        sentences[3] = ((MenuButton) window.getRight().lookup("#" + Integer.toString(3))).getText();//天
        sentences[4] = ((TextField) window.getRight().lookup("#" + Integer.toString(6))).getText();//导出文件名称
        sentences[5] = ((MenuButton) window.getRight().lookup("#" + Integer.toString(8))).getText();//导入文件名称
        String strings_3 = "";
        switch (sentences[3]) {
            case "小时":
                strings_3 = " hours";
                break;
            case "天":
                strings_3 = " days";
                break;
            case "星期":
                strings_3 = " weeks";
                break;
            case "月":
                strings_3 = " months";
                break;
            default:
                break;
        }

        List<DataFormat> list = new ArrayList<>();
        DataFormat f = new DataFormat(0, sentences[0]);
        list.add(new DataFormat(0, sentences[0]));
        list.add(new DataFormat(1, sentences[1]));
        list.add(new DataFormat(2, sentences[2]));
        list.add(new DataFormat(3, strings_3));
        System.out.println("haha");

        String jsonstr = JSON.toJSONString(list);
        System.out.println(jsonstr);




//        导出file名称
        String filename="wangderingDDL-"+sentences[4];

//        导出1.0版

        /*
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage s = new Stage();
        File file = fileChooser.showSaveDialog(s);
        if (file == null)
            return;
        if (file.exists()) {//文件已存在，则删除覆盖文件
            file.delete();
        }
        String exportFilePath = file.getAbsolutePath();
        System.out.println("导出文件的路径" + exportFilePath);
        fileWriterMethod(exportFilePath, jsonstr.toString());
        */

//     导出2.0版
        CreateFileUtil.createJsonFile(jsonstr, "src/main/resources/desktop/application/wanderingddl/MyModels", filename);
        MenuItem m = new MenuItem(filename+".json");
        ((MenuButton) window.getRight().lookup("#" + Integer.toString(8))).getItems().add(m);
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                ((MenuButton) window.getRight().lookup("#" + Integer.toString(8))).setText(((MenuItem) e.getSource()).getText());
            }
        };
        m.setOnAction(event);
    }



    public static void fileWriterMethod(String filepath, String content) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filepath)) {
            fileWriter.append(content);
        }
    }

    @FXML
    protected void ToWanderingPage() {
        //选中
        wanderingPage.setStyle("-fx-background-color: #7c9fcc;");
        toDoListPage.setStyle("-fx-background-color: rgba(176,196,222,1);");
        routePage(0);
        System.out.println("wanderingDDL");
    }
    @FXML
    protected void ToRipplePage() {
    }

    @FXML
    private void ToDoListPage() {
        toDoListPage.setStyle("-fx-background-color: #7c9fcc;");
        wanderingPage.setStyle("-fx-background-color: rgba(176,196,222,1);");
        routePage(1);
    }
    @FXML
    private void MuyuPage(){
        System.out.println("tomuyu");
//        muyuPage.setStyle("-fx-background-color: #7c9fcc;");
//        routePage(2);
        ContentController.getInstance().newInit();
        System.out.println("tomuyu2");
    }
    @FXML
    protected void openWanderingUI() {
        String[] sentences = new String[5];
        sentences[0] = ((TextField) window.getRight().lookup("#0")).getText();//某某作业
        sentences[1] = ((TextField) window.getRight().lookup("#1")).getText();//还有
        sentences[2] = ((Spinner) window.getRight().lookup("#2")).getValue().toString();//x
        sentences[3] = ((MenuButton) window.getRight().lookup("#3")).getText();//天
        String strings_3 = Chi2Eng(sentences[3]);
        String strings_4 = ((TextField) window.getRight().lookup("#4")).getText();
        String strings_5 = ((TextField) window.getRight().lookup("#7")).getText();
        sentences[4] = strings_4 + " " + sentences[2] + strings_3 + " " + strings_5;
        WanderingController.getInstance().newInit(sentences);
    }

    @FXML
    protected void openWanderingUI_with_ji() {
        openWanderingUI();
        play_ji();
    }

    public void play_ji() {
        URL url = getClass().getResource("ji.mp3");
        Media media = new Media(url.toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }

    private String Chi2Eng(String sentences) {
        switch (sentences) {
            case "小时              ":
                return " hours";
            case "天":
                return " days";
            case "星期":
                return " weeks";
            case "月":
                return " months";
            default:
                break;
        }
        return sentences;
    }

    private void routePage(int index) {
        Node node = PageFactory.createPageService(index);
        window.setRight(node);
    }

    public void newWindow() throws IOException {
        ExportWindow open  = new ExportWindow(getSentences());
        open.start(new Stage());
        //stage.hide(); //点开新的界面后，是否关闭此界面
    }
    public static void main(String[] args) {
        launch();
    }
}
