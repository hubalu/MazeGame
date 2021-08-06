package byog.Core;

import java.io.Serializable;

public class GameConfig implements Serializable {
    public boolean gameOver;
    public boolean turnLightOn;
    public int gameScore;
    public boolean gameWin;
    public GameConfig() {
        turnLightOn = false;
        gameOver = false;
        gameScore = 0;
        gameWin = false;
    }
}
