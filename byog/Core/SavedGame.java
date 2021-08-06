package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;

public class SavedGame implements Serializable {
    private static final long serialVersionUID = 1234L;
    public MapGenerator mapGenerator;
    public WalkingEntity player;
    public GameConfig gameConfig;
    public SavedGame(MapGenerator mapGenerator, WalkingEntity player, GameConfig gameConfig
    ) {
        this.mapGenerator = mapGenerator;
        this.player = player;
        this.gameConfig = gameConfig;
    }
}
