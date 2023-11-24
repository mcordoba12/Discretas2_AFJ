package com.example.cedintegradora;

import javafx.fxml.FXML;

import java.io.IOException;

public class Exit {

    @FXML
    public void onExit() throws IOException {
        HelloApplication.showWindow("exit", 600, 500);

    }
}
