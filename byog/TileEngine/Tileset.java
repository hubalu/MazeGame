package byog.TileEngine;

import java.awt.Color;
import java.io.Serializable;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset implements Serializable {
    public static final TETile PLAYER = new TETile('☃', Color.white, Color.black, "player");
    public static final TETile WALL = new TETile(' ', new Color(124, 127, 151), new Color(124, 127, 151),
            "wall");
    public static final TETile FLOOR = new TETile('·', Color.white, new Color(17, 12, 66),
            "the blue floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, new Color(124, 127, 151),
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");
    public static final TETile BLUELIGHT = new TETile('•', Color.white, new Color(63,73,223), "blue light");
    public static final TETile LIGHTOFF = new TETile('•', Color.white, new Color(17, 12, 66), "red light");
    public static final TETile Blue10 = new TETile('.', Color.white,
            new Color(58, 67, 208), "the tenth blue layer");
    public static final TETile Blue9 = new TETile('.', Color.white,
            new Color(54, 61, 192), "the ninth blue layer");
    public static final TETile Blue8 = new TETile('.', Color.white,
            new Color(49, 55, 177), "the eighth blue layer");
    public static final TETile Blue7 = new TETile('.', Color.white,
            new Color(45, 49, 161), "the seventh blue layer");
    public static final TETile Blue6 = new TETile('.', Color.white,
            new Color(40, 43, 145), "the sixth blue layer");
    public static final TETile Blue5 = new TETile('.', Color.white,
            new Color(35, 36, 129), "the fifth blue layer");
    public static final TETile Blue4 = new TETile('.', Color.white,
            new Color(31, 30, 113), "the fourth blue layer");
    public static final TETile Blue3 = new TETile('.', Color.white,
            new Color(26, 24, 98), "the third blue layer");
    public static final TETile Blue2 = new TETile('.', Color.white,
            new Color(22, 18, 82), "the second blue layer");
    public static final TETile Blue1 = new TETile('.', Color.white,
            new Color(17, 12, 66), "the first blue layer");
}


