package com.example.cedintegradora.model.entities.mob;


import com.example.cedintegradora.HelloController;
import com.example.cedintegradora.model.drawing.Coordinate;
import com.example.cedintegradora.model.drawing.MapNode;
import com.example.cedintegradora.model.drawing.Vector;
import com.example.cedintegradora.model.entities.Avatar;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Stack;

public class MobilePump extends Avatar implements Runnable {

    private static int mobilePumpCounter = 0;
    private Stack<Coordinate> path;
    private Vector direction;

    private double damage;

    private Image image = new Image("file:src/main/resources/com/example/cedintegradora/images/enemy.gif");

    /**
     * Parameterized constructor for MobilePump.
     *
     * @param x      The x-coordinate of the mobile pump.
     * @param y      The y-coordinate of the mobile pump.
     * @param life   The life points of the mobile pump.
     * @param damage The damage inflicted by the mobile pump.
     */
    public MobilePump(double x, double y, double life, double damage) {
        super(x, y, HelloController.gameMap.getNodeSize(), HelloController.gameMap.getNodeSize(), life);

        this.damage = damage;

        MapNode mapNode = HelloController.gameMap.associateMapNode
                (HelloController.character.getPosition().getX(), HelloController.character.getPosition().getY());
        Coordinate to = new Coordinate(mapNode.getPosition().getX(), mapNode.getPosition().getY());

        MapNode mapNodeFrom = HelloController.gameMap.associateMapNode
                (HelloController.finalBoss.getPosition().getX(), HelloController.finalBoss.getPosition().getY());
        Coordinate from = new Coordinate(mapNodeFrom.getPosition().getX(), mapNodeFrom.getPosition().getY());

        if (HelloController.isMatrixBased()){ //Matrix based context.
            // Here we are using BFS

            if(mobilePumpCounter < 20){

                path = HelloController.gameMap.shortestPathUsingMatrixAdjacencyBFS(from, to);

            }else {   // Here we are using DFS

                path = HelloController.gameMap.shortestPathUsingMatrixAdjacencyDFS(from, to);

            }

        }else {  //Adjacency list based context.

            // Here we are using BFS
            if (mobilePumpCounter < 20){

                path = HelloController.gameMap.shortestPathUsingListAdjacencyBFS(from, to);

            }else {    // Here we are using DFS

                path = HelloController.gameMap.shortestPathUsingListAdjacencyDFS(from, to);

            }

        }

        double diffX = path.peek().getX() - this.position.getX();
        double diffY = path.peek().getY() - this.position.getY();
        Vector diff = new Vector(diffX, diffY);
        diff.normalize();
        diff.setMag(5);
        this.direction = diff;

        mobilePumpCounter += 1;
        new Thread(this).start();

    }

    /**
     * Implements the run method from the Runnable interface.
     * This method is used to control the mobile pump's movement in a separate thread.
     */
    @Override
    public void run() {
        while (!path.isEmpty()) {

            Coordinate coordinate = path.peek();
            double pitagoras =
                    Math.sqrt(Math.pow(position.getX() - coordinate.getX(), 2) +
                            Math.pow(position.getY() - coordinate.getY(), 2));
            if (pitagoras < 6) {
                path.pop();
                if (!path.isEmpty()) {
                    coordinate = path.peek();
                    double diffX = coordinate.getX() - this.position.getX();
                    double diffY = coordinate.getY() - this.position.getY();
                    Vector diff = new Vector(diffX, diffY);
                    diff.normalize();
                    diff.setMag(5);
                    this.direction = diff;
                }

            }else{

            }

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Draws the mobile pump on the canvas using the provided GraphicsContext.
     *
     * @param gc The GraphicsContext used for drawing on the canvas.
     */
    @Override
    public void draw(GraphicsContext gc) {
        //gc.setFill(Color.RED);
        //gc.fillOval(hitBox.getX0(), hitBox.getY0(), 80, 80);

        gc.drawImage(image, box.getxMin(), box.getyMin(), width, height);

        position.setX(position.getX() + direction.getX());
        position.setY(position.getY() + direction.getY());
        box.refreshHitBox(position.getX() - (width / 2), position.getY() - (height / 2), position.getX() + (width / 2), position.getY() + (height / 2));
        gc.strokeRect(box.getxMin(), box.getyMin(), width, height);
    }

    /**
     * Checks if the mobile pump is outside the given height and width boundaries.
     *
     * @param height The maximum height boundary.
     * @param width  The maximum width boundary.
     * @return True if the mobile pump is outside the boundaries, false otherwise.
     */
    public boolean outside(double height, double width) {
        return position.getX() > width || position.getX() < 0 || position.getY() > height || position.getY() < 0;
    }
    /**
     * Inflicts damage to the player if the mobile pump's hitbox overlaps with the player's hitbox.
     *
     * @param avatar The player object to check for collision.
     * @return True if damage is inflicted, false otherwise.
     */
    public boolean giveDamage(Avatar avatar) {
        if (box.comparePosition(avatar.getHitBox())) {
            System.out.println("hit");
            avatar.setLife(avatar.getLife() - this.damage);
            return true;
        }
        return false;
    }

    public Stack<Coordinate> getPath() {
        return path;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public void setPath(Stack<Coordinate> path) {
        this.path = path;
    }

    public Vector getDirection() {
        return direction;
    }

    public void setDirection(Vector direction) {
        this.direction = direction;
    }

    public static int getMobilePumpCounter() {
        return mobilePumpCounter;
    }

    public static void setMobilePumpCounter(int mobilePumpCounter) {
        MobilePump.mobilePumpCounter = mobilePumpCounter;
    }
}
