package pepton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Map;

public class Preparation {
    private JComboBox comboBox1;
    private JTextPane firstPlayerTextPane;
    private JTextPane secondPlayerTextPane;
    private JComboBox comboBox2;
    private JTextPane setupOthelloTextPane;
    private JButton playWithAboveSettingsButton;
    JPanel panel;

    private final OthelloFrame frame;
    private final GameSetupListener gameSetupListener;

    Preparation(OthelloFrame frame, GameSetupListener gameSetupListener) {
        this.frame = frame;
        playWithAboveSettingsButton.addActionListener(this::onPlayButton);
        this.gameSetupListener = gameSetupListener;
    }

    private void onPlayButton(ActionEvent e) {
        gameSetupListener.SetupPerformed(new SetupSettings(
                Map.of(false,
                        new SetupSettings.PlayerInfo(false, comboBox1.getItemAt(comboBox1.getSelectedIndex()).toString()),
                        true,
                        new SetupSettings.PlayerInfo(true, comboBox2.getItemAt(comboBox2.getSelectedIndex()).toString())
                ),
                Map.of(0, "●",
                        1, "〇",
                        2, "-"),
                8));
    }
}
