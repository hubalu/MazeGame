package byog.Core;

import byog.TileEngine.TETile;

/** This is the main entry point for the game.
 *  Start the program and type in a random number as the seed, then type "s",and the maze will be presented.
 *  The sight of player is limited, and your goal is to find the locked door and leave the maze through it.
 *  If you feel it too difficult, press "t" on the keyboard to see what will happen!
 *  Whenever you pass a blue light, the light will turn off and you receive one score.
 *  Whenever you want to quit and save the game, just type ":q" and choose to "load" the next time you start the game.
 *  Hope you enjoy our game!
 *
 *  @author Rui Ge & Bihao Xu
 *  @source University of California, Berkley CS61B class
 */
public class Main {
    public static void main(String[] args) {
        if (args.length > 1) {
            System.out.println("Can only have one argument - the input string");
            System.exit(0);
        } else if (args.length == 1) {
            Game game = new Game();
            TETile[][] worldState = game.playWithInputString(args[0]);
            System.out.println(TETile.toString(worldState));
        } else {
            Game game = new Game();
            game.playWithKeyboard();
        }
    }
}
