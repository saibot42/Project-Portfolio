package ui;

import javax.management.AttributeChangeNotificationFilter;
import javax.swing.*;

import org.w3c.dom.css.RGBColor;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.EOFException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import Structures.*;
import planners.DriverOverview;
import planners.TripOverview;
import utils.*;

public class dashboardGUI extends JPanel {
    private MapManager manager;
    private TripOverview trips;
    private DriverOverview drivers;
    private Image mapImage;
    private ArrayList<Trip> tripList;
    private ArrayList<Driver> driverList;
    private Rectangle screenBounds;

    public dashboardGUI(MapManager manager, TripOverview trips, DriverOverview drivers, Rectangle screeBounds) {
        this.manager = manager;
        this.trips = trips;
        this.drivers = drivers;
        this.mapImage = manager.getMapImage();
        this.screenBounds = screeBounds;

        this.tripList = trips.getAllTrips();
        this.driverList = drivers.getDrivers();
        setLayout(null);
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Get the image's width and height
        int imgWidth = mapImage.getWidth(null);
        int imgHeight = mapImage.getHeight(null);

        g.drawImage(mapImage, 0, 0, imgWidth, imgHeight, null);
        drawGrid(g);
        
        highlightDeliveryLocations(g);

        try {
            drawDriverandTripOverview(g);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    // Draw the grid cells
    private void drawGrid(Graphics g) {
        // g.setColor(new Color(255, 255, 255, 50));  // Semi-transparent grid lines
        g.setColor(new Color(0, 0, 0, 0)); // Fully transparent

        for (int x = 0; x < manager.getPixelGrid().getGridWidth(); x++) {
            for (int y = 0; y < manager.getPixelGrid().getGridHeight(); y++) {
                g.drawRect(x * manager.getPixelGrid().getCellWidth(), 
                           y * manager.getPixelGrid().getCellHeight(),
                           manager.getPixelGrid().getCellWidth(), 
                           manager.getPixelGrid().getCellHeight());
            }
        }
    }

    // Highlight delivery locations (This should ideally come from a dynamic list of addresses)
    private void highlightDeliveryLocations(Graphics g) {
        g.setColor(Color.RED); // Color for highlights
         // Get the width and height of a grid cell
        int cellWidth = manager.getPixelGrid().getCellWidth();
        int cellHeight = manager.getPixelGrid().getCellHeight();

        // Example list of addresses (replace with actual addresses)
        for (Trip trip : trips) {
            Pixel pixel = manager.ConvertMapToGrid(trip.getAddress().getMapCoordinate());  // Convert to pixel
            g.fillRect(pixel.getX() * cellWidth, pixel.getY() * cellHeight, cellWidth, cellHeight);
        }
    }

    private void drawDriverandTripOverview(Graphics g) throws Exception {
        if (driverList == null || driverList.isEmpty()) {
            return; // Nothing to draw if the list is empty
        }

        //Define the main layout areas
        int mapWidth = mapImage.getWidth(null);
        int screenWidth = screenBounds.width;
        int screenHeight = screenBounds.height;
        
        // The width available to the right of the map
        int remainingWidth = screenWidth - mapWidth;
        
        // Dedicate 30% of the remaining width to the driver , which leaves 70% to the trips
        int driverBoxWidth = (int)(remainingWidth * 0.3);
        int tripBoxWidth = remainingWidth - driverBoxWidth;

        // Calculate the height of each driver's row dynamically
        int numDrivers = driverList.size();
        int rowHeight = screenHeight / numDrivers;

        for (int i = 0; i < numDrivers; i++) {
            Driver driver = driverList.get(i);
            
            // Base starting coordinates for this specific driver's row
            int rowX = mapWidth;
            int rowY = i * rowHeight;

            // --- DRAW DRIVER SECTION ---
            g.setColor(Color.BLACK);
            //g.drawRect(rowX, rowY, driverBoxWidth, rowHeight); // Draw the border for the driver section

            Image driverImage = driver.getDriverPicture();
            if (driverImage != null) {
                // Determine a good size for the image
                int imgSize = Math.min(driverBoxWidth - 40, rowHeight - 60); 
                
                // Center Horizontally: Half of the leftover space in the box
                int imgX = rowX + ((driverBoxWidth - imgSize) / 2);
                
                // Center Vertically: Offset slightly upwards (-15) to leave room for the name below
                int imgY = rowY + ((rowHeight - imgSize) / 2) - 15;
                
                drawRoundedImage(g, driverImage, imgX, imgY, imgSize, imgSize, 30);

                // Draw drivers name under the picture (Using the relative imgY coordinate)
                g.setFont(new Font("Arial", Font.BOLD, 22));
                String driverName = driver.getName();

                //Calculate the width of the drivers name
                FontMetrics metrics = g.getFontMetrics();
                int textWidth = metrics.stringWidth(driverName);

                int nameX = rowX + ((driverBoxWidth - textWidth) / 2);
                int nameY = imgY + imgSize + 25; // Place text below the image
                g.drawString(driver.getName(), nameX, nameY);
            }

            // --- DRAW TRIP SECTION ---
            int tripSectionX = rowX + driverBoxWidth;
            
            // Draw the outer border for the entire trip section of this driver
            //g.drawRect(tripSectionX, rowY, tripBoxWidth, rowHeight); 

            ArrayList<Trip> driverTrips = driver.getTripsForDriver();
            if (driverTrips != null && !driverTrips.isEmpty()) {
                int numTrips = driverTrips.size();
                // If a driver has multiple trips, divide the row height among them
                int singleTripHeight = rowHeight / numTrips;

                for (int j = 0; j < numTrips; j++) {
                    g.setColor(Color.BLACK);
                    Trip trip = driverTrips.get(j);
                    
                    // Calculate the Y position for this specific trip
                    int currentTripY = rowY + (j * singleTripHeight);

                    String tripName = trip.getAddress().toString();

                    //Calculate the width of the drivers name
                    FontMetrics metrics = g.getFontMetrics();
                    int tripTextWidth = metrics.stringWidth(tripName);
                    int tripTextHeight = metrics.getHeight();

                    // Setup coordinates for the text inside the trip box
                    int tripTextX =  (rowX + driverBoxWidth) + ((tripBoxWidth - tripTextWidth) / 2);
                    int tripTextY = rowY + ((singleTripHeight - tripTextHeight) / 2); 

                    g.setFont(new Font("Arial", Font.PLAIN, 30));
                    g.drawString(tripName, tripTextX, tripTextY);

                    Integer timeLeft = trip.minutesLeft();
                    String timeText = "Time left: " + timeLeft.toString() + " min";
                    
                    if(timeLeft > 0) {
                        g.setColor(new Color(46,111,64));
                    } else {
                        g.setColor(new Color(179,27,27));
                    }
                    
                    //Calculate the width of the time text
                    int timeTextWidth = metrics.stringWidth(timeText);
                    int timeTextHeight = metrics.getHeight();


                    //Coordinates of timeText
                    int timeTextX = (rowX + driverBoxWidth) + ((tripBoxWidth - timeTextWidth) / 2);
                    int timeTextY = rowY + ((singleTripHeight - timeTextHeight) / 2); ;

                    g.drawString(timeText, timeTextX, timeTextY + 50);

                }
            } else {
                // Handle edge case where a driver is listed but has no active trips
                g.setFont(new Font("Arial", Font.ITALIC, 14));
                g.drawString("No active trips", tripSectionX + 15, rowY + 30);
            }
        }
    }

    private ArrayList<ArrayList<Trip>> getDriversTrips() {
        ArrayList<ArrayList<Trip>> activeTrips = new ArrayList<>();
        for (Driver driver : driverList) { 
            activeTrips.add(driver.getTripsForDriver());
        }
        return activeTrips;
    } 

    private ArrayList<Trip> generateListofUnassignedTrips() throws Exception {
        //List of unassigned trips
        ArrayList<Trip> inActiveTrips = new ArrayList<>();
        
        for(Trip trip : tripList) {
            if (trip.getDriver() == null) {
                inActiveTrips.add(trip);
            } else {
                throw new Exception();
            }
        }

        return inActiveTrips;
    }

    private void drawRoundedImage(Graphics g, Image img, int x, int y, int width, int height, int cornerRadius) {
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
