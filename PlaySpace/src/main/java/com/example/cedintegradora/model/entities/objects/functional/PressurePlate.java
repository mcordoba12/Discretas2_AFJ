package com.example.cedintegradora.model.entities.objects.functional;

import com.example.cedintegradora.model.entities.Avatar;
import com.example.cedintegradora.model.entities.objects.Tile;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class PressurePlate extends Tile {

    private Image image = new Image("file:src/main/resources/com/example/cedintegradora/images/naves.gif");

    public boolean isPressed = false;

    public PressurePlate(double x, double y) {
        super(x, y);
    }

    /**
     * Draws the pressure plate on the canvas using the provided GraphicsContext.
     *
     * @param gc The GraphicsContext used for drawing on the canvas.
     */
    @Override
    public void draw(GraphicsContext gc) {
        gc.strokeRect(box.getxMin(), box.getyMin(), width, height);

        if (isPressed) {
            gc.setFill(Color.WHITE);
            gc.fillRect(position.getX() - (width / 2), position.getY() - (height / 2), getWidth(), getHeight());
        } else {
            gc.drawImage(image, position.getX() - (width / 2), position.getY() - (height / 2), getWidth(), getHeight());
        }
    }

    /**
     * Checks if the pressure plate is pressed.
     *
     * @return True if the pressure plate is pressed, false otherwise.
     */
    public boolean isPressed() {
        return isPressed;
    }

    /**
     * Checks if the pressure plate is pressed when colliding with the given avatar.
     *
     * @param avatar The avatar object to check for collision.
     * @return True if the pressure plate is pressed due to the collision, false otherwise.
     */
    public boolean isPressed(Avatar avatar) {
        if (box.comparePosition(avatar.getHitBox())) {
            isPressed = true;
            return true;
        } else {
            return false;
        }
    }
}
