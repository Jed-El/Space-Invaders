/**
* @author Elad Jedwabe <jed.el.me@gmail.com>
*/
// a BlockRemover is in charge of removing blocks from the game, as well as keeping count
// of the number of blocks that remain.
public class BlockRemover implements HitListener {
   private GameLevel game;
   /**
    * Constructor for BlockRemover.
    *
    * @param game for GameLevel.
    */
   public BlockRemover(GameLevel game) {
       this.game = game;
   }
   /** This method is called whenever the beingHit object is hit.
   * Blocks that are hit and reach 0 hit-points should be removed from the game.
   *
   * @param beingHit is the Block that's being hit.
   * @param hitter is the Ball that's doing the hitting.
   */
   public void hitEvent(Block beingHit, Shot hitter) {
       beingHit.removeHitListener(this);
       beingHit.removeFromGame(this.game);
   }
}
