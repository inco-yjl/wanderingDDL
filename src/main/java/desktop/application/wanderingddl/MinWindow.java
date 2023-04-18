package desktop.application.wanderingddl;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;


import desktop.application.wanderingddl.tools.SaverAndLoader;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * 自定义系统托盘(单例模式)
 */
public class MinWindow {

    private static MinWindow instance;
    private static TrayIcon trayIcon;
    final private PopupMenu popup = new PopupMenu();


    static{
        //执行stage.close()方法,窗口不直接退出
        Platform.setImplicitExit(false);

        URL url = MinWindow.class.getResource("Config/source/icon.png");
        Image image = Toolkit.getDefaultToolkit().getImage(url);
        //系统托盘图标
        trayIcon = new TrayIcon(image);
        //初始化监听事件(空)

    }

    public static MinWindow getInstance(){
        if(instance == null){
            instance = new MinWindow();
        }
        return instance;
    }

    private MinWindow(){
        try {
            //检查系统是否支持托盘
            if (!SystemTray.isSupported()) {
                //系统托盘不支持
                return;
            }
            //设置图标尺寸自动适应
            trayIcon.setImageAutoSize(true);
            //系统托盘
            SystemTray tray = SystemTray.getSystemTray();
            //弹出式菜单组件
            trayIcon.setPopupMenu(popup);
            //鼠标移到系统托盘,会显示提示文本
            trayIcon.setToolTip("命运之轮");
            tray.add(trayIcon);
            initStage();
        } catch (Exception e) {
        }
    }
    private void initStage(){
        Stage[] stages = new Stage[]{ApplicationController.stage,WanderingController.getInstance().stage,ToDoListController.getInstance().stage,MuYuController.getInstance().stage};
        SingleWindow.singleWindows[0].setMainStage(stages);
        SingleWindow.singleWindows[1].setWanderingStage(stages[1]);
        SingleWindow.singleWindows[2].setToDoStage(stages[2]);
        SingleWindow.singleWindows[3].setMuYuStage(stages[3]);
        popup.add(SingleWindow.singleWindows[0].exitItem);

        trayIcon.addMouseListener(SingleWindow.singleWindows[0].mouseListener);
    }

    private int indexOf(MenuItem menuItem) {
        for (int i = 0; i < popup.getItemCount(); i++) {
            if(popup.getItem(i).equals(menuItem))return i;
        }
        return -1;
    }
    private void remove(MenuItem menuItem) {
        int index = indexOf(menuItem);
        if(index>0)
            popup.remove(index);
    }
    public void listen(int index) {
        popup.add(SingleWindow.singleWindows[index].showItem);
        popup.add(SingleWindow.singleWindows[index].exitItem);
    }
    public static void closeWindow(SingleWindow singleWindow){
        instance.remove(singleWindow.showItem);
        instance.remove(singleWindow.exitItem);
    }


    /**
     * 关闭窗口
     */
    public void hide(Stage stage){
        Platform.runLater(() -> {
            //如果支持系统托盘,就隐藏到托盘,不支持就直接退出
            if (SystemTray.isSupported()) {
                //stage.hide()与stage.close()等价
                stage.hide();
            } else {
                System.exit(0);
            }
        });
    }


}
class SingleWindow{
    private ActionListener showListener;
    private ActionListener exitListener;
    public  java.awt.MenuItem showItem;
    public java.awt.MenuItem exitItem;
    public static SingleWindow[] singleWindows;

    public MouseAdapter mouseListener;
    static {
        singleWindows = new SingleWindow[4];
        singleWindows[0]=new SingleWindow("Config");
        singleWindows[1]=new SingleWindow("WanderingDDL");
        singleWindows[2]=new SingleWindow("ToDoList");
        singleWindows[3]=new SingleWindow("MuYu");
    }

    public void setStage(Stage stage) {
        showListener = e -> Platform.runLater(() -> showStage(stage));
        showItem.addActionListener(showListener);
        exitItem.addActionListener(exitListener);
    }
    public void setMainStage(Stage[] stages) {
        mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //鼠标左键
                if (e.getButton() == MouseEvent.BUTTON1) {
                    showStage(stages[0]);
                }
            }
        };
        exitListener = e -> {
            if(stages[1].isShowing()) {
                saveWandering();
            }
            if(stages[2].isShowing()) {
                saveToDo();
            }
            if(stages[3].isShowing()) {
                saveMerit();
            }
            MinWindow.closeWindow(this);
            System.exit(0);
        };
        exitItem.addActionListener(exitListener);
    }
    public void setToDoStage(Stage stage) {
        exitListener = e -> {
            saveToDo();
            MinWindow.closeWindow(this);
        };
        setStage(stage);
    }
    public void setWanderingStage(Stage stage) {
        exitListener = e -> {
            saveWandering();
            MinWindow.closeWindow(this);
        };
        setStage(stage);
    }
    public void setMuYuStage(Stage stage) {
        exitListener = e -> {
            saveMerit();
            MinWindow.closeWindow(this);
        };
        setStage(stage);
    }

    private void saveWandering(){
       WanderingController.getInstance().saveData();
        Platform.runLater(() ->{
            ApplicationController.reloadPage(0);
            WanderingController.getInstance().closeStage();
        });
    }
    private void saveToDo(){
        ToDoListController.getInstance().saveData();
        Platform.runLater(() ->{
            ApplicationController.reloadPage(1);
            ToDoListController.getInstance().closeStage();
        });
    }
    private void saveMerit(){
        Platform.runLater(() ->{
            MuYuController.getInstance().closeStage();
            System.out.println(111);
        });
        SaverAndLoader.tool.saveMuYuMerit(MuYuController.getInstance().count);

    }
    /**
     * 显示界面(并且显示在最前面,将最小化的状态设为false)
     */
    private void showStage(Stage stage){
        //点击系统托盘,
        Platform.runLater(() -> {
            if(stage.isIconified()){ stage.setIconified(false);}
            if(!stage.isShowing()){ stage.show(); }
            stage.toFront();
        });
    }
    private SingleWindow(String name) {
        super();
        if(name.equals("Config")){
            exitItem = new MenuItem("exit");
        }else {
            showItem = new MenuItem("Show "+name);
            exitItem = new MenuItem("Close "+name);
        }
        showListener = e -> Platform.runLater(() -> {
        });
        exitListener = e -> {};
    }

}