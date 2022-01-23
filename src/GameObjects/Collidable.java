/**
* @author Elad Jedwabe <jed.el.me@gmail.com>
*/
public interface Collidable {
   /**
   * The method is in charge of finding "collision shape" of the object.
   *
   * @return the "collision shape" of the object.
   */
   Rectangle getCollisionRectangle();
   /**
   * The method is in charge of removing the hitting ball.
   *
   * @param hitter the hitting ball.
   */
   void hit(Shot hitter);
}