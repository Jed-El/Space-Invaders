/**
* @author Elad Jedwabe <jed.el.me@gmail.com>
*/
public interface Shooter {
   /**
   * This method is in charge of creating a new shot.
   * Note: if Shooter can not shoot, return null.
   *
   * @return a new Shot.
   */
   Shot shoot();
}