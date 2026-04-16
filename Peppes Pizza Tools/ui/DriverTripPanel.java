package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JPanel;

import Structures.Delivery;
import planners.DriverOverview;
import utils.MapManager;
import Structures.Driver;

public class DriverTripPanel extends JPanel {
    private DriverOverview drivers;
    private ArrayList<Structures.Driver> driverList;
    private Rectangle screenBounds;
    private MapManager mapManager;
    private final Color boxColor = new Color(222, 219, 217);
    // private final Color boxColor = new Color(194, 189, 217);


    public DriverTripPanel(DriverOverview drivers, Rectangle screenBounds, MapManager mapManager) {
        this.drivers = drivers;
        this.driverList = drivers.getDrivers();
        this.screenBounds = screenBounds;
        this.mapManager = mapManager;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        try {
            drawDriverandTripOverview(g2d);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawDriverandTripOverview(Graphics2D g2d) throws Exception {
    if (driverList == null || driverList.isEmpty()) {
        return;
    }

    // Use this panel's own dimensions instead of screen/map calculations
    int panelWidth = getWidth();
    int panelHeight = getHeight();
    int driverBoxWidth = (int)(panelWidth * 0.3);
    int tripBoxWidth = panelWidth - driverBoxWidth;

    int numDrivers = driverList.size();
    int rowHeight = panelHeight / numDrivers;

    for (int i = 0; i < numDrivers; i++) {
        Driver driver = driverList.get(i);

        // rowX is now 0 — this panel starts at the left edge
        int rowX = 0;
        int rowY = i * rowHeight;

        // --- DRAW DRIVER SECTION ---
        Image driverImage = driver.getDriverPicture();
        if (driverImage != null) {
            int imgWidth = driverImage.getWidth(null);
            int imgHeight = driverImage.getHeight(null);

            int imgX = rowX + ((driverBoxWidth - imgWidth) / 2);
            int imgY = rowY + ((rowHeight - imgHeight) / 2) - 15;

            drawRoundedImage(g2d, driverImage, imgX, imgY, imgWidth, imgHeight, 30);

            g2d.setFont(new Font("Arial", Font.BOLD, 35));
            String driverName = driver.getName();
            FontMetrics metrics = g2d.getFontMetrics();
            int textWidth = metrics.stringWidth(driverName);

            int nameX = rowX + ((driverBoxWidth - textWidth) / 2);
            int nameY = imgY + imgHeight + 50;

            int nameBoxY = imgY + imgHeight;
            int nameBoxWidth = driverBoxWidth / 2;
            int nameBoxHeight = (rowY + rowHeight) - nameBoxY;
            int nameBoxX = imgX + ((imgWidth - nameBoxWidth) / 2);

            drawRoundedBox(g2d, nameBoxX, nameBoxY, nameBoxWidth, nameBoxHeight, 9, boxColor);

            g2d.setColor(Color.BLACK);
            g2d.drawString(driverName, nameX, nameY);
        }

        // --- DRAW TRIP(S) SECTION ---
        // tripSectionX is now relative to this panel, not the full screen
        int tripSectionX = driverBoxWidth;

        ArrayList<Delivery> driverTrips = driver.getDeliveriesForDriver();
        if (driverTrips != null && !driverTrips.isEmpty()) {
            int numTrips = driverTrips.size();
            int singleTripHeight = rowHeight / numTrips;
            int boxTopBuffer = 10;

            for (int j = 0; j < numTrips; j++) {
                Delivery trip = driverTrips.get(j);
                int currentTripY = rowY + (j * singleTripHeight) + boxTopBuffer;

                drawRoundedBox(g2d, tripSectionX, currentTripY, tripBoxWidth, singleTripHeight, i, boxColor);

                g2d.setFont(new Font("Arial", Font.BOLD, 30));
                FontMetrics metrics = g2d.getFontMetrics();

                String tripName = trip.getAddress().toString();
                int tripTextWidth = metrics.stringWidth(tripName);
                int tripTextHeight = metrics.getHeight();

                // tripTextX is now relative to tripSectionX, not rowX + driverBoxWidth
                int tripTextX = tripSectionX + ((tripBoxWidth - tripTextWidth) / 2);
                int tripTextY = currentTripY + ((singleTripHeight - tripTextHeight) / 2) - metrics.getAscent();

                g2d.setColor(Color.BLACK);
                g2d.drawString(tripName, tripTextX, tripTextY);

                Integer timeLeft = trip.minutesLeft();
                String timeText = "Time left: " + timeLeft.toString() + " min";

                int timeTextWidth = metrics.stringWidth(timeText);
                int timeTextHeight = metrics.getHeight();

                int timeTextX = tripSectionX + ((tripBoxWidth - timeTextWidth) / 2);
                int timeTextY = currentTripY + ((singleTripHeight - timeTextHeight) / 2);

                g2d.setColor(timeLeft > 0 ? new Color(46, 111, 64) : new Color(179, 27, 27));
                g2d.drawString(timeText, timeTextX, timeTextY + 50);
            }
        } else {
            g2d.setFont(new Font("Arial", Font.ITALIC, 14));
            g2d.drawString("No active deliveries", tripSectionX + 15, rowY + 30);
        }
    }
}

    private ArrayList<ArrayList<Delivery>> getDriversTrips() {
        ArrayList<ArrayList<Delivery>> activeTrips = new ArrayList<>();
        for (Driver driver : driverList) { 
            activeTrips.add(driver.getDeliveriesForDriver());
        }
        return activeTrips;
    }

    private void drawRoundedBox(Graphics2D g2d, int X, int Y, int width, int height, int padding, Color color) {
                    int gapX = 50;  // Horizontal space between boxes and row border
                    int gapY = 50;  // Vertical space between boxes and row border
                    int boxX = X + padding;
                    int boxY = Y + padding;
                    int boxW = width - (gapX / 2);
                    int boxH = height - (gapY / 2);
                    g2d.setColor(color);
                    g2d.fillRoundRect(boxX, boxY, boxW, boxH, 20, 20);
    }

    private void drawRoundedImage(Graphics2D g, Image img, int x, int y, int width, int height, int cornerRadius) {
        // Safety check just in case a driver is missing an image
        if (img == null) {
            return; 
        }

        // Create a Graphics2D copy so we don't affect the main Graphics object
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Turn on anti-aliasing for smooth, non-pixelated corners
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Define the rounded rectangle (the "cookie cutter")
        java.awt.geom.RoundRectangle2D roundedShape = new java.awt.geom.RoundRectangle2D.Float(x, y, width, height, cornerRadius, cornerRadius);

        // Apply the cookie cutter
        g2d.setClip(roundedShape);

        // Draw the image
        g2d.drawImage(img, x, y, width, height, null);

        // Dispose of our temporary Graphics2D object to reset the clip
        g2d.dispose();
    }
}