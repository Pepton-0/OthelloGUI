package pepton.player;

import pepton.GameStage;
import pepton.SetupSettings;
import pepton.Vec2i;

import java.util.ArrayList;
import java.util.Random;

public class PlayerAIRandom extends PlayerBase {

    private final Random random = new Random(System.nanoTime());

    public PlayerAIRandom(SetupSettings.PlayerInfo info, GameStage stage) {
        super(info, stage);
    }

    @Override
    public void receiveTurnFlag() {
        super.receiveTurnFlag();

        ArrayList<Vec2i> list = stage.getVirtualBoardCopy().settablePositions(info.side());
        Vec2i chosen = list.get(random.nextInt(list.size()));

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            releaseTurnFlag(chosen.x(), chosen.y());
        });
        thread.start();
    }
}
