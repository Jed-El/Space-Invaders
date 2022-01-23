import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
/**
* @author Elad Jedwabe <jed.el.me@gmail.com>
*/
public class GameLevel implements Animation {
   private int levelNum;
   private AnimationRunner runner;
   private boolean running;
   private KeyboardSensor keyboard;
   private SpriteCollection sprites;
   private GameEnvironment environment;
   private AliensFormation enemy;
   private Paddle player;
   private List<Shot> shots;
   private Counter score;
   private Counter lives;
   /**
    * Constructor for GameLevel, using a given arguments.
    *
    * @param n the level number.
    * @param keyboardSensor a KeyboardSensor.
    * @param animationRunner an AnimationRunner.
    * @param liveNum a Counter for lives.
    * @param scoreNum a Counter for score.
    */
   public GameLevel(int n,
           KeyboardSensor keyboardSensor,
           AnimationRunner animationRunner,
           Counter liveNum, Counter scoreNum) {
      this.levelNum = n;
      this.keyboard = keyboardSensor;
      this.runner = animationRunner;
      this.lives = liveNum;
      this.score = scoreNum;
      this.environment = new GameEnvironment();
      this.sprites = new SpriteCollection();
      this.running = true;
      this.shots = new ArrayList<Shot>();
   }
   /**
   * The method is in charge of finding the score Counter.
   *
   * @return the score Counter.
   */
   public Counter getScore() {
       return this.score;
   }
   /**
   * The method is in charge of adding a new Collidable object to the list.
   *
   * @param c the new object.
   */
   public void addCollidable(Collidable c) {
      this.environment.addCollidable(c);
   }
   /**
   * The method is in charge of removing a given Collidable object from the list.
   *
   * @param c the given object.
   */
   public void removeCollidable(Collidable c) {
       this.environment.removeCollidable(c);
   }
   /**
   * The method is in charge of adding a new Sprite object to the list.
   *
   * @param s the new object.
   */
   public void addSprite(Sprite s) {
      this.sprites.addSprite(s);
   }
   /**
   * The method is in charge of removing a given Sprite object from the list.
   *
   * @param s the given object.
   */
   public void removeSprite(Sprite s) {
       this.sprites.removeSprite(s);
   }
   /**
   * The method is in charge of removing a given Shot from the list.
   *
   * @param s the given Shot.
   */
   public void removeShot(Shot s) {
       this.shots.remove(s);
       this.sprites.removeSprite(s);
   }
   /**
   * The method is in charge of adding all the blocks.
   */
   private void addBlocks() {
      BlockRemover blockRemover = new BlockRemover(this);
      // a loop that creates all the blocks
      for (int i = 1; i <= 3; i++) {
          for (int c = 0; c < 40; c++) {
              for (int r = 0; r < 4; r++) {
                  Point p = new Point(5 * c + 50 * i + (i - 1) * 200, 5 * r + 500);
                  Block b = new Block(p, 5, 5, new DrawRect(Color.GREEN));
                  b.addHitListener(blockRemover);
                  b.addToGame(this);
              }
          }
      }
   }
   /**
   * The method is in charge of adding a new Shot.
   *
   * @param s the new Shot.
   */
   private void addShot(Shot s) {
      if (s == null) {
          return;
      }
      ShotRemover shotRemover = new ShotRemover(this);
      s.addHitListener(shotRemover);
      s.setEnvironment(this.environment);
      s.addToGame(this);
      shots.add(s);
   }
   /**
   * The method is in charge of setting up the game, by creating objects
   * (like the Blocks, the Ball and the Paddle) and add them to the game.
   *
   * @throws IOException if could not create the enemy.
   */
   public void initialize() throws IOException {
      SurfaceBorders surfaceBorders = new SurfaceBorders(800, 600);
      surfaceBorders.addToGame(this);
      ScoreIndicator scoreIndicat = new ScoreIndicator(this.score);
      LivesIndicator livesIndicat = new LivesIndicator(this.lives);
      LevelIndicator levelIndicat = new LevelIndicator("Battle n." + (this.levelNum + 1));
      this.sprites.addSprite(scoreIndicat);
      this.sprites.addSprite(livesIndicat);
      this.sprites.addSprite(levelIndicat);
      this.player = new Paddle(this.keyboard, 100, 100);
      this.player.addToGame(this);
      addBlocks();
      this.enemy = new AliensFormation(2 + this.levelNum, this);
   }
   /**
   * The method is in charge of checking if the animation should stop.
   *
   * @return true if yes, and false if not.
   */
   public boolean shouldStop() {
      return !this.running;
   }
   /**
   * The method is in charge of checking if the level is finished.
   *
   * @return true if yes, and false if not.
   */
   public boolean levelDone() {
      return (this.enemy.getEnemyNum() <= 0);
   }
   /**
   * The method is in charge of doing one frame of the animation.
   *
   * @param d a surface to draw on.
   * @param dt the amount of seconds passed since the last call.
   */
   public void doOneFrame(DrawSurface d, double dt) {
      this.sprites.notifyAllTimePassed(dt);
      this.sprites.drawAllOn(d);
      addShot(this.enemy.shoot());
      if (this.keyboard.isPressed(KeyboardSensor.SPACE_KEY)) {
          addShot(this.player.shoot());
       }
      if (levelDone()) {
         this.running = false;
         this.score.increase(100);
      }
      if (this.enemy.reacheShields() || this.player.beingHit()) {
          this.running = false;
          this.lives.decrease(1);
      }
      if (this.keyboard.isPressed("p")) {
         KeyPressStoppableAnimation kpsa = new KeyPressStoppableAnimation(
                 this.keyboard, KeyboardSensor.SPACE_KEY, new PauseScreen());
         this.runner.run(kpsa);
      }
   }
   /**
   * The method is in charge of running one turn, by starting the animation loop.
   */
   public void playOneTurn() {
      // reset player and enemy
      this.player.resetPaddle();
      this.enemy.reset();
      // delete all the shots
      if (this.shots.size() > 0) {
          for (int i = 0; i < this.shots.size(); i++) {
              removeShot(this.shots.get(i));
          }
      }
      this.runner.run(new CountdownAnimation(2, 3, this.sprites)); // countdown before turn starts
      this.running = true;
      // use our runner to run the current animation -- which is one turn of
      // the game.
      this.runner.run(this);
   }
}