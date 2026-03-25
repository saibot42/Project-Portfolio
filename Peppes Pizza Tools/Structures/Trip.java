package Structures;
import java.time.LocalDateTime;
import java.time.Duration;

public class Trip {
    private LocalDateTime orderTime;
    private Customer customer;
    private Address address;
    private Driver driver;

    public Trip(LocalDateTime orderTime, Customer customer) {
        this.orderTime = orderTime;
        this.address = customer.getAddress();
        this.customer = customer;
    }

    /**
     * Calculates how many minutes there are left before the trip is considered "late".
     * 
     * Within 60 minutes of order time
     * 
     * @return Integer minutes until trip is late
     */
    public Integer minutesLeft() {
        if (orderTime == null) {
            return 0; // Safety check
        }

        // Calculate the difference between the order time and right now
        LocalDateTime now = LocalDateTime.now();
        long minutesPassed = Duration.between(orderTime, now).toMinutes();
        
        // Subtract the minutes passed from the 60-minute limit
        return (int) (60 - minutesPassed);
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }
    
    public Customer getCustomer() {
        return customer;
    }

    public Address getAddress() {
        return address;
    }

    public void assignTripToDriver(Driver driver) {
        this.driver = driver;
    }

    public Driver getDriver() throws Exception {
        if (driver != null) {
            return driver;
        } else {
            throw new Exception("There is no driver assigned to this trip");
        }
    }

    public Integer calculateTripTime(Address startAdd, Address endAdd) {
        //TODO: Get information OSRM. Forked the project on my own account, but need ssh key to clone it
        return 0;
    }
    

}
