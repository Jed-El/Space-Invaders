import java.awt.Color;
/**
* @author Elad Jedwabe <jed.el.me@gmail.com>
*/
public class SurfaceBorders extends Block {
    /**
     * Constructor for SurfaceBorders, using a given width and height values.
     *
     * @param width the game's gui width (not the borders width!).
     * @param height the game's gui height (not the borders height!).
     */
   public SurfaceBorders(double width, double height) {
      super(new Point(0, 26), width, height - 26, new DrawRect(Color.BLACK));
   }
   /**
   * The method is in charge of removing the block from the game.
   * Note: SurfaceBorders should not be removed.
   *
   * @param game the game.
   */
   @Override
   public void removeFromGame(GameLevel game) {
   }
}