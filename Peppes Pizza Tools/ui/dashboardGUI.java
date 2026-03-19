package ui;

import javax.management.AttributeChangeNotificationFilter;
import javax.swing.*;
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
        //Think I want to a display each drivers as its own block with what trips they are on

        g.setColor(Color.BLACK);
        //g.drawRect(100, 200, 500, 500); //I want to draw the square where the drivers should be visualized
        
        // Get the image's width and height
        int MapimgWidth = mapImage.getWidth(null);
        int MapimgHeight = mapImage.getHeight(null);

        Integer driversWidth = screenBounds.width - MapimgWidth;
        Integer driverHeight = screenBounds.height / driverList.size();

        for (int i = 0; i < driverList.size(); i++) {
            Driver driver = driverList.get(i);
            ArrayList<Trip> driverTrips = driver.getTripsForDriver();
            int x = MapimgWidth;
            int y = i * driverHeight;
            int margin = 0;
            Image driverImage = driver.getDriverPicture();

            //Draw border
            g.setColor(Color.BLACK);
            g.drawRect(x, y, driversWidth, driverHeight);


            //TODO: Set the drawImage width and height to driversheight and the width as scaled. Make sure its scaled and not zoomed in
            g.drawImage(driverImage, x + margin, y + margin, driverImage.getWidth(null), driverImage.getWidth(null), null);

            //Draw drivers name (under picture)
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString(driver.getName(), x + margin, driverImage.getHeight(null) + 20);
            
            //Draw drivers trips
            for (int j = 0; i < driverTrips.size(); i++) {
                Trip trip = driverTrips.get(j);
                int tripX = driverImage.getWidth(null);
                int tripY = (driverHeight / driverTrips.size()) * j;
                
                g.setColor(Color.BLACK);
                g.drawRect(tripX, tripY, MapimgWidth, MapimgHeight);
                
                //Draw address
                ///DO WE NEED THEESE?
                // g.setColor(Color.BLACK);
                // g.setFont(new Font("Arial", Font.BOLD, 16));
                g.drawString(trip.getAddress().toString(), tripX, tripY);               
                g.drawString("hello", tripX, tripY);

                //Draw orderTime
                g.drawString(trip.getOrderTime().toString(), tripX, tripY);

            }
        }

        ArrayList<Trip> inActiveTrips = generateListofUnassignedTrips();
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

}
