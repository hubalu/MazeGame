package byog.Core;

import java.io.Serializable;

public class Position implements Serializable {

    private int x;
    private int y;

    public Position(int nx, int ny) {
        x = nx;
        y = ny;
    }

    public Position() {
        x = 35;
        y = 35;
    }

    public void setX(int nx) {
        x = nx;
    }

    public void setY(int ny) {
        y = ny;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    @Override
    public int hashCode() {
        return x*31 + y*17;
    }
    @Override
    public boolean equals(Object obj) {
        Position p = (Position) obj;
        return p.x == x && p.y == y;
    }
}
