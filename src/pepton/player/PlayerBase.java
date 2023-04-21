package pepton.player;

import pepton.GameStage;
import pepton.SetupSettings;

public class PlayerBase {

    protected final SetupSettings.PlayerInfo info;

    protected final GameStage stage;

    public PlayerBase(SetupSettings.PlayerInfo info, GameStage stage) {
        this.info = info;
        this.stage = stage;
    }

    public SetupSettings.PlayerInfo getInfo() {
        return info;
    }

    public void receiveTurnFlag(){
    }

    public void releaseTurnFlag(int x, int y){
        stage.receiveTurnFlag(x, y);
    }
}
