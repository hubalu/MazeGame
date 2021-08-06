package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;

public class Game {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private static final int TILE_SIZE = 16;
    WalkingEntity player;
    Deque<Character> playerKeyQueue = new ArrayDeque<>();
    private GameConfig gameConfig;
    private TETile[][] world;
    private TETile[][] playerSightWorld;
    private MapGenerator mapGenerator;
    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        initializeCanvasConfig(WIDTH, HEIGHT);
        createStartPageCanvas();
        onStartPage();

        initializeGameConfig();
        onMainGame();

        onEndPage();
        System.exit(0);
    }
    private void initializeGameConfig() {
        createPlayerSightWorld();
        setGameCanvasTextConfig();
    }
    private void initializeCanvasConfig(int width, int height) {
        StdDraw.setCanvasSize(width * TILE_SIZE, height * TILE_SIZE);
        Font font = new Font("Monaco", Font.BOLD, TILE_SIZE - 2);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);

        StdDraw.clear(new Color(0, 0, 0));

        StdDraw.enableDoubleBuffering();
        StdDraw.show();
    }
    private  void renderFrame(TETile[][] world) {
        int numXTiles = world.length;
        int numYTiles = world[0].length;
        StdDraw.clear(new Color(0, 0, 0));
        displayGameScoreOnTopMid();
        displayTileNameOnTopLeft();
        for (int x = 0; x < numXTiles; x += 1) {
            for (int y = 0; y < numYTiles; y += 1) {
                if (world[x][y] == null) {
                    throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
                            + " is null.");
                }
                world[x][y].draw(x , y);
            }
        }
        StdDraw.show();
    }
    /**
     * execute the action on start page
     */
    private void onStartPage() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (key == 'n') {
                    StdDraw.clear(Color.BLACK);
                    StdDraw.text((double) WIDTH / 2, (double) HEIGHT * 4 / 5, "Please typing seed");
                    StdDraw.show();
                    long seed = parseSeed();
                    mapGenerator = new MapGenerator(seed, WIDTH, HEIGHT - 2);
                    world = mapGenerator.getWorld();
                    player = selectEntity(world, mapGenerator.getDoorPosition());
                    gameConfig = new GameConfig();
                    break;
                } else if (key == 'l') {
                    SavedGame savedGame = load();
                    mapGenerator = savedGame.mapGenerator;
                    world = mapGenerator.getWorld();
                    player = savedGame.player;
                    gameConfig = savedGame.gameConfig;
                    break;
                }
            }
        }
    }
    /**
     * execute the action on main game
     */
    private void onMainGame() {
        while (!gameConfig.gameOver) {
            playerInteractivity();
            if (gameConfig.turnLightOn == true) {
                world[player.getX()][player.getY()] = Tileset.PLAYER;
                renderFrame(world);
            } else {
                playerSightPrismatic();
                playerSightWorld[player.getX()][player.getY()] = Tileset.PLAYER;
                renderFrame(playerSightWorld);
            }
        }
    }
    /**
     * execute the action on end page
     */
    private void onEndPage() {
        setInformationCanvasTextConfig();
        while (gameConfig.gameWin){
            StdDraw.clear(Color.BLACK);
            createEndPageCanvas();
            if(StdDraw.hasNextKeyTyped()){
                if(StdDraw.nextKeyTyped()=='q'){
                    System.exit(0);
                }
            }
        }
    }

    /**
     * create the StartPageCanvas
     */
    private void createStartPageCanvas() {
        setInformationCanvasTextConfig();
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT * 4 / 5, "CS61B: THE GAME");
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2, "NEW GAME (N)");
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 - 3, "LOAD GAME (L)");
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2 - 6, "QUIT (Q)");
        StdDraw.show();

    }

    /**
     * set the canvas text config for page except MainGame page
     */
    private void   setInformationCanvasTextConfig(){
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);

    }

    /**
     * set the canvas text config for MainGame page
     */
    private void setGameCanvasTextConfig() {
        Font font = new Font("Monaco", Font.BOLD, 14);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
    }

    /**
     * create end page
     */
    private void createEndPageCanvas(){
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT /2, "You Win!");
        StdDraw.text((double) WIDTH / 2, (double) HEIGHT /2 - 2, "Your score: " + gameConfig.gameScore);
        StdDraw.show();
    }

    /**
     * create and initialize PlayerSightWorld
     */
    private void createPlayerSightWorld() {
        playerSightWorld = new TETile[WIDTH][HEIGHT - 2];
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT - 2; j++) {
                playerSightWorld[i][j] = Tileset.NOTHING;
            }
        }
    }

    /**
     * enable the player to have a prismatic view
     */
    private void playerSightPrismatic() {
        createPlayerSightWorld();
        Prismatic prismatic = new Prismatic(WalkingEntity.parsePosition(player), 5);
        prismatic.drawPrismatic(world, playerSightWorld);
    }


    /**
     * handle the player interactivity with the game
     * if player type ":q" will save the executing game and exit
     * if player type "t" will switch between the global view and fog view
     * User can move
     */
    private void playerInteractivity() {
        if (StdDraw.hasNextKeyTyped()) {
            char key = StdDraw.nextKeyTyped();
            if (playerKeyQueue.size() > 10) {
                playerKeyQueue.pollFirst();
            }
            if (!playerKeyQueue.isEmpty() && playerKeyQueue.peekLast() == ':' && key == 'q') {
                save();
                gameConfig.gameOver = true;
            }
            if (key == 't') {
                gameConfig.turnLightOn = !gameConfig.turnLightOn;
            }
            playerKeyQueue.add(key);
            movingPlayer(key);
            if(player.getCurrentTile().equals(Tileset.BLUELIGHT)){
                BlueLights blueLight = mapGenerator.getBlueLights().get(WalkingEntity.parsePosition(player));
                blueLight.setLightRed();
                // set the player currentTile to be redTile, so that the map will changed
                player.setCurrentTile(blueLight.light(mapGenerator.getWorld()));
                gameConfig.gameScore++;
            }
            if(checkWin(WalkingEntity.parsePosition(player))){
                gameConfig.gameOver=true;
                gameConfig.gameWin=true;
            }
        }
    }

    private void movingPlayer(char key) {
        Position nextPosition;
        switch (key) {
            case 'w':
                nextPosition = new Position(player.getX(), player.getY() + 1);
                break;
            case 'a':
                nextPosition = new Position(player.getX() - 1, player.getY());
                break;
            case 's':
                nextPosition = new Position(player.getX(), player.getY() - 1);
                break;
            case 'd':
                nextPosition = new Position(player.getX() + 1, player.getY());
                break;
            default:
                return;
        }
        if (checkValidMove(nextPosition)) {
            world[player.getX()][player.getY()] = player.getCurrentTile();
            player.setCurrentTile(world[nextPosition.getX()][nextPosition.getY()]);
            player.setX(nextPosition.getX());
            player.setY(nextPosition.getY());
        }
    }

    /**
     * save the information of the executing game
     */
    private void save() {
        SavedGame savedGame = new SavedGame(mapGenerator, player, gameConfig);
        File f = new File("./savedGame.ser");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(savedGame);
            os.close();
            fs.close();
            System.out.printf("Serialized data is saved in ./savedGame.ser");
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * load the game
     * @return
     */
    private SavedGame load() {
        File f = new File("./savedGame.ser");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                SavedGame savedGame = (SavedGame) os.readObject();
                os.close();
                fs.close();
                return savedGame;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        return null;
    }

    private boolean checkWin(Position p){
        return world[p.getX()][p.getY()].equals(Tileset.LOCKED_DOOR);
    }
    private boolean checkWall(Position p) {
        return world[p.getX()][p.getY()].equals(Tileset.WALL);
    }

    private boolean checkNothing(Position p) {
        return world[p.getX()][p.getY()].equals(Tileset.NOTHING);
    }

    private boolean checkValidMove(Position p) {
        return !checkWall(p) && !checkNothing(p);
    }

    /**
     * select a entity, from the floor
     * @param world
     * @param p
     * @return
     */
    private WalkingEntity selectEntity(TETile[][] world, Position p) {
        double maxDistance = 0;
        WalkingEntity farthest = new WalkingEntity(0, 0, world[0][0]);
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                if (world[i][j] != Tileset.FLOOR) {
                    continue;
                }
                double distance = (p.getX() - i) * (p.getX() - i) + (p.getY() - j) * (p.getY() - j);
                if (distance > maxDistance) {
                    maxDistance = distance;
                    farthest.setX(i);
                    farthest.setY(j);
                    farthest.setCurrentTile(world[i][j]);
                }
            }
        }
        return farthest;
    }

    private long parseSeed() {
        String userInput = "";
        boolean typing = true;
        while (typing) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (key == 's') {
                    return Long.parseLong(userInput);
                } else if (!Character.isDigit(key)) {
                    continue;
                }
                userInput += key;
                StdDraw.clear(Color.BLACK);
                StdDraw.text((double) WIDTH / 2, (double) HEIGHT * 4 / 5, "Please typing seed");
                StdDraw.text((double) WIDTH / 2, (double) HEIGHT / 2, userInput);
                StdDraw.show();
            }
        }
        return 0;
    }


    /**
     * when mouse click on the Tile show the Tile name
     * @param
     */
    private void displayTileNameOnTopLeft() {
        double mouseX = StdDraw.mouseX();
        double mouseY = StdDraw.mouseY();
        int x = (int) mouseX;
        int y = (int) mouseY;
        if (x >= world.length || y >= world[0].length || x < 0 || y < 0) {
            return;
        }
        TETile tile = world[x][y];
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.textLeft(1, HEIGHT - 1, tile.description());

    }
    private void displayGameScoreOnTopMid() {
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH/2, HEIGHT - 1, "current score: " + gameConfig.gameScore);
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        String num = "";
        for (int i = 0; i < input.length(); i++) {
            if (((int) input.charAt(i) >= 48) && ((int) input.charAt(i) <= 57)) {
                num += input.charAt(i);
            }
        }
        long seed = Long.parseLong(num);
        MapGenerator mapGenerator = new MapGenerator(seed, WIDTH, HEIGHT);
        TETile[][] finalWorldFrame = mapGenerator.getWorld();
        return finalWorldFrame;
    }
}
