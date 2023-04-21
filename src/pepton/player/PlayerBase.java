package pepton.player;

import pepton.GameSettings;

public class PlayerBase {

    protected final GameSettings.PlayerInfo info;

    public PlayerBase(GameSettings.PlayerInfo info){
        this.info = info;
    }
}
