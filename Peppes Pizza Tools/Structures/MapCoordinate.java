package Structures;

public class MapCoordinate {
    private double lat;
    private double lon;

    public MapCoordinate(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    /**
     * Gets the latitude part of the coordinate, which gives us its vertical position. In order words, North / south
     * @return double
     */
    public double getLatitude() {
        return lat;
    }

    /**
     * Gets the longitude part of the coordinate, which gives us its horizontal position. In order words, West or North
     * @return double
     */
    public double getLongitude() {
        return lon;
    }

    public void setLatitude(double newLat) {
        this.lat = newLat;
    }

    public void setLongitude(double newLon) {
        this.lon = newLon;
    }
}
