package desktop.application.wanderingddl.tools;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import desktop.application.wanderingddl.AnswerBookController;
import javafx.scene.Node;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;


import java.io.*;

public class SaverAndLoader {
    private String  pathHead ="src/main/resources/desktop/application/wanderingddl/Cache/";

    public static SaverAndLoader tool=new SaverAndLoader();
    private SaverAndLoader(){

    }
    private class WanderingItem {
        String content;
        public WanderingItem(String str) {
            super();
            content = str;
        }
    }
    public void loadPage(Node node, int index) {
        switch (index) {
            case 0 -> loadWanderingInput(node);
            case 1 -> loadToDoInput(node);
            case 2 -> loadAnswerBook();
            default -> {
            }
        }
    }
    private void loadAnswerBook(){
        JSONArray jsa = read("src/main/resources/desktop/application/wanderingddl/ContentSrc/AnswerBooks/","allAnswers");
        AnswerBookController.setAnswers(jsa);
    }

    public void loadToDoInput(Node node) {}

    public void loadWanderingInput(Node node){
        JSONObject jsonObject = read("wanderingDDL");
        JSONArray jsa=jsonObject.getJSONArray("last-modified");
        Pane all = (Pane)node;
        System.out.println(jsa);
        ((TextField) all.lookup("#0")).setText(jsa.get(0).toString());//某某作业
        ((TextField) all.lookup("#1")).setText(jsa.get(1).toString());
        ((Spinner) all.lookup("#2")).getValueFactory().setValue(Integer.parseInt(jsa.get(2).toString()));//x
        ((MenuButton) all.lookup("#3")).setText(jsa.get(3).toString());
        ((TextField) all.lookup("#4")).setText(jsa.get(4).toString());
        ((TextField) all.lookup("#7")).setText(jsa.get(5).toString());
    }
    public void saveWanderingInput(String[] data){
        WanderingItem[] list = new WanderingItem[data.length];
        for (int i = 0; i < data.length; i++) {
            list[i] = new WanderingItem(data[i]);
        }
        JSONArray jsonstr = JSONArray.from(data);
        JSONObject jsonObject = read("wanderingDDL");

        jsonObject.put("last-modified",jsonstr);
        save(jsonObject,"wanderingDDL");
    }
    private void save(JSONObject jsonObject, String filePath){
        try{ BufferedWriter bw = new BufferedWriter(new FileWriter(pathHead+filePath+".json"));
            bw.write(jsonObject.toString());
            bw.flush();
            bw.close();
        }catch (IOException e) {
            System.out.println("save error");
        }
    }
    private JSONObject read(String filename){
        String path=pathHead+filename+".json";
        StringBuffer stringBuffer = new StringBuffer();
        try {
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(new FileInputStream(path),"UTF-8"));

            String line;
            while((line=bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//开始解析

        JSONObject arr = JSONObject.parseObject(stringBuffer.toString());
        return arr;
    }
    private JSONArray read(String pathHead,String filename) {
        String path=pathHead+filename+".json";
        StringBuffer stringBuffer = new StringBuffer();
        try {
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(new FileInputStream(path),"UTF-8"));

            String line;
            while((line=bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//开始解析

        JSONArray arr = JSONArray.parseArray(stringBuffer.toString());
        return arr;
    }


}