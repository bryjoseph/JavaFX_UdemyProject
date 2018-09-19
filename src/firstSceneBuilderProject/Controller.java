package firstSceneBuilderProject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Controller {

    @FXML
    private Label okLabel;

    @FXML
    public void handleLabel() {
        okLabel.setText("OK Button Pressed");
    }
}
