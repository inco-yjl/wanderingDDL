package desktop.application.wanderingddl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONPathTypedMultiNamesPrefixIndex1;
import desktop.application.wanderingddl.navigation.PageFactory;
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
import java.util.ArrayList;
import java.util.List;

//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;

public class ApplicationController extends Application {
    static Stage stage;
    static BorderPane window;
    public Button wanderingPage;

    public Pane wanderingPageContent;


    @Override
    public void start(Stage stage) throws IOException {
        ApplicationController.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("Config/Window.fxml"));
        HBox windowMenu = (HBox) FXMLLoader.load(getClass().getResource("Config/Menubar.fxml"));
        VBox mainContent = (VBox) FXMLLoader.load(getClass().getResource("Config/Content.fxml"));
        this.wanderingPageContent = (Pane) FXMLLoader.load(getClass().getResource("MainContent/WanderingPage.fxml"));
        initWanderingSelect();
        Node[] pages = {wanderingPageContent};
        createScene(root, windowMenu, mainContent);
        PageFactory.setPages(pages);
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
        spinner.getStylesheets().add("@style/start.css");

        //  时间额度钮
        MenuButton select_ddl = new MenuButton();
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

        //
        //  预设一排 ———— 英文|数字|单位|英文
        TextField textField_1 = new TextField("Deadline comes in");
        textField_1.setId("4");
        textField_1.setLayoutX(52);
        textField_1.setLayoutY(200);
        textField_1.setPrefHeight(25);
        textField_1.setPrefWidth(140);
        textField_1.getStyleClass().add("start-textField");
        textField_1.getStylesheets().add("@style/start.css");

        Label label_x = new Label("x");
        label_x.setId("5");
        label_x.setLayoutX(203);
        label_x.setLayoutY(205);
        label_x.getStyleClass().add("hint");
        label_x.getStylesheets().add("@style/start.css");
        label_x.setTextFill(Paint.valueOf("#092053"));
        //label_x.setFont(Font.loadFont("Consolas", 13.0));

        Label label_days = new Label("days");
        label_days.setId("6");
        label_days.setLayoutX(230);
        label_days.setLayoutY(205);
        label_days.getStyleClass().add("hint");
        label_days.getStylesheets().add("@style/start.css");
        label_days.setTextFill(Paint.valueOf("#092053"));
        //label_x.setFont(Font.loadFont("Consolas", 13.0));

        TextField textField_2 = new TextField(",wait for it patiently");
        textField_2.setId("7");
        textField_2.setLayoutX(280);
        textField_2.setLayoutY(200);
        textField_2.setPrefHeight(25);
        textField_2.setPrefWidth(150);
        textField_2.getStyleClass().add("start-textField");
        textField_2.getStylesheets().add("@style/start.css");

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
        hours.setOnAction(event);
        days.setOnAction(event);
        weeks.setOnAction(event);
        months.setOnAction(event);
        this.wanderingPageContent.getChildren().add(spinner);
        this.wanderingPageContent.getChildren().add(select_ddl);
        this.wanderingPageContent.getChildren().add(textField_1);
        this.wanderingPageContent.getChildren().add(label_x);
        this.wanderingPageContent.getChildren().add(label_days);
        this.wanderingPageContent.getChildren().add(textField_2);
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
        Scene scene = new Scene(root, 700, 450);
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


        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择需要的打开的文件");
        Stage s = new Stage();
        File inputFile = fileChooser.showOpenDialog(s);

//        JFileChooser fileChooser = new JFileChooser(".");
//        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//        fileChooser.setDialogTitle("选择输入Excel文件");

//        int ret = fileChooser.showOpenDialog(null);
//        if (ret == JFileChooser.APPROVE_OPTION) {
//            File inputFile = fileChooser.getSelectedFile().getAbsoluteFile();
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
        // 然后根据实际情况去操作input即可。
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

    @FXML
    void exportJson() throws IOException {
        String[] sentences = new String[5];
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
        // 制造假数据，模拟JSON数据
        List<DataFormat> list = new ArrayList<>();
        DataFormat f = new DataFormat(0, sentences[0]);
        list.add(new DataFormat(0, sentences[0]));
        list.add(new DataFormat(1, sentences[1]));
        list.add(new DataFormat(2, sentences[2]));
        list.add(new DataFormat(3, strings_3));
        System.out.println("haha");

        String jsonstr = JSON.toJSONString(list);
        System.out.println(jsonstr);
//        JSONArray jsonArray1 =JSONArray.fromObject(sentences);
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
//        FileWriteUtil.WriteDocument(exportFilePath, jsonArray1);
//        ShowDialog.showMessageDialog(FXRobotHelper.getStages().get(0), "导出成功!保存路径:\n"+exportFilePath, "提示");
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
        routePage(0);
        System.out.println("wanderingDDL");
    }

    @FXML
    private void ToDoListPage() {
        //  routePage(1);
        ToDoListController.getInstance().newInit();
        System.out.println("todoList");
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

    public static void main(String[] args) {
        launch();
    }
}
