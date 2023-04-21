package pepton;

import javax.swing.*;

public class OthelloFrame extends JFrame {

    private final Preparation preparation;
    private GameStage gameStage;

    public OthelloFrame() {
        preparation = new Preparation(this, this::setupStage);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize((int)(1920*0.4), (int)(1080*0.4));
        setTitle("Othello GUI");
        setLocationRelativeTo(null); // set the window position at the center of the screen.
        setContentPane(preparation.panel);
    }

    private void setupStage(GameSettings settings){
        gameStage = new GameStage(this::backToPreparation, settings);
        setContentPane(gameStage.panel);
    }

    private void backToPreparation(){
        gameStage=null;
        setContentPane(preparation.panel);
    }
}
