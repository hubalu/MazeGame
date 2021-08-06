package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.*;


public class MapGenerator implements Serializable {
    private Random RANDOM;
    private TETile[][] world;
    private Position doorPosition;
    private Map<Position,BlueLights> blueLights;
    public List<Rectangle> rectangleList;

    /**
     * Constructor
     * @param seed
     * @param width
     * @param height
     */
    public MapGenerator(long seed, int width, int height) {
        RANDOM = new Random(seed);
        rectangleList = new LinkedList<>();
        world = generateWorld(width, height);
        blueLights = addBlueLights();
        doorPosition = addLockedDoor();
        drawExtraElement();
    }
    public TETile[][] getWorld() {
        return world;
    }

    public Position getDoorPosition() {
        return doorPosition;
    }
    public Map<Position,BlueLights> getBlueLights() {
        return blueLights;
    }
    /**
     * take width and height as para to produce a 2-D TETile world
     * @param width
     * @param height
     * @return
     */
    private TETile[][] generateWorld(int width, int height) {
        TETile[][] world = new TETile[width][height];
        initializeWorld(world);
        Position p = new Position(width / 2, height / 2);
        generateRectangles(world, p, rectangleList);
        fullFillWall(world);
        return world;
    }

    /**
     * draw extra elements except for floors and walls
     */
    private void drawExtraElement() {
        drawBlueLights();
        drawLockedDoor();
    }

    /**
     * randomly generate rectangles and draw them within world[][]
     * @param world
     * @param p
     * @param rectangleList
     */
    public void generateRectangles(TETile[][] world, Position p, List<Rectangle> rectangleList) {
        //draw the first rectangle from the given position p
        Rectangle rectangle = generateRectangleFromPoint(p);
        drawRectangle(world, rectangle, Tileset.FLOOR);
        rectangleList.add(rectangle);
        //draw other rectangles randomly according to the first rectangle
        while (rectangleList.size() < 60) {
            rectangle = chooseRandomRectangle(rectangleList);
            String direction = chooseRandomDirection();
            Rectangle newRectangle = generateRectangleFromRectangle(rectangle, direction, 2);
            if (!checkRectangleInsideMap(world, newRectangle)) {
                continue;
            }
            if (!checkValid(rectangleList, newRectangle, rectangle)) {
                continue;
            }
            rectangleList.add(newRectangle);
            drawRectangle(world, newRectangle, Tileset.FLOOR);
        }
    }

    /**
     * choose a random position on the given direction side of the given rectangle
     * generate a new rectangle or hallway from the new position
     * @param rectangle
     * @param direction
     * @param leastLength
     * @return
     */
    private Rectangle generateRectangleFromRectangle(Rectangle rectangle, String direction, int leastLength) {
        Position p = chooseRandomPosition(rectangle, direction);
        if (rectangle.type == Rectangle.RECTANGLE) {
            switch (direction) {
                case "Left":
                    return new Rectangle(
                            new Position(p.getX() - (RANDOM.nextInt(8) + leastLength), p.getY()), p);
                case "Up":
                    return new Rectangle(
                            p, new Position(p.getX(), p.getY() + (RANDOM.nextInt(8) + leastLength)));
                case "Right":
                    return new Rectangle(
                            p, new Position(p.getX() + (RANDOM.nextInt(8) + leastLength), p.getY()));
                case "Down":
                    return new Rectangle(
                            new Position(p.getX(), p.getY() - (RANDOM.nextInt(8) + leastLength)), p);
                default:
                    throw new RuntimeException("direction not exist");
            }
        } else if (rectangle.type == Rectangle.HORIZONTALLINE) {
            switch (direction) {
                case "Left":
                    return new Rectangle(
                            new Position(p.getX() - (RANDOM.nextInt(10) + leastLength), p.getY() - (RANDOM.nextInt(5) + leastLength)),
                            new Position(p.getX(), p.getY() + (RANDOM.nextInt(5))));
                case "Up":
                    return new Rectangle(
                            p, new Position(p.getX(), p.getY() + (RANDOM.nextInt(10) + leastLength)));
                case "Right":
                    return new Rectangle(
                            new Position(p.getX(), p.getY() - (RANDOM.nextInt(5) + leastLength)),
                            new Position(p.getX() + (RANDOM.nextInt(10) + leastLength), p.getY() + (RANDOM.nextInt(5))));
                case "Down":
                    return new Rectangle(
                            new Position(p.getX(), p.getY() - (RANDOM.nextInt(10) + leastLength)), p);
                default:
                    throw new RuntimeException("direction not exist");
            }
        } else if (rectangle.type == Rectangle.VERTICALLINE) {
            switch (direction) {
                case "Left":
                    return new Rectangle(
                            new Position(p.getX() - (RANDOM.nextInt(10) + leastLength), p.getY()), p);
                case "Up":
                    return new Rectangle(
                            new Position(p.getX() - (RANDOM.nextInt(5) + leastLength), p.getY()),
                            new Position(p.getX() + (RANDOM.nextInt(5)), p.getY() + (RANDOM.nextInt(10) + leastLength)));
                case "Right":
                    return new Rectangle(
                            p, new Position(p.getX() + (RANDOM.nextInt(10) + leastLength), p.getY()));
                case "Down":
                    return new Rectangle(
                            new Position(p.getX() - (RANDOM.nextInt(5) + leastLength), p.getY() - (RANDOM.nextInt(10) + leastLength)),
                            new Position(p.getX() + (RANDOM.nextInt(5)), p.getY()));
                default:
                    throw new RuntimeException("direction not exist");
            }
        }
        return null;
    }
    public void drawBlueLights() {
        for (Position p : blueLights.keySet()) {
            blueLights.get(p).light(world);
        }
    }

    private Map<Position,BlueLights> addBlueLights(){
        blueLights = new HashMap<>();
        for(Rectangle rectangle:rectangleList){
            if(rectangle.type==Rectangle.RECTANGLE){
                Position p=new Position();
                p.setX(rectangle.leftBottom.getX()+RANDOM.nextInt(rectangle.width));
                p.setY(rectangle.leftBottom.getY()+RANDOM.nextInt(rectangle.height));
                blueLights.put(p,new BlueLights(p, rectangle));
            }
            else{
                continue;
            }
        }
        return blueLights;
    }

    /**
     * fulfill the wall around floor
     * @param world
     */
    private void fullFillWall(TETile[][] world) {
        int[] dx = new int[]{0, 0, 1, -1, 1, 1, -1, -1};
        int[] dy = new int[]{1, -1, 0, 0, 1, -1, 1, -1};
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                int cnt = 0;
                if (world[i][j] != Tileset.NOTHING) {
                    continue;
                }
                for (int k = 0; k < 8; k++) {
                    int ii = i + dx[k];
                    int jj = j + dy[k];
                    if (ii < 0 || ii >= world.length || jj < 0 || jj >= world[0].length) {
                        continue;
                    }
                    if (world[ii][jj] == Tileset.FLOOR) {
                        cnt++;
                    }
                }
                if (cnt > 0) {
                    world[i][j] = Tileset.WALL;
                }
            }
        }
    }

    /**
     * add one locked yellow door in the walls
     * @return
     */
    private Position addLockedDoor() {
        while (true) {
            Rectangle rectangle = chooseRandomRectangle(rectangleList);
            if (rectangle.type != Rectangle.RECTANGLE) {
                continue;
            }
            String direction = chooseRandomDirection();
            Position p = chooseRandomPosition(rectangle, direction);
            if (world[p.getX()][p.getY()] != Tileset.WALL) {
                continue;
            }
            world[p.getX()][p.getY()] = Tileset.LOCKED_DOOR;
            return new Position(p.getX(), p.getY());
        }

    }
    private void drawLockedDoor() {
        Position p = doorPosition;
        world[p.getX()][p.getY()] = Tileset.LOCKED_DOOR;
    }


    /**
     * generate the first rectangle
     * @param leftBottom
     * @return
     */
    private Rectangle generateRectangleFromPoint(Position leftBottom) {
        int width = RANDOM.nextInt(5) + 1;
        int height = RANDOM.nextInt(5) + 1;
        Position rightTop = new Position(leftBottom.getX() + width, leftBottom.getY() + height);
        return new Rectangle(leftBottom, rightTop);
    }

    /**
     * scan the rectangleList and randomly choose one
     * @param rectangleList
     * @return
     */
    private Rectangle chooseRandomRectangle(List<Rectangle> rectangleList) {
        return rectangleList.get(RANDOM.nextInt(rectangleList.size()));
    }

    /**
     * randomly choose a side or direction
     * @return
     */
    private String chooseRandomDirection() {
        int condition = RANDOM.nextInt(4);
        switch (condition) {
            case 0:
                return "Left";
            case 1:
                return "Up";
            case 2:
                return "Right";
            case 3:
                return "Down";
        }
        return null;
    }

    /**
     * choose a random Position from rectangle
     * @param rectangle
     * @param direction
     * @return a Position(a point)
     */
    private Position chooseRandomPosition(Rectangle rectangle, String direction) {
        switch (direction) {
            case "Left":
                return new Position(rectangle.leftBottom.getX() - 1,
                        rectangle.leftBottom.getY() + RANDOM.nextInt(rectangle.height));
            case "Up":
                return new Position(rectangle.leftBottom.getX() + RANDOM.nextInt(rectangle.width),
                        rectangle.rightTop.getY() + 1);
            case "Right":
                return new Position(rectangle.rightTop.getX() + 1,
                        rectangle.leftBottom.getY() + RANDOM.nextInt(rectangle.height));
            case "Down":
                return new Position(rectangle.leftBottom.getX() + RANDOM.nextInt(rectangle.width),
                        rectangle.leftBottom.getY() - 1);
            default:
                throw new RuntimeException("condition out of cases when choosing random position");
        }
    }

    /**
     * fulfill the 2D TETile with NOTING
     * @param world
     */
    private void initializeWorld(TETile[][] world) {
        for (int x = 0; x < world.length; x += 1) {
            for (int y = 0; y < world[0].length; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    /**
     * We consider width>0 to right and height>0 to up
     *
     * @param world
     * @param tile
     */
    public void drawRectangle(TETile[][] world, Rectangle rectangle, TETile tile) {
        for (int i = rectangle.leftBottom.getX(); i <= rectangle.rightTop.getX(); i++) {
            for (int j = rectangle.leftBottom.getY(); j <= rectangle.rightTop.getY(); j++) {
                world[i][j] = tile;
            }
        }
    }

    /**
     * Check if Position inside map
     * @param world
     * @param rectangle
     * @return
     */
    private boolean checkRectangleInsideMap(TETile[][] world, Rectangle rectangle) {
        return rectangle.rightTop.getX() < world.length - 1
                && rectangle.rightTop.getY() < world[0].length - 1
                && rectangle.leftBottom.getX() >= 1
                && rectangle.leftBottom.getY() >= 1;
    }

    /**
     * check if the rectangle overlap
     * @param rectangleList
     * @param rectangle
     * @param originRectangle
     * @return
     */
    private boolean checkValid(List<Rectangle> rectangleList, Rectangle rectangle, Rectangle originRectangle) {
        for (Rectangle rec : rectangleList) {
            if (rec == originRectangle) {
                continue;
            }
            if (rec.checkOverlap(rectangle)) {
                return false;
            }
        }
        return true;
    }
}
