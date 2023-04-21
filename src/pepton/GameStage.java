package pepton;

import pepton.player.PlayerBase;

import javax.swing.*;
import java.util.Map;

public class GameStage {
    JPanel panel;
    private JTextPane stateView;
    private JButton back;
    private JPanel gameScene;
    private JButton button1;
    private JPanel firstPlayer;
    private JPanel secondPlayer;
    private JTextPane firstPlayerState;
    private JTextPane secondPlayerState;

    /**
     * @apiNote false: 1st player</p>true: 2nd player
     */
    private final Map<Boolean, PlayerBase> players;

    private final BackToPreparationListener backListener;

    GameStage(BackToPreparationListener backListener, GameSettings settings){
        this.backListener = backListener;
        back.addActionListener(e->backListener.backPerformed());

        try {
            players = Map.of(false, settings.preparePlayer(false),
                    true, settings.preparePlayer(true));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
