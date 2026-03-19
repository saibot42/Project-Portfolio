package Structures;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Driver {
    private String name;
    private Image pfPicture;
    private ArrayList<Trip> activeTrips;
    private DriverStatus driverStatus;

    public Driver(String name, Image pfPicture) {
        this.name = name;
        this.pfPicture = pfPicture;
        this.activeTrips = new ArrayList<>();

        rescaleImage();
    }

    public String getName() {
        return name;
    }

    public Image getDriverPicture() {
        return pfPicture;
    }

    public ArrayList<Trip> getTripsForDriver() {
        return activeTrips;
    }

    public void addTrip(Trip trip) {
        activeTrips.add(trip);
    }

    public DriverStatus getDriverStatus() {
        return driverStatus;
    }

    private void rescaleImage() {
        Integer newWidth = 200;
        Integer newHeight = 200;
        pfPicture = pfPicture.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);    
    }

}
