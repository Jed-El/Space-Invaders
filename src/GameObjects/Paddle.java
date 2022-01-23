import java.awt.Color;
import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
/**
* @author Elad Jedwabe <jed.el.me@gmail.com>
*/
public class Paddle extends Block implements Sprite, Collidable, Shooter {
   private double seconds;
   private biuoop.KeyboardSensor keyboard;
   private int speed;
   private boolean beingHit;
   /**
    * Constructor for Paddle, using a given KeyboardSensor.
    *
    * @param kb a KeyboardSensor.
    * @param size for paddle width.
    * @param s for paddle speed.
    */
   public Paddle(biuoop.KeyboardSensor kb, double size, int s) {
      super(new Point((800 - size) / 2, 575), size, 10, new DrawRect(Color.BLUE));
      this.keyboard = kb;
      this.speed = s;
      this.seconds = 0;
      this.beingHit = false;
   }
   /**
   * The method is in charge of reseting the paddle location and size.
   */
   public void resetPaddle() {
       double size = super.getCollisionRectangle().getWidth();
       super.getCollisionRectangle().setUpperLeft(new Point((800 - size) / 2, 575));
       this.seconds = 0;
       this.beingHit = false;
   }
   /**
   * The method is in charge of finding if if the Paddle was hit.
   *
   * @return the beingHit value.
   */
   public boolean beingHit() {
       return this.beingHit;
   }
   /**
   * The method is in charge of moving the paddle to the left.
   *
   * @param dt the amount of seconds passed since the last call.
   */
   public void moveLeft(double dt) {
      double newX = super.getCollisionRectangle().getUpperLeft().getX() - dt * this.speed,
         y = super.getCollisionRectangle().getUpperLeft().getY();
      Point p = new Point(newX, y);
      if (newX >= 0) {
          super.getCollisionRectangle().setUpperLeft(p);
      }
   }
   /**
   * The method is in charge of moving the paddle to the right.
   *
   * @param dt the amount of seconds passed since the last call.
   */
   public void moveRight(double dt) {
      double newX = super.getCollisionRectangle().getUpperLeft().getX() + dt * this.speed,
         y = super.getCollisionRectangle().getUpperLeft().getY();
      Point p = new Point(newX, y);
      if (newX + super.getCollisionRectangle().getWidth() <= 800) {
          super.getCollisionRectangle().setUpperLeft(p);
      }
   }
   // Sprite
   /**
   * The method is in charge of charge of notifying the paddle that time has passed,
   * and changing the paddle location if needed.
   *
   * @param dt the amount of seconds passed since the last call.
   */
   public void timePassed(double dt) {
       this.seconds = this.seconds - dt;
      if (keyboard.isPressed(KeyboardSensor.LEFT_KEY)) {
         moveLeft(dt);
       }
      if (keyboard.isPressed(KeyboardSensor.RIGHT_KEY)) {
         moveRight(dt);
       }
   }
   /**
   * The method is in charge of drawing the paddle on the given DrawSurface.
   *
   * @param surface a surface to draw on.
   */
   public void drawOn(DrawSurface surface) {
      // Get the Paddle location values
      int x = (int) super.getCollisionRectangle().getUpperLeft().getX(),
         y = (int) super.getCollisionRectangle().getUpperLeft().getY(),
         w = (int) super.getCollisionRectangle().getWidth(),
         h = (int) super.getCollisionRectangle().getHeight();
      // Draw the block
      surface.setColor(java.awt.Color.BLUE);
      surface.fillRectangle(x, y, w, h);
      surface.setColor(java.awt.Color.BLACK);
      surface.drawRectangle(x, y, w, h);
   }
   // Collidable
   /**
   * The method is in charge of removing the hitting ball.
   *
   * @param hitter the hitting ball.
   */
   public void hit(Shot hitter) {
       hitter.notifyDeath(this);
       if (hitter.getVelocity() > 0) {
           this.beingHit = true;
       }
   }
   /**
   * The method is in charge of adding the paddle to the game.
   *
   * @param game the game.
   */
   public void addToGame(GameLevel game) {
      game.addCollidable(this);
      game.addSprite(this);
   }
   /**
   * This method is in charge of creating a new shot.
   * Note: if Shooter can not shoot, return null.
   *
   * @return a new Shot.
   */
   public Shot shoot() {
       if (this.seconds > 0) {
           return null;
       } else {
           this.seconds = 0.35;
           double x = super.getCollisionRectangle().getUpperLeft().getX()
                   + (super.getCollisionRectangle().getWidth() / 2),
                  y = super.getCollisionRectangle().getUpperLeft().getY() - 5;
           return new Shot(new Point(x, y), 5, -20, Color.BLUE);
         }
   }
}