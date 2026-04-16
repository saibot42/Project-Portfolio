package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import Structures.Address;
import Structures.Delivery;
import Structures.Pixel;
import planners.DeliveryOverview;
import utils.MapManager;

public class MapPanel extends JPanel {
    private MapManager mapManager;
    private DeliveryOverview deliveries;
    private Address pAddress;
    private Image peppesIcon;
    private Image mapImage;

    public MapPanel(MapManager mapManager, DeliveryOverview deliveries, Address pAddress) {
        this.mapManager = mapManager;
        this.deliveries = deliveries;
        this.pAddress = pAddress;
        this.mapImage = mapManager.getMapImage();
        this.peppesIcon = new ImageIcon("assets/peppesIcon.png").getImage();
        

        // // Lock the size so BorderLayout.WEST doesn't squish it
        int w = mapImage.getWidth(null);
        int h = mapImage.getHeight(null);
        setPreferredSize(new Dimension(w, h));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawMap(g2d);
        drawGrid(g2d);
        highlightDeliveryLocations(g2d);
    }

    //Draw the map
    private void drawMap(Graphics2D g2d) {
        // Get the image's width and height
        int imgWidth = mapImage.getWidth(null);
        int imgHeight = mapImage.getHeight(null);

        g2d.drawImage(mapImage, 0, 0, imgWidth, imgHeight, null);
    }

    // Draw the grid cells
    private void drawGrid(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 0)); // Fully transparent

        for (int x = 0; x < mapManager.getPixelGrid().getGridWidth(); x++) {
            for (int y = 0; y < mapManager.getPixelGrid().getGridHeight(); y++) {
                g2d.drawRect(x * mapManager.getPixelGrid().getCellWidth(), 
                           y * mapManager.getPixelGrid().getCellHeight(),
                           mapManager.getPixelGrid().getCellWidth(), 
                           mapManager.getPixelGrid().getCellHeight());
            }
        }
    }

    // Highlight delivery locations
    private void highlightDeliveryLocations(Graphics2D g2d) {
        g2d.setColor(Color.RED); // Color for highlights
         // Get the width and height of a grid cell
        int cellWidth = mapManager.getPixelGrid().getCellWidth();
        int cellHeight = mapManager.getPixelGrid().getCellHeight();

        //Add Peppes Pizza Restaraunt to the map
        //TODO: Icon fills just the pixel and scaling up will only offsett the actual location -> need to find fix
        Pixel peppesLocationPixel = mapManager.ConvertMapToGrid(pAddress.getMapCoordinate());  // Convert to pixel
        g2d.drawImage(peppesIcon, peppesLocationPixel.getX() * cellWidth, peppesLocationPixel.getY() * cellHeight, cellWidth, cellHeight, null);

        //Add every delivery
        for (Delivery trip : deliveries) {
            Pixel pixel = mapManager.ConvertMapToGrid(trip.getAddress().getMapCoordinate());  // Convert to pixel
            g2d.fillRect(pixel.getX() * cellWidth, pixel.getY() * cellHeight, cellWidth, cellHeight);
        }
    }
}