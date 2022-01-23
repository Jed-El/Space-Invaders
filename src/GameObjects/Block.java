import java.util.ArrayList;
import java.util.List;
import biuoop.DrawSurface;
/**
* @author Elad Jedwabe <jed.el.me@gmail.com>
*/
public class Block implements Collidable, Sprite, HitNotifier {
   private List<HitListener> hitListeners;
   private Rectangle rect;
   private DrawTask drow;
   /**
   * Constructor for Block, using a given location, size and appearance values.
   *
   * @param upperLeft for block location.
   * @param width for block width.
   * @param height for block height.
   * @param d for DrawTask.
   */
   public Block(Point upperLeft, double width, double height, DrawTask d) {
      this.rect = new Rectangle(upperLeft, width, height);
      this.drow = d;
      this.hitListeners = new ArrayList<HitListener>();
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
   /**
   * The method is in charge of finding "collision shape" of the object.
   *
   * @return the "collision shape" of the object.
   */
   public Rectangle getCollisionRectangle() {
      return this.rect;
   }
   /**
   * The method is in charge of removing the hitting ball.
   *
   * @param hitter the hitting ball.
   */
   public void hit(Shot hitter) {
       hitter.notifyDeath(this);
       notifyHit(hitter);
   }
   /**
   * The method is in charge of drawing the block on the given DrawSurface.
   *
   * @param surface a surface to draw on.
   */
   public void drawOn(DrawSurface surface) {
       this.drow.draw(this.rect, surface);
   }
   /**
   * The method is in charge of notifying the block that time has passed.
   *
   * @param dt the amount of seconds passed since the last call.
   */
   public void timePassed(double dt) {
   }
   /**
   * The method is in charge of adding the block to the game.
   *
   * @param game the game.
   */
   public void addToGame(GameLevel game) {
      game.addCollidable(this);
      game.addSprite(this);
   }
   /**
   * The method is in charge of removing the block from the game.
   *
   * @param game the game.
   */
   public void removeFromGame(GameLevel game) {
       game.removeCollidable(this);
       game.removeSprite(this);
   }
   /**
   * The method is in charge of notifying about a hit.
   *
   * @param hitter the hitting ball.
   */
   protected void notifyHit(Shot hitter) {
       // Make a copy of the hitListeners before iterating over them.
       List<HitListener> listeners = new ArrayList<HitListener>(this.hitListeners);
       // Notify all listeners about a hit event:
       for (HitListener hl : listeners) {
          hl.hitEvent(this, hitter);
       }
   }
}