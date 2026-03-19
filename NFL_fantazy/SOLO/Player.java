package SOLO;

import java.util.HashMap;
import java.util.Map;

public class Player {
    private String name;
    private Position position;
    private NFLTEAM NFLTeam;
    private Map<Integer, Double> weeklyPoints; // week -> points

    public Player(String name, Position position, NFLTEAM NFLTeam) {
        this.name = name;
        this.position = position;
        this.NFLTeam = NFLTeam;
        this.weeklyPoints = new HashMap<>();
    }

    public String getName() { return name; }
    public Position getPosition() { return position; }
    public NFLTEAM getNFLTeam() { return NFLTeam; }


    public void setPointsForWeek(int week, double points) {
        weeklyPoints.put(week, points);
    }

    public double getPointsForWeek(int week) {
        return weeklyPoints.getOrDefault(week, 0.0);
    }

    public double getTotalPoints() {
        return weeklyPoints.values().stream().mapToDouble(Double::doubleValue).sum();
    }
}
