package com.example.cedintegradora.model.drawing;

import com.example.cedintegradora.model.entities.objects.Tile;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class MapNode extends Tile implements IDrawable {

    private Image navigableImage = new Image("file:src/main/resources/com/example/cedintegradora/images/azul.jpg");
    private Image nonNavigableImage = new Image("file:src/main/resources/com/example/cedintegradora/images/galaxia.jpg");
    boolean navigable;

    public MapNode(double x, double y, boolean navigable) {
        super(x, y);
        this.navigable = navigable;
    }
    public MapNode() {
    }
    @Override
    public void draw(GraphicsContext gc) {
        if (isNavigable()) {
            gc.drawImage(navigableImage, position.getX() - (width / 2), position.getY() - (height/2), getWidth(), getHeight());
        }
        else {
            gc.drawImage(nonNavigableImage, position.getX() - (width / 2), position.getY() - (height/2), getWidth(), getHeight());
        }
        gc.strokeRect(hitBox.getX0(), hitBox.getY0(), width, height);
    }

    public boolean isNavigable() {
        return navigable;
    }

    public void setNavigable(boolean navigable) {
        this.navigable = navigable;
    }


}
