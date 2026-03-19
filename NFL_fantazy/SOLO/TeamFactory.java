package SOLO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamFactory {
    ArrayList<Team> teams = new ArrayList<>();
    ArrayList<Player> availablePlayers;
    Map<String, Player> playerMap = new HashMap<>();

    public TeamFactory(ArrayList<Player> allPlayers) {
        createLookupMap(allPlayers);
        createAllTeams();
    }

    public void createAllTeams() {
        //---------  Team 1 ---------\\:
        ArrayList<Player> team1Roster = new ArrayList<>();
        team1Roster.add(PlayerFactory.getPlayer(PlayerEnum.MAHOMES));
        
        teams.add(new Team("McChartyism", team1Roster));

        //---------  Team 2 ---------\\:
        ArrayList<Player> team2Roster = new ArrayList<>();
        team1Roster.add(PlayerFactory.getPlayer(PlayerEnum.JEFFERSON));
        
        teams.add(new Team("McChartyism", team2Roster));

        //---------  Team 3 ---------\\:
        ArrayList<Player> team3Roster = new ArrayList<>();
        team1Roster.add(PlayerFactory.getPlayer(PlayerEnum.KELCE));
        
        teams.add(new Team("McChartyism", team3Roster));
        
        //---------  Team 4 ---------\\:
        ArrayList<Player> team4Roster = new ArrayList<>();
        team1Roster.add(PlayerFactory.getPlayer(PlayerEnum.MCCAFFREY));
        
        teams.add(new Team("McChartyism", team4Roster));

    }

    private void createLookupMap(ArrayList<Player> allPlayers) {
        // Build lookup map
        for (Player p : allPlayers) {
            playerMap.put(p.getName(), p);
        }
    }
}
