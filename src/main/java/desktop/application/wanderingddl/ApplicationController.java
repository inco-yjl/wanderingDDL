package desktop.application.wanderingddl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import desktop.application.wanderingddl.navigation.PageFactory;
import desktop.application.wanderingddl.tools.DragUtil;

import desktop.application.wanderingddl.tools.FontLoader;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
        MenuButton select_ddl = new MenuButton();
        MenuItem hours = new MenuItem("小时");
        MenuItem days = new MenuItem("天");
        MenuItem weeks = new MenuItem("星期");
        MenuItem months = new MenuItem("月");
        select_ddl.getItems().addAll(hours, days, weeks, months);
        select_ddl.setId("3");
        select_ddl.setLayoutX(391);
        select_ddl.setLayoutY(138);
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                select_ddl.setText(((MenuItem) e.getSource()).getText());
            }
        };
        hours.setOnAction(event);
        days.setOnAction(event);
        weeks.setOnAction(event);
        months.setOnAction(event);
        this.wanderingPageContent.getChildren().add(select_ddl);
    }

    public void createScene(Parent root, HBox windowMenu, VBox mainContent) {
        BorderPane window = (BorderPane) root;
        ApplicationController.window = window;
        window.setTop(windowMenu);
        window.setRight(mainContent);
        Scene scene = new Scene(root, 700, 800);
        stage.setResizable(false); //固定大小
        stage.initStyle(StageStyle.TRANSPARENT);//隐藏头标题); //去除窗口样式
        scene.setFill(null);
        stage.setScene(scene);
        DragUtil.addDragListener(stage, root); //窗口拖拽
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
            FileInputStream input = new FileInputStream(inputFile );


            StringBuilder text = new StringBuilder("");



            int readLen = 0;
            //定义一个字节数组
            byte[] buf = new byte[8];
            try {
                //创建FileInputStream对象，用于读取文件
                while((readLen = input.read(buf)) != -1){
                    String tmp=new String(buf,0,readLen);
                    System.out.println("tmp:"+tmp);
                    text.append(tmp);
                }
                System.out.println("text:"+text);
                //从该输入流读取一个字节。如果没有输入可以读取，此方法将被阻止，然后返回-1
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                //关闭文件流，释放资源
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            String[] sentences = new String[5];
            // 然后根据实际情况去操作input即可。
            JSONArray arr=JSON.parseArray(text.toString());
            for(int i =0; i < arr.size(); i++){
                System.out.println("arr:"+arr.get(i));
                DataFormat data= JSON.parseObject(arr.get(i).toString(), DataFormat.class);
                sentences[i]=data.content;
            }
            String strings_3="";
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
        DataFormat f=new DataFormat(0,sentences[0]);
        list.add(new DataFormat(0,sentences[0]));
        list.add(new DataFormat(1,sentences[1]));
        list.add(new DataFormat(2,sentences[2]));
        list.add(new DataFormat(3,strings_3));
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
        if(file.exists()){//文件已存在，则删除覆盖文件
            file.delete();
        }
        String exportFilePath = file.getAbsolutePath();
        System.out.println("导出文件的路径" + exportFilePath);
        fileWriterMethod(exportFilePath,jsonstr.toString());
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
        wanderingPage.setStyle("-fx-border-color: #a8ddb5;");
        routePage(0);
        System.out.println(1);
    }

    @FXML
    protected void openWanderingUI() {
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
        sentences[4] = "deadline comes in " + sentences[2] + strings_3 + ", wait for it patiently";
        WanderingController.getInstance().newInit(sentences);
    }

    private void routePage(int index) {
        Node node = PageFactory.createPageService(index);
        window.setRight(node);
    }

    public static void main(String[] args) {
        launch();
    }
}
