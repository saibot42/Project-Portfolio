package SOLO;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private String teamName;
    private ArrayList<Player> roster;
    private Double totalPTS;
    private ArrayList<Float> weeklyPTS;

    public Team(String teamName, ArrayList<Player> roster) {
        this.teamName = teamName;
        this.roster = roster;
    }

    public void signPlayer(Player player) {
        roster.add(player);
    }

    public void cutPlayer(Player player) {
        roster.remove(player);
    }

    public void tradePlayer(Player playerIN, Player playerOUT) {
        this.signPlayer(playerIN);
        this.cutPlayer(playerOUT);
    }

    public double getPointsForWeek(int week) {
        return roster.stream().mapToDouble(p -> p.getPointsForWeek(week)).sum();
    }

    public double getTotalPoints() {
        return roster.stream().mapToDouble(Player::getTotalPoints).sum();
    }

    public String getTeamName() { return teamName; }
    public List<Player> getRoster() { return roster; }
}