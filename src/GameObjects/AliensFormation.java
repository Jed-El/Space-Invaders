import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import javax.imageio.ImageIO;
import biuoop.DrawSurface;
/**
* @author Elad Jedwabe <jed.el.me@gmail.com>
*/
public class AliensFormation implements Sprite, HitListener, Shooter {
   private Block[][] form;
   private double seconds;
   private int speed;
   private double x;
   private double y;
   private int num;
   private GameLevel game;
   private boolean reacheShields;
   /**
    * Constructor for AliensFormation, using a given GameLevel and speed value.
    *
    * @param sp a speed value.
    * @param g a GameLevel.
    *
    * @throws IOException if the enemy image could not be read.
    */
   public AliensFormation(int sp, GameLevel g) throws IOException {
       this.speed = sp;
       this.seconds = 0.5;
       this.game = g;
       this.x = 5;
       this.y = 30;
       this.reacheShields = false;
       Block[][] f = new Block[10][5];
       double dx = 0, dy = 0;
       // get the enemy DrawImage task
       InputStream source = ClassLoader.getSystemClassLoader().getResourceAsStream("enemy.png");
       DrawImage dr = new DrawImage(ImageIO.read(source));
       // create all the enemies
       for (int c = 0; c < 10; c++) {
          for (int r = 0; r < 5; r++) {
             Point p = new Point(this.x + dx, y + dy);
             f[c][r] = new Block(p, 40, 30, dr);
             f[c][r].addHitListener(this);
             g.addCollidable(f[c][r]);
             dy = dy + 40;
          }
          dy = 0;
          dx = dx + 50;
       }
       this.num = 10 * 5;
       this.form = f;
       g.addSprite(this);
   }
   /**
   * The method is in charge of finding the remaining enemies number.
   *
   * @return the enemies number.
   */
   public int getEnemyNum() {
       return this.num;
   }
   /**
   * The method is in charge of finding if the enemies form reaches the shields.
   *
   * @return the reacheShields value.
   */
   public boolean reacheShields() {
       return this.reacheShields;
   }
   /**
   * The method is in charge of drawing on the given DrawSurface.
   *
   * @param d a surface to draw on.
   */
   public void drawOn(DrawSurface d) {
      for (Block[] col: this.form) {
          for (Block a: col) {
              if (a != null) {
                 a.drawOn(d);
              }
          }
      }
   }
   /**
   * The method is in charge of finding the most left column.
   *
   * @return the number of the most left column.
   */
   private int findLeftCol() {
       for (int col = 9; col >= 0; col--) {
           for (Block b: this.form[col]) {
               if (b != null) {
                   return col + 1;
               }
           }
       }
       return 0;
   }
   /**
   * The method is in charge of notifying the block that time has passed.
   *
   * @param dt the amount of seconds passed since the last call.
   */
   public void timePassed(double dt) {
      double dx = 0, dy = 0;
      this.seconds = this.seconds - dt;
      // move form
      this.x = this.x + this.speed;
      for (int c = 0; c < 10; c++) {
         for (int r = 0; r < 5; r++) {
            if (this.form[c][r] != null) {
                Point p = new Point(this.x + dx, y + dy);
                this.form[c][r].getCollisionRectangle().setUpperLeft(p);
                // check for borders
                if ((this.x <= 0) || (this.x + dx >= 800)) {
                    this.x = 10;
                    if (this.speed > 0) {
                        this.x = 800 - 50 * findLeftCol();
                    }
                    this.y = this.y + 40;
                    this.speed = this.speed * -1;
                    // start again the loop
                    c = 0;
                    r = -1;
                    dx = 0;
                    dy = 0;
                }
                if (this.y + dy >= 500) {
                    this.reacheShields = true;
                    return;
                }
            }
            dy = dy + 40;
         }
         dy = 0;
         dx = dx + 50;
      }
   }
   /**
   * The method is in charge of resetting the form to new round.
   */
   public void reset() {
       this.reacheShields = false;
       this.x = 10;
       this.y = 30;
       if (this.speed < 0) {
           this.speed = this.speed * -1;
       }
       double dx = 0, dy = 0;
       for (int c = 0; c < 10; c++) {
           for (int r = 0; r < 5; r++) {
              if (this.form[c][r] != null) {
                  Point p = new Point(this.x + dx, y + dy);
                  this.form[c][r].getCollisionRectangle().setUpperLeft(p);
              }
              dy = dy + 40;
           }
           dy = 0;
           dx = dx + 50;
        }
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
           this.seconds = 0.5;
           Block b = null;
           Random rand = new Random(); // create a random-number generator
           int c = rand.nextInt(5);
           // find the lowest enemy is random column
           for (int i = 4; b == null; i--)  {
               b = this.form[c][i];
               // if the column is empty - remove to another
               if (i == 0) {
                   i = 4;
                   c = (c + 2) % 5;
               }
           }
           double shotX = b.getCollisionRectangle().getUpperLeft().getX()
                   + (b.getCollisionRectangle().getWidth() / 2),
                  shotY = b.getCollisionRectangle().getUpperLeft().getY() + 35;
           return new Shot(new Point(shotX, shotY), 5, 20, Color.WHITE);
       }
   }
   /**
   * This method is called whenever the beingHit Block is hit.
   *
   * @param beingHit is the Block that's being hit.
   * @param hitter is the Ball that's doing the hitting.
   */
   public void hitEvent(Block beingHit, Shot hitter) {
       if (hitter.getVelocity() > 0) {
           return;
       }
       this.game.getScore().increase(100);
       this.game.removeCollidable(beingHit);
       for (int c = 0; c < 10; c++) {
           for (int r = 0; r < 5; r++) {
               if (beingHit.equals(this.form[c][r])) {
                   beingHit.removeHitListener(this);
                   this.form[c][r] = null;
                   break;
               }
           }
       }
       this.num = this.num - 1;
   }
}
