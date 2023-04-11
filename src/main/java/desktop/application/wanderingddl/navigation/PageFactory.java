package desktop.application.wanderingddl.navigation;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class PageFactory {

    private static Node[] pages;

    public static Node createPageService(int index) {
        return pages[index];
    }
    public static void setPages(Node[] pages) {
        PageFactory.pages = pages;
    }
    public static void addNode(int index, Node... nodes) {
        ((Pane)pages[index]).getChildren().addAll(nodes);
    }

}

