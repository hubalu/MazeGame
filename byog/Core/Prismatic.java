package byog.Core;

import byog.TileEngine.TETile;

public class Prismatic {
    Position center;
    int size;
    public Prismatic(Position p, int size) {
        center = p;
        this.size = size;
    }
    public void drawPrismatic(TETile[][] world, TETile[][] playerSight) {
        int k = 0;
        for (int j = center.getY() - size; j <= center.getY() + size; j++) {
            for (int i = center.getX() - k; i <= center.getX() + k; i++) {
                if (i < 0 || i >= world.length || j < 0 || j >= world[0].length) {
                    continue;
                }
                playerSight[i][j] = world[i][j];
            }
            if (j >= center.getY()) {
                k--;
            } else {
                k++;
            }
        }
    }

}
