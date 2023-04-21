package pepton;

import pepton.player.PlayerAITest;
import pepton.player.PlayerBase;
import pepton.player.PlayerHumanity;

import java.util.Map;

public record GameSettings(Map<Boolean, PlayerInfo> playerList) {
    public record PlayerInfo(boolean side, String playerType) {
    }

    public PlayerBase preparePlayer(boolean side) throws RuntimeException {
        if (playerList.containsKey(side)) {
            PlayerInfo info = playerList.get(side);
            return switch (info.playerType) {
                case "Humanity" -> new PlayerHumanity(info);
                case "AI_Test" -> new PlayerAITest(info);
                default -> new PlayerHumanity(info);
            };
        } else {
            throw new RuntimeException("The side is not prepared yet: " + (side ? "second" : "first"));
        }
    }
}
