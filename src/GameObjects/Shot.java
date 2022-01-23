import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import biuoop.DrawSurface;
/**
* @author Elad Jedwabe <jed.el.me@gmail.com>
*/
public class Shot implements Sprite, HitNotifier {
   private List<HitListener> hitListeners;
   private Point location;
   private int size;
   private Color color;
   private double vel;
   private GameEnvironment environment;
   /**
    * Constructor for Shot, using a given location point.
    *
    * @param center for Shot location.
    * @param r for Shot size.
    * @param v for Shot speed and direction (+/-).
    * @param color for Shot color.
    */
   public Shot(Point center, int r, double v, Color color) {
      this.location = center;
      this.size = r;
      this.color = color;
      this.vel = v;
      this.environment = null;
      this.hitListeners = new ArrayList<HitListener>();
   }
   /**
   * Constructor for Shot, using given location values (needed for the test).
   *
   * @param x for x value of the Shot location.
   * @param y for y value of the Shot location.
   * @param r for Shot size.
   * @param v for Shot speed and direction (+/-).
   * @param color for Shot color.
   */
   public Shot(int x, int y, int r, double v, Color color) {
      this.location = new Point(x, y);
      this.size = r;
      this.color = color;
      this.vel = v;
      this.environment = null;
      this.hitListeners = new ArrayList<HitListener>();
   }
   /**
   * The method is in charge of setting environment.
   *
   * @param envi for the Shot environment.
   */
   public void setEnvironment(GameEnvironment envi) {
      this.environment = envi;
   }
   /**
   * The method is in charge of adding a given HitListener.
   *
   * @param hl the HitListener.
   */
   public void addHitListener(HitListener hl) {
       this.hitListeners.add(hl);
   }
   /**
   * The method is in charge of removing a given HitListener.
   *
   * @param hl the HitListener.
   */
   public void removeHitListener(HitListener hl) {
       this.hitListeners.remove(hl);
   }
   // accessors
   /**
   * The method is in charge of finding x value of the ball location.
   *
   * @return x value.
   */
   public int getX() {
      return (int) this.location.getX();
   }
   /**
   * The method is in charge of finding y value of the ball location.
   *
   * @return y value.
   */
   public int getY() {
      return (int) this.location.getY();
   }
   /**
   * The method is in charge of finding the ball size.
   *
   * @return size value.
   */
   public int getSize() {
      return this.size;
   }
   /**
   * The method is in charge of finding the ball color.
   *
   * @return color value.
   */
   public Color getColor() {
      return this.color;
   }
   /**
   * The method is in charge of finding the ball velocity.
   *
   * @return velocity value.
   */
   public double getVelocity() {
      return this.vel;
   }
   /**
   * The method is in charge of drawing the ball on the given DrawSurface.
   *
   * @param surface a surface to draw on.
   */
   public void drawOn(DrawSurface surface) {
      // Get the ball location values
      int x = getX(), y = getY();
      surface.setColor(this.color);
      // Draw the ball
      surface.fillCircle(x, y, this.size);
      surface.setColor(java.awt.Color.BLACK);
      surface.drawCircle(x, y, this.size);
   }
   /**
   * The method is in charge of changing the ball location, using the velocity values.
   * Note: check if the ball's environment for a hit, and changes it's velocity.
   *
   * @param dt the amount of seconds passed since the last call.
   */
   public void timePassed(double dt) {
       double x = this.location.getX(), y = this.location.getY() + this.vel;
       Point next = new Point(x, y);
       Line move = new Line(this.location, next);
       // check for a hit
       CollisionInfo col = this.environment.getClosestCollision(move);
       if (col.collisionPoint() == null) {
          this.location = next;
       } else {
           col.collisionObject().hit(this);
       }
   }
   /**
   * The method is in charge of adding the ball to the game.
   *
   * @param game the game.
   */
   public void addToGame(GameLevel game) {
      game.addSprite(this);
   }
   /**
   * The method is in charge of removing the ball from the game.
   *
   * @param game the game.
   */
   public void removeFromGame(GameLevel game) {
      game.removeSprite(this);
      game.removeShot(this);
   }
   /**
   * The method is in charge of notifying the Ball's Listeners that it's being removed.
   *
   * @param beingHit the object that being hit to remove the Ball.
   */
   public void notifyDeath(Block beingHit) {
       // Make a copy of the hitListeners before iterating over them.
       List<HitListener> listeners = new ArrayList<HitListener>(this.hitListeners);
       // Notify all listeners about a hit event:
       for (HitListener hl : listeners) {
          hl.hitEvent(beingHit, this);
       }
   }
}