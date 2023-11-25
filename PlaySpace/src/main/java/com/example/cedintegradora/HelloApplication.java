package com.example.cedintegradora;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {


    /**
     * Overrides the start method of the Application class to initialize and display the main stage.
     *
     * @param stage The primary stage for the application.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Create an FXMLLoader to load the FXML file.
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

        // Create a Scene by loading the FXML file content.
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);

        // Set the title of the stage.
        stage.setTitle("Hi!");

        // Set the scene for the stage.
        stage.setScene(scene);

        // Show the primary stage.
        stage.show();
    }


    /**
     * Displays a new window with the specified FXML file content and dimensions.
     *
     * @param fxml   The name of the FXML file (without extension) to be loaded.
     * @param width  The width of the new window.
     * @param height The height of the new window.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    public static void showWindow(String fxml, int width, int height) throws IOException {
        // Create an FXMLLoader to load the FXML file.
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxml + ".fxml"));

        // Create a Scene by loading the FXML file content.
        Scene scene = new Scene(fxmlLoader.load(), width, height);

        // Create a new Stage for the window.
        Stage stage = new Stage();

        // Set the scene for the new stage.
        stage.setScene(scene);

        // Show the new window.
        stage.show();
    }

    /**
     * Hides the specified JavaFX stage.
     *
     * @param stage The JavaFX stage to be hidden.
     */
    public static void hideWindow(Stage stage) {
        // Hide the specified stage.
        stage.hide();
    }

    public static void main(String[] args) {
        launch();
    }
}