package utils;

import Structures.*;

import java.awt.*;

public class MapManager {
    private MapBounds bounds;
    private PixelGrid grid;
    private Image mapImage;

    public MapManager(MapBounds bounds, Image mapImage, PixelGrid grid) {
        this.bounds = bounds;
        this.mapImage = mapImage;
        this.grid = grid;
        grid.setGridHeight(mapImage.getHeight(null) / grid.getCellHeight());
        grid.setGridWidth(mapImage.getWidth(null) / grid.getCellWidth());
    }

    public PixelGrid getPixelGrid() {
        return grid;
    }

    public Pixel ConvertMapToGrid(MapCoordinate coordinate) {
        double lat = coordinate.getLatitude();
        double lon = coordinate.getLongitude();
    
        // Latitude: Map to Y (vertical)
        double normalizedLatitude = (lat - bounds.getLowerLatitude()) / (bounds.getUpperLatitude() - bounds.getLowerLatitude());
        // Invert the latitude to match screen coordinate system
        int y = (int) Math.round((1 - normalizedLatitude) * grid.getGridHeight()); // Invert Y-axis

        // Longitude: Map to X (horizontal)
        double normalizedLongitude = (lon - bounds.getLeftLongitude()) / (bounds.getRightLongitude() - bounds.getLeftLongitude());
        int x = (int) Math.round(normalizedLongitude * grid.getGridWidth()); // No inversion needed

        // Ensure x and y are within bounds
        x = Math.min(Math.max(0, x), grid.getGridWidth() - 1);
        y = Math.min(Math.max(0, y), grid.getGridHeight() - 1);

        return new Pixel(x, y);

    }
    
    
    public Image getMapImage() {
        return mapImage;
    }


}
