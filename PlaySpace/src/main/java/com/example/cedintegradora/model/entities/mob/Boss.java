package com.example.cedintegradora.model.entities.mob;


import com.example.cedintegradora.HelloController;
import com.example.cedintegradora.model.drawing.Vector;
import com.example.cedintegradora.model.entities.Avatar;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Boss extends Avatar implements Runnable {
    private Image idle;
    private String uri = "file:src/main/resources/com/example/cedintegradora/images/PLANETspace.png";

    /**
     * Parameterized constructor for Boss.
     *
     * @param x      The x-coordinate of the boss.
     * @param y      The y-coordinate of the boss.
     * @param width  The width of the boss.
     * @param height The height of the boss.
     * @param life   The life points of the boss.
     */
    public Boss(double x, double y, double width, double height, double life) {
        super(x, y, width, height, life);
        idle = new Image(uri);  // Assuming uri is a field or constant specifying the image path.
    }

    /**
     * Draws the boss on the canvas using the provided GraphicsContext.
     *
     * @param gc The GraphicsContext used for drawing on the canvas.
     */
    @Override
    public void draw(GraphicsContext gc) {
        gc.strokeRect(box.getxMin(), box.getyMin(), width, height);
        box.refreshHitBox(position.getX() - (width / 2), position.getY() - (height / 2),
                position.getX() + (width / 2), position.getY() + (height / 2));
        gc.drawImage(idle, box.getxMin(), box.getyMin(), width, height);
    }

    /**
     * Implements the run method from the Runnable interface.
     * This method is used to control the boss's behavior in a separate thread.
     */
    @Override
    public void run() {
        while (HelloController.character.getLife() > 0 && this.getLife() > 0) {
            try {
                double diffX = HelloController.character.getPosition().getX() - this.position.getX();
                double diffY = HelloController.character.getPosition().getY() - this.position.getY();
                Vector diff = new Vector(diffX, diffY);
                diff.normalize();
                diff.setMag(10);
                MobilePump mobilePump = new MobilePump(getPosition().getX(), getPosition().getY(), 1, 200);
                HelloController.mobilePumps.add(mobilePump);

                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets the life points of the boss to a negative value, indicating death.
     */
    public void died() {
        life = -1;
    }
}