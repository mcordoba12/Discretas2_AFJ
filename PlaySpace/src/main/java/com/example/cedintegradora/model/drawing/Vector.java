package com.example.cedintegradora.model.drawing;

public class Vector {
    private double x;
    private double y;

    /**
     * Parameterized constructor for Vector.
     *
     * @param x The x-component of the vector.
     * @param y The y-component of the vector.
     */
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the x-component of the vector.
     *
     * @return The x-component of the vector.
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the x-component of the vector.
     *
     * @param x The new x-component value to be set.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Gets the y-component of the vector.
     *
     * @return The y-component of the vector.
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the y-component of the vector.
     *
     * @param y The new y-component value to be set.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Scales the vector by a given scalar value.
     *
     * @param scalar The scalar value to scale the vector by.
     */
    public void setMag(int scalar) {
        this.x *= scalar;
        this.y *= scalar;
    }

    /**
     * Normalizes the vector, making it a unit vector with magnitude 1.
     */
    public void normalize() {
        double mag = Math.sqrt(x * x + y * y);
        double angle = Math.atan2(y, x);
        this.x = 1 * Math.cos(angle);
        this.y = 1 * Math.sin(angle);
    }
}
