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
        this.driverStatus = DriverStatus.Available;

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

    /**
     * Set new driverStatus
     * @return the new DriverStatus
     */
    public DriverStatus updateDriverStatus(DriverStatus status) {
        this.driverStatus = status;
        return driverStatus;
    }

    private void rescaleImage() {
        Integer newSize = 350;
        pfPicture = pfPicture.getScaledInstance(newSize, newSize, Image.SCALE_DEFAULT);    
    }

}
