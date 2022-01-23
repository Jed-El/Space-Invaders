/**
* @author Elad Jedwabe <jed.el.me@gmail.com>
*/
public class ShotRemover implements HitListener {
   private GameLevel game;
   /**
    * Constructor for ShotCreaterRemover.
    *
    * @param game for GameLevel.
    */
   public ShotRemover(GameLevel game) {
        this.game = game;
   }
   /** This method is called whenever the beingHit object is hit.
   * Balls that hitting the bottom of the SurfaceBorders should be removed from the game.
   *
   * @param beingHit is the Block that's being hit.
   * @param hitter is the Ball that's doing the hitting.
   */
   public void hitEvent(Block beingHit, Shot hitter) {
      hitter.removeFromGame(this.game);
   }
}