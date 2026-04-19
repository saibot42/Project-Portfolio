package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import Structures.Address;
import Structures.Delivery;
import Structures.Pixel;
import planners.DeliveryManager;
import planners.DeliveryOverview;
import utils.MapManager;

public class MapPanel extends JPanel {
    private MapManager mapManager;
    private DeliveryManager deliveryManager;
    private Address pAddress;
    private Image peppesIcon;
    private Image mapImage;

    public MapPanel(MapManager mapManager, DeliveryManager deliveryManager, Address pAddress) {
        this.mapManager = mapManager;
        this.deliveryManager = deliveryManager;
        this.pAddress = pAddress;
        this.mapImage = mapManager.getMapImage();
        this.peppesIcon = new ImageIcon("assets/peppesIcon.png").getImage();
        setBackground(dashboardGUI.theme.background());
        

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
        g2d.setColor(dashboardGUI.theme.transparentColor()); // Fully transparent

        for (int x = 0; x < mapManager.getPixelGrid().getGridWidth(); x++) {
            for (int y = 0; y < mapManager.getPixelGrid().getGridHeight(); y++) {
                g2d.drawRect(x * mapManager.getPixelGrid().getCellWidth(), 
                           y * mapManager.getPixelGrid().getCellHeight(),
                           mapManager.getPixelGrid().getCellWidth(), 
                           mapManager.getPixelGrid().getCellHeight());
            }
        }
    }

    private void highlightDeliveryLocations(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cellWidth = mapManager.getPixelGrid().getCellWidth();
        int cellHeight = mapManager.getPixelGrid().getCellHeight();
        
        //Guards -> If dimensions arent ready yet 
        if (cellWidth <= 0 || cellHeight <= 0) return;
        if (mapImage.getWidth(null) <= 0) return;

        // --- RESTAURANT ICON ---
        int iconSize = cellWidth * 12; // 3x bigger than a cell
        Pixel peppesPixel = mapManager.ConvertMapToGrid(pAddress.getMapCoordinate());

        // Center the icon on the pixel rather than top-left aligning it
        int iconX = (peppesPixel.getX() * cellWidth) + (cellWidth / 2) - (iconSize / 2);
        int iconY = (peppesPixel.getY() * cellHeight) + (cellHeight / 2) - (iconSize / 2);
        g2d.drawImage(peppesIcon, iconX, iconY, iconSize, iconSize, null);

        // --- DELIVERY DOTS ---
        int dotSize = cellWidth * 3; // Bigger than a cell, adjust to taste

        for (Delivery delivery : deliveryManager.getPendingDeliveries()) {
            Pixel pixel = mapManager.ConvertMapToGrid(delivery.getAddress().getMapCoordinate());

            // Center point of this cell
            int cx = (pixel.getX() * cellWidth) + (cellWidth / 2);
            int cy = (pixel.getY() * cellHeight) + (cellHeight / 2);

            // Draw colored filled circle centered on the location
            switch (delivery.getStatus()) {
                case ON_TIME: g2d.setColor(dashboardGUI.theme.onTimeTextColor());  break;
                case WARNING: g2d.setColor(dashboardGUI.theme.warningTextColor()); break;
                case LATE:    g2d.setColor(dashboardGUI.theme.lateTextColor());    break;
                default:      g2d.setColor(dashboardGUI.theme.primaryTextColor()); break;
            }

            // fillOval centered on cx, cy
            g2d.fillOval(cx - (dotSize / 2), cy - (dotSize / 2), dotSize, dotSize);

            // Subtle dark border around the dot so it's visible on any map color
            g2d.setColor(new Color(0, 0, 0, 120));
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawOval(cx - (dotSize / 2), cy - (dotSize / 2), dotSize, dotSize);
        }
    }
}