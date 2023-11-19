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

    @Override
    public void draw(GraphicsContext gc) {
        gc.strokeRect(hitBox.getX0(), hitBox.getY0(), width, height);

        if ( isPressed ) {
            gc.setFill(Color.WHITE);
            gc.fillRect(position.getX() - (width / 2), position.getY() - (height/2), getWidth(), getHeight());
        } else {
            gc.drawImage(image, position.getX() - (width / 2), position.getY() - (height/2), getWidth(), getHeight());
        }
    }

    public boolean isPressed() {
        return isPressed;
    }

    public boolean isPressed(Avatar avatar) {
        if(hitBox.comparePosition(avatar.getHitBox())){
            isPressed = true;
            return true;
        } else {
            return false;
        }
    }
}
