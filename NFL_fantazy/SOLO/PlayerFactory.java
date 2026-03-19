package SOLO;

import java.util.ArrayList;

public class PlayerFactory {
    public static ArrayList<Player> createAllPlayers() {
        ArrayList<Player> players = new ArrayList<>();

        for (PlayerEnum pEnum : PlayerEnum.values()) {
            players.add(pEnum.toPlayer());
        }

        return players;
    }

    public static Player getPlayer(PlayerEnum pEnum) {
        return pEnum.toPlayer();
    }
}
