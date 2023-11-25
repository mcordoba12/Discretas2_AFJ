package com.example.cedintegradora.model.drawing;
public class Box {
    protected double xMin;
    protected double yMin;
    protected double xMax;
    protected double yMax;

    public Box(double xMin, double yMin, double xMax, double yMax) {
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
    }


    public void refreshHitBox(double xMin, double yMin, double xMax, double yMax) {
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
    }


    public double getxMin() {
        return xMin;
    }

    public void setxMin(double xMin) {
        this.xMin = xMin;
    }

    public double getyMin() {
        return yMin;
    }

    public void setyMin(double yMin) {
        this.yMin = yMin;
    }

    public double getxMax() {
        return xMax;
    }

    public void setxMax(double xMax) {
        this.xMax = xMax;
    }

    public double getyMax() {
        return yMax;
    }

    public void setyMax(double yMax) {
        this.yMax = yMax;
    }

    /**
     * Compares the position of the current box with another box.
     *
     * @param obj The Box object to compare positions with.
     * @return True if there is overlap in the horizontal and vertical axes, indicating a collision of positions; otherwise, false.
     */
    public boolean comparePosition(Box obj) {
        // Check if there is overlap in both the horizontal and vertical axes
        if ((this.xMin <= obj.getxMax() && this.xMax >= obj.getxMin()) &&
                (this.yMin <= obj.getyMax() && this.yMax >= obj.getyMin())) {
            return true;
        }

        // Return false if there is no overlap in either axis
        return false;
    }

}
