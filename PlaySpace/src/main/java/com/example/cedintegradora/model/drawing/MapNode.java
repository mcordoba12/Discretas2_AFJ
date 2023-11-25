package com.example.cedintegradora.model.drawing;

import com.example.cedintegradora.model.entities.objects.Tile;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class MapNode extends Tile implements IDrawable {

    private Image navigableImage = new Image("file:src/main/resources/com/example/cedintegradora/images/azul.jpg");
    private Image nonNavigableImage = new Image("file:src/main/resources/com/example/cedintegradora/images/galaxia.jpg");
    boolean navigable;

    /**
     * Parameterized constructor for MapNode.
     *
     * @param x          The x-coordinate value of the node.
     * @param y          The y-coordinate value of the node.
     * @param navigable  The navigability status of the node.
     */
    public MapNode(double x, double y, boolean navigable) {
        super(x, y);
        this.navigable = navigable;
    }

    /**
     * Default constructor for MapNode.
     */
    public MapNode() {
    }

    /**
     * Draws the MapNode on the canvas using the provided GraphicsContext.
     *
     * @param gc The GraphicsContext used for drawing on the canvas.
     */
    @Override
    public void draw(GraphicsContext gc) {
        if (isNavigable()) {
            gc.drawImage(navigableImage, position.getX() - (width / 2), position.getY() - (height/2), getWidth(), getHeight());
        } else {
            gc.drawImage(nonNavigableImage, position.getX() - (width / 2), position.getY() - (height/2), getWidth(), getHeight());
        }
        gc.strokeRect(box.getxMin(), box.getyMin(), width, height);
    }

    /**
     * Checks if the node is navigable.
     *
     * @return True if the node is navigable, false otherwise.
     */
    public boolean isNavigable() {
        return navigable;
    }

    /**
     * Sets the navigability status of the node.
     *
     * @param navigable The new navigability status to be set for the node.
     */
    public void setNavigable(boolean navigable) {
        this.navigable = navigable;
    }
}


