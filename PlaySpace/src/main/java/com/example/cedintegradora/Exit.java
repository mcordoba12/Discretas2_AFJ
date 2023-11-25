package com.example.cedintegradora;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Exit {

    @FXML
    private Button startButton;

    /**
     * Handles the action to be taken when exiting the current view.
     *
     * @throws IOException If an error occurs while loading the "hello-view.fxml" file.
     */
    @FXML
    public void onExit() throws IOException {
        // Hide the current window.
        HelloApplication.hideWindow((Stage) startButton.getScene().getWindow());

        // Show the "hello-view" window with the specified dimensions.
        HelloApplication.showWindow("hello-view", 600, 500);
    }

}
