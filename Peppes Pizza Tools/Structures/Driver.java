package Structures;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Driver {
    private String name;
    private Image pfPicture;
    private ArrayList<Delivery> activeDeliveries;
    private DriverStatus driverStatus;

    public Driver(String name, String imagePath) {
        this.name = name;
        this.pfPicture = loadImage(imagePath);
        this.activeDeliveries = new ArrayList<>();
        this.driverStatus = DriverStatus.AVAILABLE;
        rescaleImage();
    }

    public enum DriverStatus {
        AVAILABLE,
        ON_TRIP,
        COMING_BACK,
        ON_BREAK;

        @Override
        public String toString() {
            switch (this) {
                case AVAILABLE:    return "Available";
                case ON_TRIP:      return "On a trip";
                case COMING_BACK:  return "Coming back";
                case ON_BREAK:     return "On break";
                default:           return "Unknown";
            }
        }
    }

    public String getName() { return name; }
    public Image getDriverPicture() { return pfPicture; }
    public DriverStatus getDriverStatus() { return driverStatus; }
    public ArrayList<Delivery> getDeliveriesForDriver() { return activeDeliveries; }

    // --- Availability helpers used by dispatch system ---
    public boolean isAvailable() {
        return driverStatus == DriverStatus.AVAILABLE;
    }

    public boolean willBeSoonAvailable() {
        return driverStatus == DriverStatus.COMING_BACK;
    }

    public boolean isUnavailable() {
        return driverStatus == DriverStatus.ON_TRIP
            || driverStatus == DriverStatus.ON_BREAK;
    }

    public void addDelivery(Delivery delivery) {
        activeDeliveries.add(delivery);
        driverStatus = DriverStatus.ON_TRIP;
    }

    public void removeDelivery(Delivery delivery) {
        activeDeliveries.remove(delivery);
        if (activeDeliveries.isEmpty()) {
            driverStatus = DriverStatus.COMING_BACK;
        }
        // If there are still deliveries, status stays ON_TRIP
    }

    // Called when driver physically arrives back at the restaurant
    public void arrivedAtRestaurant() {
        if (driverStatus == DriverStatus.COMING_BACK) {
            driverStatus = DriverStatus.AVAILABLE;
        }
    }

    // Called when driver starts/ends a break
    public void startBreak() {
        if (isAvailable()) {
            driverStatus = DriverStatus.ON_BREAK;
        }
    }

    public void endBreak() {
        if (driverStatus == DriverStatus.ON_BREAK) {
            driverStatus = DriverStatus.AVAILABLE;
        }
    }

    private Image loadImage(String path) {
        Image img = new ImageIcon(path).getImage();
        if (img == null || img.getWidth(null) <= 0) {
            return new ImageIcon("/assets/default_pf.jpg").getImage();
        }
        return img;
    }
    private void rescaleImage() {
        pfPicture = pfPicture.getScaledInstance(350, 350, Image.SCALE_DEFAULT);
    }
}