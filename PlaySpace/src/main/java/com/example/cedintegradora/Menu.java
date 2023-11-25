package com.example.cedintegradora;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Menu implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField name;

    @FXML
    private Button startButton;


    @FXML
    private CheckBox matriz;

    @FXML
    private CheckBox lista;


    @FXML
    void start() {
        String username = name.getText();

        if (!username.isBlank()) {

            User user = User.getInstance();

            user.setName(username);

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
                HelloApplication.hideWindow((Stage) startButton.getScene().getWindow());
                HelloApplication.showWindow("game", 1200, 720);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            name.setText("Please enter a username");
        }
    }

    // This method is called when the FXML file is loaded
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public CheckBox getMatriz() {
        return matriz;
    }

    public void setMatriz(CheckBox matriz) {
        this.matriz = matriz;
    }


    public CheckBox getLista() {
        return lista;
    }

    public void setLista(CheckBox lista) {
        this.lista = lista;
    }
}
