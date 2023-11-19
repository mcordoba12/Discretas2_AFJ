package com.example.cedintegradora.model.entities.mob;


import com.example.cedintegradora.HelloController;
import com.example.cedintegradora.model.drawing.Vector;
import com.example.cedintegradora.model.entities.Avatar;
import com.example.cedintegradora.model.entities.objects.functional.Bullet;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Boss extends Avatar implements Runnable{
    private Image idle;
    private String uri = "file:src/main/resources/com/example/cedintegradora/images/FinalBoss/planet.png";

    public Boss(double x, double y, double width, double height, double life){

        super(x, y, width, height, life);
        idle = new Image(uri);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.strokeRect(hitBox.getX0(), hitBox.getY0(), width, height);
        hitBox.refreshHitBox(position.getX()-(width/2), position.getY()-(height/2), position.getX()+(width/2), position.getY()+(height/2));
        gc.drawImage(idle, hitBox.getX0(), hitBox.getY0(), width, height);
    }

    @Override
    public void run() {
        while (HelloController.character.getLife()>0 && this.getLife()>0) {
            try {

                    double diffX = HelloController.character.getPosition().getX() - this.position.getX();
                    double diffY = HelloController.character.getPosition().getY() - this.position.getY();
                    Vector diff = new Vector(diffX, diffY);
                    diff.normalize();
                    diff.setMag(10);
                    MobilePump mobilePump = new MobilePump(getPosition().getX(),getPosition().getY(),1,200);
                    HelloController.mobilePumps.add(mobilePump);

                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void died() {
        life = -1;
    }
}