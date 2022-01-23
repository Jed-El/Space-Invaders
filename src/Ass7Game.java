import java.io.IOException;
import biuoop.DialogManager;
import biuoop.GUI;
import biuoop.KeyboardSensor;
/**
* @author Elad Jedwabe <jed.el.me@gmail.com>
*/
public class Ass7Game {
   /**
   * This is the main function. It's runs the game.
   *
   * @param args used to run a given levels set.
   */
   public static void main(String[] args) {
      GUI gui = new GUI("title", 800, 600);
      AnimationRunner animationRunner = new AnimationRunner(gui);
      KeyboardSensor keyboardSensor = gui.getKeyboardSensor();
      DialogManager dialog = gui.getDialogManager();
      ShowHiScoresTask showHSTask = new ShowHiScoresTask(animationRunner, dialog, keyboardSensor);
      // Creating the main menu
      Menu<Task<Void>> menu = new MenuAnimation<Task<Void>>("Space Invaders", keyboardSensor);
      menu.addSelection("s", "start a new game", new Task<Void>() {
          // the task that run the levels
          public Void run() {
              GameFlow game = new GameFlow(animationRunner, keyboardSensor, 3);
              int score;
            try {
                score = game.runLevels();
                showHSTask.addToTable(score);
                showHSTask.run();
            } catch (IOException e) {
                System.out.println(e);
            }
              return null;
          }
      });
      menu.addSelection("h", "see the high scores", showHSTask);
      menu.addSelection("q", "quit", new Task<Void>() {
          public Void run() {
             gui.close();
             System.exit(0);
             return null;
           } });
      while (true) {
          menu.setMenu();
          animationRunner.run(menu);
          Task<Void> status = menu.getStatus();
          status.run();
      }
   }
}