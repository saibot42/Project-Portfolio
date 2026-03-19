package SOLO;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        // Create players
        PlayerFactory playerFactory = new PlayerFactory();
        ArrayList<Player> allPlayers = playerFactory.createAllPlayers();

        // Create teams
        TeamFactory teamFactory = new TeamFactory(allPlayers);
        ArrayList<Team> teams = teamFactory.teams;

        // Load weekly points from CSV
        ReadCSV.updatePlayerPointsFromCSV("fantasy_points.csv", allPlayers);

        // Example: get points for a player or team
    }
}
