package desktop.application.wanderingddl;

import desktop.application.wanderingddl.tools.CreateFileUtil;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ExportWindow extends Application {
    String jsonstr;
    StackPane root;
    public ExportWindow(String jsonstr){
        this.jsonstr=jsonstr;
    }
    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        this.root=root;
        Scene scene = new Scene(root, 600, 400);
        init();
        primaryStage.setTitle("导出模板");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public void init(){
//      输入模板名
        TextField txtFld=new TextField("好菜好想死");
        txtFld.setId("1");
        txtFld.setLayoutX(280);
        txtFld.setLayoutY(200);
        txtFld.setPrefHeight(25);
        txtFld.setPrefWidth(50);
        Button btn = new Button("导出");
        btn.setId("2");
        btn.setLayoutX(450);
        btn.setLayoutY(370);

        btn.setOnAction((ActionEvent event) -> {
            String filename="wangderingDDL-"+txtFld.getText();
            CreateFileUtil.createJsonFile(jsonstr, "src/main/resources/desktop/application/wanderingddl/MyModels", filename);
        });

        root.getChildren().add(btn);
//        root.getChildren().add(txtFld);
    }
}
