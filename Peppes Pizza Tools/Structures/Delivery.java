package Structures;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import graph.V;
import graph.WeightedGraph;

import java.time.Duration;

public class Delivery {
    private LocalDateTime orderTime;
    private LocalDateTime orderedForTime;
    private Customer customer;
    private Address address;
    private Driver driver;
    private OrderType orderType;

    private static final Integer TimeItTakesToOrder = 60;

    public Delivery(LocalDateTime orderTime, LocalDateTime orderedForTime, Customer customer) {
        this.orderTime = orderTime;
        this.orderedForTime = orderedForTime;
        this.address = customer.getAddress();
        this.customer = customer;
        
        // Calculate and set the order type immediately upon creation
        this.orderType = determineOrderType();
    }

    public enum DeliveryStatus {
        ON_TIME,
        WARNING,
        LATE
    }

    public enum OrderType {
        ASAP,
        PREORDER
    }

    /**
     * Helper method to determine the order type based on the time gap.
     */
    private OrderType determineOrderType() {
        if (orderedForTime == null) {
            return OrderType.ASAP;
        }

        long minutesBetween = Duration.between(orderTime, orderedForTime).toMinutes();

        if (minutesBetween > TimeItTakesToOrder) {
            return OrderType.PREORDER;
        } else {
            return OrderType.ASAP;
        }
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public DeliveryStatus getStatus() {
        int mins = minutesLeft();
        if (mins <= 0)  return DeliveryStatus.LATE;
        if (mins <= 15) return DeliveryStatus.WARNING;
        return DeliveryStatus.ON_TIME;
    }

    /**
     * Calculates how many minutes there are left before the trip is considered "late".
     * Dynamically adjusts based on whether it is an ASAP or PREORDER delivery.
     * * @return Integer minutes until trip is late
     */
    public Integer minutesLeft() {
        if (orderTime == null) {
            return 0; // Safety check
        }

        LocalDateTime now = LocalDateTime.now();

        if (orderType == OrderType.ASAP) {
            // ASAP: Due 60 minutes from orderTime
            long minutesPassed = Duration.between(orderTime, now).toMinutes();
            return (int) (TimeItTakesToOrder - minutesPassed);
        } else {
            // PREORDER: Due exactly at orderedForTime
            // Duration between NOW and the FUTURE ordered time
            return (int) Duration.between(now, orderedForTime).toMinutes();
        }
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public LocalDateTime getOrderedForTime() {
        return orderedForTime;
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
        WeightedGraph<V, Integer> graph = new WeightedGraph<>();
        
        return 0;
    }

    public String getFormattedOrderedTime() {
        if (orderedForTime == null) {
            return "ASAP";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return orderedForTime.format(formatter);
    }
}