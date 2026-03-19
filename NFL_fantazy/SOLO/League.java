package SOLO;

import java.util.List;

public class League {
    private String leagueName;
    private Integer leagueSize;
    private List<Team> teams;

    public League(String leagueName, List<Team> teams) {
        this.leagueName = leagueName;
        this.teams = teams;
        this.leagueSize = teams.size();

    }

    public String getLeagueName() {
        return leagueName;
    }

    public Integer getleagueSize() {
        return leagueSize;
    }
    
    public List<Team> getTeams() {
        return teams;
    }
    
    
}
