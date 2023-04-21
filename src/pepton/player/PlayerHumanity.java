package pepton.player;

import pepton.GameStage;
import pepton.SetupSettings;
import pepton.Vec2i;
import pepton.VirtualBoard;

import java.util.ArrayList;

public class PlayerHumanity extends PlayerBase {
    public PlayerHumanity(SetupSettings.PlayerInfo info, GameStage stage) {
        super(info, stage);
    }

    @Override
    public void receiveTurnFlag() {
        super.receiveTurnFlag();

        VirtualBoard clone = stage.getVirtualBoardCopy();
        ArrayList<Vec2i> candidates = clone.settablePositions(info.side());
        for (Vec2i candidate : candidates) {
            stage.setListenersToDiscViews(
                    e -> {
                        releaseTurnFlag(candidate.x(), candidate.y());
                    }, true, info.side(), candidate);
        }
    }
}
