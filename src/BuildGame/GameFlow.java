import java.io.IOException;
import biuoop.KeyboardSensor;
/**
* @author Elad Jedwabe <jed.el.me@gmail.com>
*/
public class GameFlow {
   private AnimationRunner animationRunner;
   private KeyboardSensor keyboardSensor;
   private Counter lives;
   private Counter score;
   /**
    * Constructor for GameFlow, using a given AnimationRunner, KeyboardSensor and lives number.
    *
    * @param ar an AnimationRunner.
    * @param ks a KeyboardSensor.
    * @param li a value for lives count.
    */
   public GameFlow(AnimationRunner ar, KeyboardSensor ks, int li) {
       this.animationRunner = ar;
       this.keyboardSensor = ks;
       this.lives = new Counter(li);
       this.score = new Counter(0);
   }
   /**
   * The method is in charge of running the game, by starting the animation loop.
   *
   * @return the score value.
   *
   * @throws IOException if could not create the enemy.
   */
   public int runLevels() throws IOException {
      for (int i = 0; this.lives.getValue() > 0; i++) {
         GameLevel level = new GameLevel(i,
               this.keyboardSensor, this.animationRunner,
               this.lives, this.score);
         level.initialize();
         while (!level.levelDone() && (this.lives.getValue() > 0)) {
            level.playOneTurn();
         }
      }
      EndScreen es = new EndScreen(this.lives, this.score);
      KeyPressStoppableAnimation kpsa = new KeyPressStoppableAnimation(
              this.keyboardSensor, KeyboardSensor.SPACE_KEY, es);
      this.animationRunner.run(kpsa);
      return this.score.getValue();
   }
}