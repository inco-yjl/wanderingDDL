package desktop.application.wanderingddl.tools;

import javafx.scene.control.Button;

public class ButtonStyleController {
    public static void setPressed(int index,Button... pageButtons){
        for(int i=0;i<pageButtons.length;i++){
            if(i==index)
                pageButtons[i].setStyle("-fx-background-color: #7c9fcc;");
            else
                pageButtons[i].setStyle("-fx-background-color: rgba(176,196,222,1);");
        }
    }
}
