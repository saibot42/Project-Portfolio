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
        // Ask Java to look inside the JAR file (the "backpack")
        java.net.URL imgUrl = Driver.class.getResource(path);
        
        if (imgUrl != null) {
            return new ImageIcon(imgUrl).getImage();
        } else {
            System.out.println("Could not find image: " + path + " - loading default profile.");
            
            // If the specific driver image isn't found, load the fallback.
            // Remember to use the capital 'A' for Assets!
            java.net.URL fallbackUrl = Driver.class.getResource("/Assets/default_pf.jpg");
            
            if (fallbackUrl != null) {
                return new ImageIcon(fallbackUrl).getImage();
            } else {
                System.out.println("CRITICAL: Could not find the default profile picture either!");
                // Return a completely blank image to prevent the app from crashing entirely
                return new java.awt.image.BufferedImage(350, 350, java.awt.image.BufferedImage.TYPE_INT_ARGB);
            }
        }
    }

    private void rescaleImage() {
        //TODO: Dynamix rescaling based on resoltion and not 
        pfPicture = pfPicture.getScaledInstance(262, 262, Image.SCALE_DEFAULT); // Suits 1920x1080
        //pfPicture = pfPicture.getScaledInstance(350, 350, Image.SCALE_DEFAULT); // Suits 2560x1440

    }
}