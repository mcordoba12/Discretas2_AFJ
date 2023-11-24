package com.example.cedintegradora;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Exit {

    @FXML
    private Button startButton;

    @FXML
    public void onExit() throws IOException {
        HelloApplication.hideWindow((Stage) startButton.getScene().getWindow());
        HelloApplication.showWindow("hello-view", 600, 500);

    }
}
