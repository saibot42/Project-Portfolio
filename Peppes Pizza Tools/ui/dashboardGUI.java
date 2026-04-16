package ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import Structures.*;
import planners.DriverOverview;
import planners.DeliveryOverview;
import utils.*;

public class dashboardGUI extends JPanel {
    private MapManager manager;
    private DeliveryOverview deliveries;
    private DriverOverview drivers;
    private Address pAddress;
    private Image peppesIcon;
    private Image mapImage;
    private ArrayList<Delivery> tripList;
    private ArrayList<Driver> driverList;
    private Rectangle screenBounds;
    private final Color boxColor = new Color(222, 219, 217);

    public dashboardGUI(MapManager manager, DeliveryOverview deliveries, DriverOverview drivers, Rectangle screeBounds, Address pAddress) {
        this.manager = manager;
        this.deliveries = deliveries;
        this.drivers = drivers;
        this.pAddress = pAddress;
        this.peppesIcon = new ImageIcon("assets/peppesIcon.png").getImage();
        this.mapImage = manager.getMapImage();
        this.screenBounds = screeBounds;

        this.tripList = deliveries.getAllDeliveries();
        this.driverList = drivers.getDrivers();
        setLayout(null);
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawGrid(g2d);

        drawMap(g2d);
        
        highlightDeliveryLocations(g2d);

        try {
            drawDriverandTripOverview(g2d);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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

        for (int x = 0; x < manager.getPixelGrid().getGridWidth(); x++) {
            for (int y = 0; y < manager.getPixelGrid().getGridHeight(); y++) {
                g2d.drawRect(x * manager.getPixelGrid().getCellWidth(), 
                           y * manager.getPixelGrid().getCellHeight(),
                           manager.getPixelGrid().getCellWidth(), 
                           manager.getPixelGrid().getCellHeight());
            }
        }
    }

    // Highlight delivery locations
    private void highlightDeliveryLocations(Graphics2D g2d) {
        g2d.setColor(Color.RED); // Color for highlights
         // Get the width and height of a grid cell
        int cellWidth = manager.getPixelGrid().getCellWidth();
        int cellHeight = manager.getPixelGrid().getCellHeight();

        //Add Peppes Pizza Restaraunt to the map

        Pixel peppesLocationPixel = manager.ConvertMapToGrid(pAddress.getMapCoordinate());  // Convert to pixel
        g2d.drawImage(peppesIcon, peppesLocationPixel.getX() * cellWidth, peppesLocationPixel.getY() * cellHeight, cellWidth, cellHeight, null);

        //Add every delivery
        for (Delivery trip : deliveries) {
            Pixel pixel = manager.ConvertMapToGrid(trip.getAddress().getMapCoordinate());  // Convert to pixel
            g2d.fillRect(pixel.getX() * cellWidth, pixel.getY() * cellHeight, cellWidth, cellHeight);
        }
    }

    private void drawDriverandTripOverview(Graphics2D g2d) throws Exception {
        if (driverList == null || driverList.isEmpty()) {
            return; // Nothing to draw if the list is empty
        }

        //Define the main layout areas
        int screenWidth = screenBounds.width;
        int screenHeight = screenBounds.height;
        int mapWidth = mapImage.getWidth(null);
        int remainingWidth = screenWidth - mapWidth; // Usable space after drawing the map
        int driverBoxWidth = (int)(remainingWidth * 0.3); // Driver info (profile picture, name, shift length)
        int tripBoxWidth = remainingWidth - driverBoxWidth; // Trip info (Address, time left until delivery is late, estimated return time)

        // Calculate the height of each driver's row dynamically
        int numDrivers = driverList.size();
        int rowHeight = screenHeight / numDrivers;

        for (int i = 0; i < numDrivers; i++) {
            Driver driver = driverList.get(i);
            
            // Base starting coordinates for this specific driver's row
            int rowX = mapWidth;
            int rowY = i * rowHeight;

            // --- DRAW DRIVER SECTION ---
            Image driverImage = driver.getDriverPicture();
            if (driverImage != null) {
                int imgWidth = driverImage.getWidth(null);
                int imgHeight = driverImage.getHeight(null);
                
                // Center Horizontally and Vertically
                int imgX = rowX + ((driverBoxWidth - imgWidth) / 2);
                int imgY = rowY + ((rowHeight - imgHeight) / 2) - 15; // Offset slightly upwards (-15) to leave room for the name below
                
                drawRoundedImage(g2d, driverImage, imgX, imgY, imgWidth, imgHeight, 30);

                // -- DRAW DRIVER INFO SECTION -- \\
                g2d.setFont(new Font("Arial", Font.BOLD, 35));
                String driverName = driver.getName();

                //Calculate the width of the drivers name
                FontMetrics metrics = g2d.getFontMetrics();
                int textWidth = metrics.stringWidth(driverName);

                int nameX = rowX + ((driverBoxWidth - textWidth) / 2);
                int nameY = imgY + imgHeight + 50; // Place text below the image

                int nameBoxY = imgY + imgHeight;
                int nameBoxWidth = driverBoxWidth / 2;
                int nameBoxHeight = (rowY + rowHeight) - nameBoxY;
                int nameBoxX = imgX + ((imgWidth - nameBoxWidth) / 2);

                drawRoundedBox(g2d, nameBoxX, nameBoxY, nameBoxWidth, nameBoxHeight, 9, boxColor);

                g2d.setColor(Color.BLACK);
                g2d.drawString(driver.getName(), nameX, nameY);

                String driverStatus = driver.getDriverStatus().toString();
                System.out.println(driverStatus);
            }

            // --- DRAW TRIP(S) SECTION ---
            int tripSectionX = rowX + driverBoxWidth;

            ArrayList<Delivery> driverTrips = driver.getDeliveriesForDriver(); // Get all current deliveries for the driver we are working on
            if (driverTrips != null && !driverTrips.isEmpty()) {
                int numTrips = driverTrips.size();
                int singleTripHeight = rowHeight / numTrips; // Driver has multiple deliveries -> divide the row height among them
                int boxTopBuffer = 10; // Buffer / padding -> Used to increase the spaces between trip boxes

                for (int j = 0; j < numTrips; j++) {
                    Delivery trip = driverTrips.get(j);
                    int currentTripY = rowY + (j * singleTripHeight) + boxTopBuffer; // Calculate the Y position for this specific trip

                    // -- TRIP BACKROUND BOX -- \\
                    drawRoundedBox(g2d, tripSectionX, currentTripY, tripBoxWidth, singleTripHeight, i, boxColor);

                    // --DRAW TRIP INFORMATION
                    g2d.setFont(new Font("Arial", Font.BOLD, 30));
                    FontMetrics metrics = g2d.getFontMetrics();

                    String tripName = trip.getAddress().toString();

                    //Calculate the width of the drivers name
                    int tripTextWidth = metrics.stringWidth(tripName);
                    int tripTextHeight = metrics.getHeight();

                    // Setup coordinates for the text inside the trip box
                    int tripTextX =  (rowX + driverBoxWidth) + ((tripBoxWidth - tripTextWidth) / 2);
                    int tripTextY = currentTripY + ((singleTripHeight - tripTextHeight) / 2) - metrics.getAscent(); 

                    g2d.setColor(Color.BLACK);
                    g2d.drawString(tripName, tripTextX, tripTextY);

                    Integer timeLeft = trip.minutesLeft();
                    String timeText = "Time left: " + timeLeft.toString() + " min";
                    
                    //Calculate the width of the time text
                    int timeTextWidth = metrics.stringWidth(timeText);
                    int timeTextHeight = metrics.getHeight();

                    //Coordinates of timeText
                    int timeTextX = (rowX + driverBoxWidth) + ((tripBoxWidth - timeTextWidth) / 2);
                    int timeTextY = currentTripY + ((singleTripHeight - timeTextHeight) / 2); ;

                    g2d.setColor(timeLeft > 0 ? new Color(46, 111, 64) : new Color(179, 27, 27));
                    g2d.drawString(timeText, timeTextX, timeTextY + 50);

                }
            } else {
                // Handle edge case where a driver is listed but has no active deliveries
                g2d.setFont(new Font("Arial", Font.ITALIC, 14));
                g2d.drawString("No active deliveries", tripSectionX + 15, rowY + 30);
            }

            //g2d.drawLine(rowX, rowY, remainingWidth, 5); // Draw a line at the bottom of the top and bottow of the driver (bug)
        }
    }

    private ArrayList<ArrayList<Delivery>> getDriversTrips() {
        ArrayList<ArrayList<Delivery>> activeTrips = new ArrayList<>();
        for (Driver driver : driverList) { 
            activeTrips.add(driver.getDeliveriesForDriver());
        }
        return activeTrips;
    } 

    private ArrayList<Delivery> generateListofUnassignedTrips() throws Exception {
        //List of unassigned deliveries
        ArrayList<Delivery> inActiveTrips = new ArrayList<>();
        
        for(Delivery trip : tripList) {
            if (trip.getDriver() == null) {
                inActiveTrips.add(trip);
            } else {
                throw new Exception();
            }
        }

        return inActiveTrips;
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
