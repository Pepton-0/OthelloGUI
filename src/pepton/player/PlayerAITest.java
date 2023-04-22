package pepton.player;

import pepton.GameStage;
import pepton.SetupSettings;
import pepton.Vec2i;
import pepton.VirtualBoard;

import java.util.ArrayList;

public class PlayerAITest extends PlayerBase {
    public PlayerAITest(SetupSettings.PlayerInfo info, GameStage stage) {
        super(info, stage);
    }

    @Override
    public void receiveTurnFlag() {
        super.receiveTurnFlag();

        Thread thread = new Thread(() -> {

            long time = System.currentTimeMillis();

            System.out.println("-------");
            VirtualBoard current = stage.getVirtualBoardCopy();
            float bestScore = Float.MIN_VALUE;
            Vec2i bestPos = new Vec2i(-1, -1);
            for (Vec2i candidate : current.settablePositions(info.side())) {
                float score = search(current.copy().setStateAndUpdate(candidate.x(), candidate.y(), info.side()),
                        info.side(), info.side(), 5);
                System.out.println(score + "  x:y=" + candidate.x() + ":" + candidate.y());
                if (score >= bestScore || bestPos.x() < 0) {
                    bestScore = score;
                    bestPos = candidate;
                }
            }

            final Vec2i chosen = bestPos;
            long timeToWait = Math.min(250 - (System.currentTimeMillis() - time), 250);
            if (timeToWait > 0) {
                try {
                    Thread.sleep(timeToWait);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            releaseTurnFlag(chosen.x(), chosen.y());
        });
        thread.start();
    }

    private float search(VirtualBoard now, boolean side, boolean turn, int depth) {
        ArrayList<Vec2i> currentSettable = now.settablePositions(turn);
        ArrayList<Vec2i> nextSettable = now.settablePositions(!turn);
        if ((currentSettable.size() == 0 && nextSettable.size() == 0) || depth <= 0) {
            return now.evaluate1(side);
        } else {
            if (currentSettable.size() == 0) { // && nextSettable
                turn = !turn;
                currentSettable = nextSettable;
            }
            float worstScore = Float.MAX_VALUE;
            for (Vec2i candidate : currentSettable) {
                float score = search(now.copy().setStateAndUpdate(candidate.x(), candidate.y(), turn), side, !turn, depth - 1);
                if (score <= worstScore) {
                    worstScore = score;
                }
            }
            return worstScore;
        }
    }
}
