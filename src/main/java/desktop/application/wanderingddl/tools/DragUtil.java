package desktop.application.wanderingddl.tools;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.List;

public class DragUtil {
    public static void addDragListener(Stage stage, Node root) {
        new DragListener(stage).enableDrag(root);
    }

    public static void addDragListener(Stage stage, List<Node> nodeList){
        nodeList.forEach(node -> {
            new DragListener(stage).enableDrag(node);
        });
    }

    static class DragListener implements EventHandler<MouseEvent> {
        private double xOffset = 0;
        private double yOffset = 0;
        private final Stage stage;

        public DragListener(Stage stage) {
            this.stage = stage;
        }

        @Override
        public void handle(MouseEvent event) {
            event.consume();
            if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                stage.setX(event.getScreenX() - xOffset);
                if(event.getScreenY() - yOffset < 0) {
                    stage.setY(0);
                }else {
                    stage.setY(event.getScreenY() - yOffset);
                }
            }
        }

        public void enableDrag(Node node) {
            node.setOnMousePressed(this);
            node.setOnMouseDragged(this);
        }
    }
}


