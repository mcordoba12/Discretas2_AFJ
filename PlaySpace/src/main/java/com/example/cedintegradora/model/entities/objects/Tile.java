package com.example.cedintegradora.model.entities.objects;


import com.example.cedintegradora.model.drawing.IDrawable;
import com.example.cedintegradora.model.drawing.Box;
import com.example.cedintegradora.model.drawing.Vector;
import javafx.scene.canvas.GraphicsContext;

public abstract class Tile implements IDrawable {

    private static final double WIDTH_SIZE = 80;
    private static final double HEIGHT_SIZE = 80;

    protected Box box;
    protected double width;
    protected double height;
    protected Vector position = new Vector(0, 0);
    public Tile(double x, double y) {

        this.width = WIDTH_SIZE;
        this.height = HEIGHT_SIZE;
        this.position.setX(x);
        this.position.setY(y);
        this.box = new Box(x-(width/2), y-(height/2), x+(width/2), y+(height/2));
    }

    public Tile() {
    }

    @Override
    public void draw(GraphicsContext gc) {

    }

    public Box getHitBox() {
        return box;
    }

    public void setHitBox(Box box) {
        this.box = box;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Vector getPosition() {
        return position;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }
}
