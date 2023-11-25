package com.example.cedintegradora.model.entities.objects.functional;

import com.example.cedintegradora.model.drawing.Vector;
import com.example.cedintegradora.model.entities.Avatar;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Bullet extends Avatar {

    private Vector dir;
    private double damage;
    /**
     * Parameterized constructor for Bullet.
     *
     * @param x      The x-coordinate of the bullet.
     * @param y      The y-coordinate of the bullet.
     * @param width  The width of the bullet.
     * @param height The height of the bullet.
     * @param life   The life points of the bullet.
     * @param dir    The direction vector of the bullet.
     * @param damage The damage inflicted by the bullet.
     */
    public Bullet(double x, double y, double width, double height, double life, Vector dir, double damage) {
        super(x, y, width, height, life);
        this.dir = dir;
        this.damage = damage;
    }

    /**
     * Draws the bullet on the canvas using the provided GraphicsContext.
     *
     * @param gc The GraphicsContext used for drawing on the canvas.
     */
    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.GRAY);
        gc.fillOval(position.getX(), position.getY(), 10, 10);

        position.setX(position.getX() + dir.getX());
        position.setY(position.getY() + dir.getY());
        box.refreshHitBox(position.getX() - (width / 2), position.getY() - (height / 2),
                position.getX() + (width / 2), position.getY() + (height / 2));
    }

    /**
     * Checks if the bullet is outside the given height and width boundaries.
     *
     * @param height The maximum height boundary.
     * @param width  The maximum width boundary.
     * @return True if the bullet is outside the boundaries, false otherwise.
     */
    public boolean outside(double height, double width) {
        return position.getX() > width || position.getX() < 0 || position.getY() > height || position.getY() < 0;
    }

    /**
     * Inflicts damage to the player if the bullet's hitbox overlaps with the player's hitbox.
     *
     * @param avatar The player object to check for collision.
     * @return True if damage is inflicted, false otherwise.
     */
    public boolean giveDamage(Avatar avatar) {
        if (box.comparePosition(avatar.getHitBox())) {
            System.out.println("hit");
            avatar.setLife(avatar.getLife() - this.damage);
            System.out.println(avatar.getLife() - this.damage + " life" + avatar.getLife() + " damage" + this.damage);
            return true;
        }
        return false;
    }
}
