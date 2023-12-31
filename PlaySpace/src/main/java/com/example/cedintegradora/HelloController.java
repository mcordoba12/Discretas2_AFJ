package com.example.cedintegradora;

import com.example.cedintegradora.User;
import com.example.cedintegradora.model.drawing.GameMap;
import com.example.cedintegradora.model.entities.Avatar;
import com.example.cedintegradora.model.entities.Player;

import com.example.cedintegradora.model.entities.mob.Boss;
import com.example.cedintegradora.model.entities.mob.MobilePump;
import com.example.cedintegradora.model.entities.objects.functional.Bullet;
import com.example.cedintegradora.model.entities.objects.functional.PressurePlate;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class HelloController implements Initializable, Runnable{

    private final Image image = new Image("file:src/main/resources/images/background.jpg");

    @FXML
    private Canvas canvas;

    @FXML
    private Label label;

    private GraphicsContext gc;
    public static Player character;

    private static boolean matrixBased;

    private Avatar avatar;

    public static GameMap gameMap = new GameMap(1200,720, 80,3);
    public static Boss finalBoss;
    public static CopyOnWriteArrayList<Bullet> bullets = new CopyOnWriteArrayList<>();
    private final Cursor customCursor = new ImageCursor(new Image("file:src/main/resources/images/Cursor/nt_normal.png"));
    public static CopyOnWriteArrayList<PressurePlate> pressurePlates = new CopyOnWriteArrayList<>();

    public static CopyOnWriteArrayList<MobilePump> mobilePumps = new CopyOnWriteArrayList<>();


    /**
     * Initializes the game environment.
     *
     * This method is automatically called when the associated FXML file is loaded.
     * It sets up the game by displaying a welcome message, prompting the user to choose
     * the type of graph representation, initializing the game map, creating obstacles,
     * establishing the graph map representation, initializing the player character,
     * setting up the canvas and event handlers, starting threads for player and final boss,
     * creating pressure plates, drawing the initial game state, and updating the label.
     *
     * @param location  The location used to resolve relative paths for the root object,
     *                  or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the
     *                  root object was not localized.
     */
    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        // Display welcome message and prompt for graph type
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bienvenido al juego!");
        alert.setHeaderText("Hola " + User.getInstance().getName() + "!\n" + "Debes llegar al planeta con las naves");
        alert.setContentText("Escoge el tipo de grafo:");
        ButtonType okButton = new ButtonType("Lista de adyacencia");
        ButtonType cancelButton = new ButtonType("Matriz de adyacencia");
        alert.getButtonTypes().setAll(okButton, cancelButton);

        // Get user's choice for graph type
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == okButton) {
            setMatrixBased(false);
        } else {
            setMatrixBased(true);
        }

        // Initialize game map
        gameMap.initialFillingOfMapWithNodesAndCoordinates();
        gameMap.creatingNotNavigableObstacles();

        // Establish graph map representation based on the chosen graph type
        if (isMatrixBased()) {
            gameMap.establishMatrixGraphMapRepresentationForMinimumPaths();
        } else {
            gameMap.establishGraphMapRepresentationForMinimumPaths();
        }

        // Initialize player character
        for (int i = 0; i < gameMap.getMapGuide().get(0).size(); i++) {
            if (gameMap.getMapGuide().get(0).get(i).isNavigable()) {
                character = new Player(gameMap.getMapGuide().get(0).get(i).getPosition().getX(),
                        gameMap.getMapGuide().get(0).get(i).getPosition().getY(), 60, 60, 20000);
                break;
            }
        }

        // Set up canvas and event handlers
        canvas.setCursor(customCursor);
        gc = canvas.getGraphicsContext2D();
        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(character::pressKey);
        canvas.setOnKeyReleased(character::releasedKey);
        finalBoss = new Boss(canvas.getWidth() / 2, canvas.getHeight() / 2, 240, 240, 100);
        canvas.setOnMouseMoved(this::onMouseMoved);

        // Start threads for player, final boss, and game
        new Thread(character).start();
        new Thread(finalBoss).start();
        new Thread(this).start();

        // Create pressure plates
        pressurePlates = gameMap.creatingPressurePlates(pressurePlates, 8);

        // Draw initial game state and update label
        draw();
        escribir();
    }

    /**
     * Updates the label to display the user's name and remaining life points.
     * This method is executed on the JavaFX Application Thread using Platform.runLater()
     * to ensure proper synchronization with the JavaFX UI thread.
     *
     * The label is updated with information about the user's name and the remaining life points
     * of the player character. The displayed text format is "<username> tienes <life points> de vida".
     * This method is typically called to reflect changes in the player's status.
     */
    public void escribir() {
        Platform.runLater(() -> {
            label.setText(User.getInstance().getName() + " tienes " + character.getLife() + " de vida");
        });
    }


    /**
     * The main logic of the game loop that continuously checks and updates game elements.
     * This method is executed within a separate thread.
     * - Checks and removes bullets that are outside the canvas or have hit the player character.
     * - Checks and removes mobile pumps that are outside the canvas or have hit the player character.
     * - Checks the status of pressure plates and updates their state based on player interaction.
     * - Checks if all pressure plates are pressed and, if so, determines the proximity to the final boss.
     *   - If the player is in close proximity to the boss, the player wins the game.
     *     The player wins event triggers the `playerWins` method.
     * - The loop continues to run indefinitely until manually stopped.
     *
     * Note: Adjust the proximityRange value as needed to define the range for winning conditions.
     */
    @Override
    public void run() {
        while (true) {
            for (int i = 0; i < bullets.size(); i++) {
                if (bullets.get(i).outside(canvas.getHeight(), canvas.getWidth()) || bullets.get(i).giveDamage(character)) {
                    bullets.remove(bullets.get(i));
                }
            }

            for (int i = 0; i < mobilePumps.size(); i++) {
                if (mobilePumps.get(i).outside(canvas.getHeight(), canvas.getWidth()) || mobilePumps.get(i).giveDamage(character)) {
                    mobilePumps.remove(mobilePumps.get(i));
                }
            }

            for (int i = 0; i < pressurePlates.size(); i++) {
                pressurePlates.get(i).isPressed(character);
            }

            if (allIsPressed()) {
                double proximityRange = 166; // Adjust this value as needed

                double playerX = character.getPosition().getX();
                double playerY = character.getPosition().getY();
                double bossX = finalBoss.getPosition().getX();
                double bossY = finalBoss.getPosition().getY();
                double distance = calculateDistance(playerX, playerY, bossX, bossY);

                System.out.println("Player: (" + playerX + ", " + playerY + ")");
                System.out.println("Boss: (" + bossX + ", " + bossY + ")");
                System.out.println("Distance: " + distance);

                if (distance <= proximityRange) {
                    System.out.println("El jugador ha ganado");
                    try {
                        playerWins();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }


    /**
     * Handles the logic when the player wins the game.
     * - Invoked when the player is in close proximity to the final boss.
     * - Initiates the transition to the exit screen with a victory message.
     * - Pauses the thread for a specific duration to display the victory screen before termination.
     *
     * @throws IOException if an error occurs during the window transition.
     * @throws RuntimeException if an error occurs during the thread sleep.
     */
    private void playerWins() throws IOException {
        Platform.runLater(() -> {
            try {
                // Hide the game window and show the exit screen with a victory message.
                HelloApplication.hideWindow((javafx.stage.Stage) canvas.getScene().getWindow());
                HelloApplication.showWindow("exit", 600, 496);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        try {
            // Pause the thread to display the victory screen before termination.
            Thread.sleep(500000000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if all pressure plates on the map are pressed.
     *
     * @return true if all pressure plates are pressed, false otherwise.
     */
    public boolean allIsPressed() {
        // Check if there are no pressure plates on the map.
        if (pressurePlates.size() == 0) {
            return false;
        }

        // Iterate through all pressure plates and check if each is pressed.
        for (int i = 0; i < pressurePlates.size(); i++) {
            if (!pressurePlates.get(i).isPressed()) {
                return false;
            }
        }

        // All pressure plates are pressed.
        return true;
    }


    /**
     * Draws the game elements on the canvas, including bullets, map, pressure plates, final boss, mobile pumps, and the player character.
     * Continuously updates the display with a delay of 16 milliseconds between frames.
     * Stops the game and displays an exit window when either the final boss or the player character runs out of life.
     */
    public void draw() {
        Thread drawThread = new Thread(() -> {
            AtomicBoolean running = new AtomicBoolean(true);

            while (running.get()) {
                Platform.runLater(() -> {
                    // Clear the canvas
                    gc.setFill(Color.WHITE);
                    gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                    // Draw bullets
                    for (int i = 0; i < bullets.size(); i++) {
                        bullets.get(i).draw(gc);
                    }

                    // Draw the game map
                    for (int i = 0; i < gameMap.getMapGuide().size(); i++) {
                        for (int j = 0; j < gameMap.getMapGuide().get(i).size(); j++) {
                            gameMap.getMapGuide().get(i).get(j).draw(gc);
                        }
                    }

                    // Draw pressure plates
                    for (int i = 0; i < pressurePlates.size(); i++) {
                        gc.setFill(Color.VIOLET);
                        pressurePlates.get(i).draw(gc);
                    }

                    // Draw the final boss
                    finalBoss.draw(gc);

                    // Draw mobile pumps
                    for (int i = 0; i < mobilePumps.size(); i++) {
                        mobilePumps.get(i).draw(gc);
                    }

                    // Draw the player character if alive
                    if (character.getLife() > 0) {
                        character.draw(gc);
                    }

                    // Check for game over conditions
                    if (finalBoss.getLife() < 1 || character.getLife() < 1) {
                        running.set(false);
                    }
                });

                // Update player character movement
                character.movement();

                try {
                    Thread.sleep(16); // Delay between frames
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Display exit window when the game is over
            try {
                HelloApplication.showWindow("exit", 600, 500);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Delay to keep the exit window visible
            try {
                Thread.sleep(500000000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        drawThread.start();
    }

    /**
     * Calculates the Euclidean distance between two points in a 2D plane.
     *
     * @param x1 The x-coordinate of the first point.
     * @param y1 The y-coordinate of the first point.
     * @param x2 The x-coordinate of the second point.
     * @param y2 The y-coordinate of the second point.
     * @return The Euclidean distance between the two points.
     */
    private double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    /**
     * Handles the mouse movement event to determine the player's facing direction.
     *
     * @param e The MouseEvent containing information about the mouse movement.
     */
    private void onMouseMoved(MouseEvent e) {
        // Calculate the relative position of the mouse cursor with respect to the player's position.
        double relativePosition = e.getX() - character.getPosition().getX();

        // Set the player's facing direction based on the relative position.
        character.setFacingRight(relativePosition > 0);
    }


    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public Image getImage() {
        return image;
    }

    public GraphicsContext getGc() {
        return gc;
    }

    public void setGc(GraphicsContext gc) {
        this.gc = gc;
    }

    public static GameMap getGameMap() {
        return gameMap;
    }

    public static void setGameMap(GameMap gameMap) {
        HelloController.gameMap = gameMap;
    }

    public static Boss getFinalBoss() {
        return finalBoss;
    }

    public static void setFinalBoss(Boss finalBoss) {
        HelloController.finalBoss = finalBoss;
    }

    public static CopyOnWriteArrayList<Bullet> getBullets() {
        return bullets;
    }

    public static void setBullets(CopyOnWriteArrayList<Bullet> bullets) {
        HelloController.bullets = bullets;
    }

    public Cursor getCustomCursor() {
        return customCursor;
    }

    public static CopyOnWriteArrayList<PressurePlate> getPressurePlates() {
        return pressurePlates;
    }

    public static void setPressurePlates(CopyOnWriteArrayList<PressurePlate> pressurePlates) {
       HelloController.pressurePlates = pressurePlates;
    }

    public static CopyOnWriteArrayList<MobilePump> getMobilePumps() {
        return mobilePumps;
    }

    public static void setMobilePumps(CopyOnWriteArrayList<MobilePump> mobilePumps) {
        HelloController.mobilePumps = mobilePumps;
    }

    public static Player getCharacter() {
        return character;
    }

    public static void setCharacter(Player character) {
        HelloController.character = character;
    }

    public static boolean isMatrixBased() {
        return matrixBased;
    }

    public static void setMatrixBased(boolean matrixBased) {
        HelloController.matrixBased = matrixBased;
    }
}
