package pepton;

import pepton.player.PlayerBase;

import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * 1. initialize
 * 2. releaseNextTurnFlag -> 1st player:receiveTurnFlag
 * 3. receiveTurnFlag <- 1st player:releaseTurnFlag
 * 4. releaseNextTurnFlag -> 2nd player:receiveTurnFlag
 * 5. receiveTurnFlag <- 2nd player:releaseTurnFlag
 * 6. releaseNextTurnFlag -> 1st player:receiveTurnFlag
 * 7... etc.
 * n. receiveTurnFlag <- player:releaseTurnFlag
 * n+1. game set
 */
public class GameStage {
    JPanel panel;
    private JTextPane stateView;
    private JButton back;
    private JPanel gameScene;
    private JPanel firstPlayer;
    private JPanel secondPlayer;
    private JTextPane firstPlayerState;
    private JTextPane secondPlayerState;
    private JPanel boardPane;

    /**
     * @apiNote false: 1st player</p>true: 2nd player
     */
    private final Map<Boolean, PlayerBase> players;

    private final JButton[][] discViews;

    private final Map<Integer, String> discIcons;

    private final VirtualBoard virtualBoard;

    private boolean nowTurn;

    GameStage(BackToPreparationListener backListener, SetupSettings settings) {
        back.addActionListener(e -> backListener.backPerformed());
        this.discIcons = settings.discIcons();

        try {
            players = Map.of(false, settings.preparePlayer(false, this),
                    true, settings.preparePlayer(true, this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        GridLayout layout = new GridLayout(settings.size(), settings.size());
        boardPane.setLayout(layout);
        discViews = new JButton[settings.size()][settings.size()];
        for (int y = settings.size() - 1; y >= 0; y--) {
            for (int x = 0; x < settings.size(); x++) {
                final JButton discView = new JButton();
                discView.setFocusable(false);
                discView.setEnabled(false);
                discView.setText(discIcons.get(2));
                discView.setText(y + ":" + x);
                discView.setUI(new MetalButtonUI(){
                    @Override
                    protected Color getDisabledTextColor() {
                        return Color.BLACK;
                    }
                });
                // TODO もっと見やすく

                boardPane.add(discView);
                discViews[x][y] = discView;
            }
        }

        virtualBoard = new VirtualBoard(settings.size(), settings.size());
        virtualBoard.setStartState();

        nowTurn = true;
        releaseNextTurnFlag();
    }

    private void updatePlayerState(JTextPane text, PlayerBase player) {
        String discIcon = discIcons.get(Comparator.toInt(player.getInfo().side()));
        String side = player.getInfo().side() ? "2nd"+discIcon : "1st"+discIcon;
        String all = side + ":" + player.getInfo().playerType();
        int value = Comparator.toInt(player.getInfo().side());
        all += "\n Discs: " + virtualBoard.getAmountOf(value);
        if (nowTurn == player.getInfo().side())
            all += "\n Your turn!";
        text.setText(all);
    }

    private void updateBoardView() {
        stateView.setText(nowTurn ? "2nd player's turn!" : "1st player's turn!");

        updatePlayerState(firstPlayerState, players.get(false));
        updatePlayerState(secondPlayerState, players.get(true));

        for (int x = 0; x < virtualBoard.width; x++) {
            for (int y = 0; y < virtualBoard.height; y++) {
                JButton button = discViews[x][y];
                button.setText(discIcons.get(virtualBoard.getState(x, y)));
                button.setEnabled(false);
                button.setBackground(new Color(10, 120, 10));
            }
        }
    }

    private void releaseNextTurnFlag() {
        boolean nowSettable = virtualBoard.settablePositions(nowTurn).size() >= 1;
        boolean nextSettable = virtualBoard.settablePositions(!nowTurn).size() >= 1;
        int nowDiscs = virtualBoard.getAmountOf(Comparator.toInt(nowTurn));
        int nextDiscs = virtualBoard.getAmountOf(Comparator.toInt(!nowTurn));
        if (!nowSettable && !nextSettable) {
            updateBoardView();
            String msg;
            if (nowDiscs == nextDiscs)
                msg = "Draw!";
            else
                msg = (nowTurn ? "2nd" : "1st") + " won!";
            stateView.setText(msg);
            back.setEnabled(true);
            return;
        } else if (nowSettable && !nextSettable) {
            nowTurn = !nowTurn;
        }
        nowTurn = !nowTurn;
        updateBoardView();
        players.get(nowTurn).receiveTurnFlag();
        back.setEnabled(false);
    }

    public void receiveTurnFlag(int x, int y) {
        // update VirtualBoard (but do not update board view. view will be updated in releaseNextTurnFlag)
        virtualBoard.setStateAndUpdate(x, y, nowTurn);
        releaseNextTurnFlag();
    }

    public VirtualBoard getVirtualBoardCopy() {
        return virtualBoard.copy();
    }

    /**
     * @apiNote できればpublicにしたくなかった…
     */
    public void setListenersToDiscViews(ActionListener listener, boolean setEnable, boolean side, Vec2i... positions) {
        for (Vec2i vec : positions) {
            JButton button = discViews[vec.x()][vec.y()];
            for (ActionListener old : button.getActionListeners())
                button.removeActionListener(old);
            button.addActionListener(listener);
            if (setEnable)
                button.setEnabled(true);
            button.setText(discIcons.get(Comparator.toInt(side)));
            button.setBackground(Color.CYAN);
        }
    }
}
