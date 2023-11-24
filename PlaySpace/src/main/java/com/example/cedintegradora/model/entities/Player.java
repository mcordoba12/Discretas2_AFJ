package com.example.cedintegradora.model.entities;


import com.example.cedintegradora.HelloController;
import com.example.cedintegradora.model.drawing.MapNode;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;


public class Player extends Avatar implements Runnable {
    private boolean keyA;
    private boolean keyW;
    private boolean keyS;
    private boolean keyD;
    private boolean keyE;

    private Image image = new Image("file:src/main/resources/com/example/cedintegradora/images/astronauta.gif");

    private Image[] idle;
    private Image[] run;

    private Image[] died;
    private int frame = 0;

    private boolean isFacingRight = true;

    private boolean isShooting = false;

    private MapNode mapNodeAssociated;
    public void setFacingRight(boolean facingRight) {
        isFacingRight = facingRight;
    }

    public Player(double x, double y, double width, double height, double life) {
        super(x, y, width, height, life);

    }

    @Override
    public void draw(GraphicsContext gc) {
        if (life < 1) {
            gc.drawImage(died[frame], isFacingRight ? position.getX() - (width / 2) : position.getX() + (width / 2), position.getY() - (width / 2), isFacingRight ? width : -width, height);
            return;
        }
        //hitBox.refreshHitBox(position.getX() - (width / 2), position.getY() - (height / 2), position.getX() + (width / 2), position.getY() + (height / 2));
        //gc.strokeRect(hitBox.getX0(), hitBox.getY0(), width, height);
        gc.drawImage(image, isFacingRight ? position.getX() - (width / 2) : position.getX() + (width / 2), position.getY() - (width / 2), isFacingRight ? width : -width, height);
    }

    @Override
    public void run() {
        while (true) {
            if ( life<1 ){
                frame = 0;
                if ( frame != 2 ){
                    frame = (frame + 1) % 3;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                frame = (frame + 1) % 6;
            }try {
                Thread.sleep(80);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void movement(){
        if ( keyA ){
            hitBox.refreshHitBox((position.getX()-3)-(width/2), position.getY()-(height/2), (position.getX()-3)+(width/2), position.getY()+(height/2));
            if ( colission() || HelloController.getGameMap().mapCollision(this.hitBox)
                    || HelloController.getGameMap().mapLimit(hitBox)) return;
            position.setX(position.getX()-3);
        }
        if ( keyW ){
            hitBox.refreshHitBox(position.getX()-(width/2), position.getY()-3-(height/2), position.getX()+(width/2), position.getY()-3+(height/2));
            if ( colission() || HelloController.getGameMap().mapCollision(this.hitBox)
                    || HelloController.getGameMap().mapLimit(hitBox)) return;
            position.setY(position.getY()-3);
        }
        if ( keyS ){
            hitBox.refreshHitBox(position.getX()-(width/2), position.getY()+3-(height/2), position.getX()+(width/2), position.getY()+3+(height/2));
            if ( colission() || HelloController.getGameMap().mapCollision(this.hitBox)
                    || HelloController.getGameMap().mapLimit(hitBox)) return;
            position.setY(position.getY()+3);
        }
        if ( keyD ){
            hitBox.refreshHitBox((position.getX()+3)-(width/2), position.getY()-(height/2), (position.getX()+3)+(width/2), position.getY()+(height/2));
            if ( colission() || HelloController.getGameMap().mapCollision(this.hitBox)
                    || HelloController.getGameMap().mapLimit(hitBox)) return;
            position.setX(position.getX()+3);
        }
        hitBox.refreshHitBox(position.getX(), position.getY(), position.getX(), position.getY());
    }

    private boolean colission() {
        if (hitBox.comparePosition(HelloController.finalBoss.getHitBox())) {
            hitBox.refreshHitBox(position.getX()-(width/2), position.getY()-(height/2), position.getX()+(width/2), position.getY()+(height/2));
            return true;
        }
        return false;
    }



    public void pressKey(KeyEvent event){
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

    public void  releasedKey(KeyEvent event){
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

    public void associateNearestMapNode(){
        this.mapNodeAssociated = HelloController.gameMap.associateMapNode(position.getX(), position.getY());
    }

    public boolean isMoving() {
        return keyA || keyW || keyS || keyD;
    }

    public MapNode getMapNodeAssociated() {
        return mapNodeAssociated;
    }

    public void setMapNodeAssociated(MapNode mapNodeAssociated) {
        this.mapNodeAssociated = mapNodeAssociated;
    }
}
