package com.example.cedintegradora.model.drawing;

import java.util.Objects;

public class Coordinate implements Comparable<Coordinate> {

    private double x;
    private double y;

    public Coordinate() {
    }

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {

        Coordinate coordinate = (Coordinate) obj;
        return this.x== coordinate.getX() && this.y == coordinate.getY();
    }

    /**
     * Overrides the hashCode method to generate a hash code for Coordinate objects.
     *
     * @return The generated hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Overrides the compareTo method for Comparable interface.
     * Currently, it returns 0, assuming all coordinates are equal.
     *
     * @param o The other Coordinate object to compare.
     * @return 0, indicating equality for the current implementation.
     */
    @Override
    public int compareTo(Coordinate o) {
        return 0;
    }
}
