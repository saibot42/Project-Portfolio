package Structures;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

import graph.Graph;
import graph.V;
import graph.WeightedGraph;

import java.time.Duration;

public class Delivery {
    private LocalDateTime orderTime;
    private Customer customer;
    private Address address;
    private Driver driver;

    public Delivery(LocalDateTime orderTime, Customer customer) {
        this.orderTime = orderTime;
        this.address = customer.getAddress();
        this.customer = customer;
    }

    public enum DeliveryStatus {
        ON_TIME,
        WARNING,
        LATE
    }

    public DeliveryStatus getStatus() {
        int mins = minutesLeft();
        if (mins <= 0)  return DeliveryStatus.LATE;
        if (mins <= 15) return DeliveryStatus.WARNING;
        return DeliveryStatus.ON_TIME;
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

    public Driver getDriver() {
        return driver;
    }

    public Integer calculateTripTime(ArrayList<Delivery> cluster) {
        WeightedGraph<V, Integer> clusterGraph = new WeightedGraph<>();
        WeightedGraph<V, Integer> graph = new WeightedGraph<>();;
        
        return 0;
    }
    

}
