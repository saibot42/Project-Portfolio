package Structures;

public class MapBounds {
    private final double upperLatitude;
    private final double lowerLatitude;
    private final double rightLongitude;
    private final double leftLongitude;

    public MapBounds(double upperLatitude, double lowerLatitude, double rightLongitude, double leftLongitude) {
        this.upperLatitude = upperLatitude;
        this.lowerLatitude = lowerLatitude;
        this.rightLongitude = rightLongitude;
        this.leftLongitude = leftLongitude;
    }

    public double getUpperLatitude() {
        return upperLatitude;
    }

    public double getLowerLatitude() {
        return lowerLatitude;
    }

    public double getRightLongitude() {
        return rightLongitude;
    }

    public double getLeftLongitude() {
        return leftLongitude;
    }
}

