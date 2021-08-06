package byog.Core;

import byog.TileEngine.TETile;

import java.io.Serializable;

public class WalkingEntity implements Serializable {
    private int x;
    private int y;
    private TETile currentTile;
    public WalkingEntity(int px,int py, TETile currentTile){
        x=px;
        y=py;
        this.currentTile = currentTile;
    }
    public void setX(int nx) {
        x = nx;
    }

    public void setY(int ny) {
        y = ny;
    }
    public void setCurrentTile(TETile currentTile) {
        this.currentTile = currentTile;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public TETile getCurrentTile() {
        return currentTile;
    }
    public static Position parsePosition(WalkingEntity entity){
        return new Position(entity.x, entity.y);
    }
}
