package pepton;

import pepton.player.PlayerAIRandom;
import pepton.player.PlayerAITest;
import pepton.player.PlayerBase;
import pepton.player.PlayerHumanity;

import java.util.Map;

public record SetupSettings(Map<Boolean, PlayerInfo> playerList, Map<Integer, String> discIcons, int size) {
    public record PlayerInfo(boolean side, String playerType) {
    }

    public PlayerBase preparePlayer(boolean side, GameStage stage) throws RuntimeException {
        if (playerList.containsKey(side)) {
            PlayerInfo info = playerList.get(side);
            return switch (info.playerType) {
                case "AI_Test" -> new PlayerAITest(info, stage);
                case "AI_Random" -> new PlayerAIRandom(info, stage);
                default -> new PlayerHumanity(info, stage); // include case:Humanity
            };
        } else {
            throw new RuntimeException("The side is not prepared yet: " + (side ? "second" : "first"));
        }
    }
}
