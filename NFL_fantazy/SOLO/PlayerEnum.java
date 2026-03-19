package SOLO;

public enum PlayerEnum {
    MAHOMES("Patrick Mahomes", Position.QB, NFLTEAM.CHIEFS),
    KELCE("Travis Kelce", Position.TE, NFLTEAM.CHIEFS),
    MCCAFFREY("Christian McCaffrey", Position.RB, NFLTEAM.NINERS),
    JEFFERSON("Justin Jefferson", Position.WR, NFLTEAM.VIKINGS);

    private final String name;
    private final Position position;
    private final NFLTEAM team;

    PlayerEnum(String name, Position position, NFLTEAM team) {
        this.name = name;
        this.position = position;
        this.team = team;
    }

    public Player toPlayer() {
        return new Player(name, position, team);
    }
}
