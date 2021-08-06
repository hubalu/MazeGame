package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;

public class BlueLights implements Serializable {
    private Position centre;
    private String lightColor;
    private Rectangle room;
    public BlueLights(Position p, Rectangle room){
        centre=p;
        this.room = room;
        lightColor = "blue";
    }
    public BlueLights(int x,int y, Rectangle room){
        centre=new Position(x, y);
        this.room = room;
        lightColor = "blue";
    }
    public void setLightBlue() {
        lightColor = "blue";
    }
    public void setLightRed() {
        lightColor = "red";
    }
    public boolean isLightRed() {
        return lightColor.equals("red");
    }
    public boolean isLightBlue() {
        return lightColor.equals("blue");
    }
    public Position getCentre(){
        return centre;
    }
    public void setCentre(Position p){
        centre=p;
    }

    public TETile light(TETile[][] world) {
        world[centre.getX()][centre.getY()] = Tileset.BLUELIGHT;

        for (int i = centre.getX() - 10; i <= centre.getX() + 10; i++) {
            for (int j = centre.getY() - 10; j <= centre.getY() + 10; j++) {
                if (i < room.leftBottom.getX()
                        || i > room.rightTop.getX()
                        || j < room.leftBottom.getY()
                        || j > room.rightTop.getY() || (i == centre.getX() && j == centre.getY())) {
                    continue;
                }
                if (lightColor.equals("blue")) {
                    int layer = Math.max(Math.abs(i - centre.getX()), Math.abs(j - centre.getY()));
                    switch (layer) {
                        case 10 : world[i][j] = Tileset.Blue1; break;
                        case 9 : world[i][j] = Tileset.Blue2; break;
                        case 8 : world[i][j] = Tileset.Blue3; break;
                        case 7 : world[i][j] = Tileset.Blue4; break;
                        case 6 : world[i][j] = Tileset.Blue5; break;
                        case 5 : world[i][j] = Tileset.Blue6; break;
                        case 4 : world[i][j] = Tileset.Blue7; break;
                        case 3 : world[i][j] = Tileset.Blue8; break;
                        case 2 : world[i][j] = Tileset.Blue9; break;
                        case 1 : world[i][j] = Tileset.Blue10; break;
                        default: break;
                    }
                } else {
                    world[i][j] = Tileset.FLOOR;
                }
            }
        }
        return Tileset.LIGHTOFF;
    }

}
