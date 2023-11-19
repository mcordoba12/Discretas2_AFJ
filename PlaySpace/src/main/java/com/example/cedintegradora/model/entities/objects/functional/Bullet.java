package com.example.cedintegradora.model.entities.objects.functional;

import com.example.cedintegradora.model.drawing.Vector;
import com.example.cedintegradora.model.entities.Avatar;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Bullet extends Avatar {

    private Vector dir;
    private double damage;
    public Bullet(double x, double y, double width, double height, double life, Vector dir, double damage) {
        super(x, y, width, height, life);
        this.dir = dir;
        this.damage = damage;
    }


    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.GRAY);
        gc.fillOval(position.getX(), position.getY(), 10,10);

        position.setX( position.getX() + dir.getX() );
        position.setY( position.getY() + dir.getY() );
        hitBox.refreshHitBox(position.getX()-(width/2), position.getY()-(height/2), position.getX()+(width/2), position.getY()+(height/2));
    }

    public boolean outside(double height, double width) {
        return position.getX() > width || position.getX() < 0 || position.getY() > height || position.getY() < 0;
    }

    public boolean giveDamage(Avatar avatar){
        if(hitBox.comparePosition(avatar.getHitBox())){
            System.out.println("hit");
            avatar.setLife(avatar.getLife()-this.damage);
            System.out.println(avatar.getLife()-this.damage + " life" + avatar.getLife() + " damage" + this.damage);
            return true;
        }
        return false;
    }
}
