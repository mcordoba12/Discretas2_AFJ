package com.example.cedintegradora.model.entities;


import com.example.cedintegradora.HelloController;
import com.example.cedintegradora.model.drawing.MapNode;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;


public class Player extends Avatar implements Runnable {
    /**
     * Boolean flag for the 'A' key.
     */
    private boolean keyA;

    /**
     * Boolean flag for the 'W' key.
     */
    private boolean keyW;

    /**
     * Boolean flag for the 'S' key.
     */
    private boolean keyS;

    /**
     * Boolean flag for the 'D' key.
     */
    private boolean keyD;

    /**
     * Boolean flag for the 'E' key.
     */
    private boolean keyE;

    /**
     * Image for the player character.
     */
    private Image image = new Image("file:src/main/resources/com/example/cedintegradora/images/astronauta.gif");

    /**
     * Array of images for idle animation.
     */
    private Image[] idle;

    /**
     * Array of images for running animation.
     */
    private Image[] run;

    /**
     * Array of images for the died animation.
     */
    private Image[] died;

    /**
     * Current frame index for animation.
     */
    private int frame = 0;

    /**
     * Flag indicating whether the player is facing right.
     */
    private boolean isFacingRight = true;

    /**
     * Flag indicating whether the player is shooting.
     */
    private boolean isShooting = false;

    /**
     * The MapNode associated with the player.
     */
    private MapNode mapNodeAssociated;

    /**
     * Constructor for the Player class.
     *
     * @param x      The initial x-coordinate of the player.
     * @param y      The initial y-coordinate of the player.
     * @param width  The width of the player.
     * @param height The height of the player.
     * @param life   The initial life points of the player.
     */
    public Player(double x, double y, double width, double height, double life) {
        super(x, y, width, height, life);
    }

    /**
     * Draws the player on the graphics context.
     *
     * @param gc The graphics context.
     */
    @Override
    public void draw(GraphicsContext gc) {
        if (life < 1) {
            gc.drawImage(died[frame], isFacingRight ? position.getX() - (width / 2) : position.getX() + (width / 2), position.getY() - (width / 2), isFacingRight ? width : -width, height);
            return;
        }
        gc.drawImage(image, isFacingRight ? position.getX() - (width / 2) : position.getX() + (width / 2), position.getY() - (width / 2), isFacingRight ? width : -width, height);
    }

    /**
     * Runs the player animation loop.
     */
    @Override
    public void run() {
        while (true) {
            if (life < 1) {
                frame = 0;
                if (frame != 2) {
                    frame = (frame + 1) % 3;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                frame = (frame + 1) % 6;
            }
            try {
                Thread.sleep(80);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Handles player movement based on key inputs.
     */
    public void movement() {
        if ( keyA ){
            box.refreshHitBox((position.getX()-3)-(width/2), position.getY()-(height/2), (position.getX()-3)+(width/2), position.getY()+(height/2));
            if ( colission() || HelloController.getGameMap().mapCollision(this.box)
                    || HelloController.getGameMap().mapLimit(box)) return;
            position.setX(position.getX()-3);
        }
        if ( keyW ){
            box.refreshHitBox(position.getX()-(width/2), position.getY()-3-(height/2), position.getX()+(width/2), position.getY()-3+(height/2));
            if ( colission() || HelloController.getGameMap().mapCollision(this.box)
                    || HelloController.getGameMap().mapLimit(box)) return;
            position.setY(position.getY()-3);
        }
        if ( keyS ){
            box.refreshHitBox(position.getX()-(width/2), position.getY()+3-(height/2), position.getX()+(width/2), position.getY()+3+(height/2));
            if ( colission() || HelloController.getGameMap().mapCollision(this.box)
                    || HelloController.getGameMap().mapLimit(box)) return;
            position.setY(position.getY()+3);
        }
        if ( keyD ){
            box.refreshHitBox((position.getX()+3)-(width/2), position.getY()-(height/2), (position.getX()+3)+(width/2), position.getY()+(height/2));
            if ( colission() || HelloController.getGameMap().mapCollision(this.box)
                    || HelloController.getGameMap().mapLimit(box)) return;
            position.setX(position.getX()+3);
        }
        box.refreshHitBox(position.getX(), position.getY(), position.getX(), position.getY());
    }

    /**
     * Checks for collision with the final boss.
     *
     * @return True if there is a collision, false otherwise.
     */
    private boolean colission() {
        if (box.comparePosition(HelloController.finalBoss.getHitBox())) {
            box.refreshHitBox(position.getX()-(width/2), position.getY()-(height/2), position.getX()+(width/2), position.getY()+(height/2));
            return true;
        }
        return false;
    }

    /**
     * Processes key press events.
     *
     * @param event The KeyEvent.
     */
    public void pressKey(KeyEvent event) {
        if ( life<1 ){
            keyA = false;
            keyW = false;
            keyS = false;
            keyD = false;
            return;
        }
        switch (event.getCode()) {
            case A -> {
                keyA = true;
            }
            case W -> {
                keyW = true;
            }
            case S -> {
                keyS = true;
            }
            case D -> {
                keyD = true;
            }
        }
    }

    /**
     * Processes key release events.
     *
     * @param event The KeyEvent.
     */
    public void releasedKey(KeyEvent event) {
        if ( life<1 ){
            keyA = false;
            keyW = false;
            keyS = false;
            keyD = false;
            return;
        }
        switch (event.getCode()) {
            case A -> {
                keyA = false;
            }
            case W -> {
                keyW = false;
            }
            case S -> {
                keyS = false;
            }
            case D -> {
                keyD = false;
            }
        }
        movement();
    }

    /**
     * Associates the nearest MapNode with the player.
     */
    public void associateNearestMapNode() {
        this.mapNodeAssociated = HelloController.gameMap.associateMapNode(position.getX(), position.getY());
    }

    /**
     * Checks if the player is currently moving.
     *
     * @return True if the player is moving, false otherwise.
     */
    public boolean isMoving() {
        return keyA || keyW || keyS || keyD;
    }

    /**
     * Gets the MapNode associated with the player.
     *
     * @return The associated MapNode.
     */
    public MapNode getMapNodeAssociated() {
        return mapNodeAssociated;
    }

    /**
     * Sets the MapNode associated with the player.
     *
     * @param mapNodeAssociated The new associated MapNode.
     */
    public void setMapNodeAssociated(MapNode mapNodeAssociated) {
        this.mapNodeAssociated = mapNodeAssociated;
    }

    /**
     * Sets the facing direction of the player.
     *
     * @param facingRight True if facing right, false otherwise.
     */
    public void setFacingRight(boolean facingRight) {
        isFacingRight = facingRight;
    }
}
