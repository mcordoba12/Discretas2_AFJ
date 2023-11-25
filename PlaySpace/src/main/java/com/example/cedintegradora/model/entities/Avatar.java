package com.example.cedintegradora.model.entities;


import com.example.cedintegradora.model.drawing.IDrawable;
import com.example.cedintegradora.model.drawing.Box;
import com.example.cedintegradora.model.drawing.Vector;

public abstract class  Avatar implements IDrawable {
    /**
     * The position vector of the Avatar.
     */
    protected Vector position = new Vector(0, 0);

    /**
     * The bounding box of the Avatar.
     */
    protected Box box;

    /**
     * The width of the Avatar.
     */
    protected double width;

    /**
     * The height of the Avatar.
     */
    protected double height;

    /**
     * The life points of the Avatar.
     */
    protected double life;

    /**
     * Constructor for the Avatar class.
     *
     * @param x      The initial x-coordinate of the Avatar.
     * @param y      The initial y-coordinate of the Avatar.
     * @param width  The width of the Avatar.
     * @param height The height of the Avatar.
     * @param life   The initial life points of the Avatar.
     */
    public Avatar(double x, double y, double width, double height, double life) {
        this.position.setX(x);
        this.position.setY(y);
        this.width = width;
        this.height = height;
        this.box = new Box(x - (width / 2), y - (height / 2), x + (width / 2), y + (height / 2));
        this.life = life;
    }

    /**
     * Gets the position vector of the Avatar.
     *
     * @return The position vector.
     */
    public Vector getPosition() {
        return position;
    }

    /**
     * Sets the position vector of the Avatar.
     *
     * @param position The new position vector.
     */
    public void setPosition(Vector position) {
        this.position = position;
    }

    /**
     * Gets the bounding box of the Avatar.
     *
     * @return The bounding box.
     */
    public Box getHitBox() {
        return box;
    }

    /**
     * Sets the bounding box of the Avatar.
     *
     * @param box The new bounding box.
     */
    public void setHitBox(Box box) {
        this.box = box;
    }

    /**
     * Gets the life points of the Avatar.
     *
     * @return The life points.
     */
    public double getLife() {
        return life;
    }

    /**
     * Gets the width of the Avatar.
     *
     * @return The width.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Sets the width of the Avatar.
     *
     * @param width The new width.
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Gets the height of the Avatar.
     *
     * @return The height.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Sets the height of the Avatar.
     *
     * @param height The new height.
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * Sets the life points of the Avatar.
     *
     * @param life The new life points.
     */
    public void setLife(double life) {
        this.life = life;
    }
}