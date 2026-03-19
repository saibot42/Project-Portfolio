package Structures;

public class MapCoordinate {
    private double lat;
    private double lon;

    public MapCoordinate(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLatitude() {
        return lat;
    }

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
