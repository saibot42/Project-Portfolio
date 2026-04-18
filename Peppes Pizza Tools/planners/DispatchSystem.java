package planners;

import java.rmi.UnexpectedException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;


import Structures.Delivery;
import graph.V;
import graph.WeightedGraph;
import Structures.Driver;
import Structures.Driver.DriverStatus;

public class DispatchSystem {
    private PriorityQueue<Delivery> deliveryQueue;  
    private PriorityQueue<Driver> driverQueue;
    private WeightedGraph<V, Integer> graph;
    private DeliveryOverview deliveries;
    private DriverOverview drivers;
    
    public DispatchSystem(WeightedGraph<V, Integer> graph, DeliveryOverview deliveries, DriverOverview drivers) {
        this.graph = graph;
        this.deliveries = deliveries;
        this.drivers = drivers;
        this.deliveryQueue = new PriorityQueue<>(
            Comparator.comparing(Delivery::getOrderTime));
        this.deliveryQueue.addAll(deliveries.getAllDeliveries()); // Initial priority based on

        // this.driverQueue = new PriorityQueue<>(
        //     Comparator.comparing(Driver::driverStatus)
        // );
        // this.driverQueue.addAll(drivers.getDrivers());
        
    }

    /**
     * Builds the next cluster of deliveries to dispatch together.
     * Uses the most urgent delivery as a seed, then greedily adds
     * geographically close deliveries that won't cause the seed to be late.
     */
    public ArrayList<Delivery> nextDrive() {
        /*
        1. Sort all pending deliveries by deadline (most urgent first)
        2. Take the most urgent trip as the "seed" of a cluster
        3. Look at all remaining deliveries and ask: can I add this trip to the route without making the seed trip late?
        4. Among those that pass the time check, pick the geographically closest one
        5. Repeat step 3-4 until no more deliveries can be added without causing lateness
        6. Dispatch that cluster
        */

        if (deliveryQueue.isEmpty()) return new ArrayList<>();

        ArrayList<Delivery> cluster = new ArrayList<>();

        Delivery seed = deliveryQueue.poll();
        cluster.add(seed);

        LocalDateTime now = LocalDateTime.now();
        long minutesUntilSeedLate = seed.minutesLeft();

        List<Delivery> remaining = new ArrayList<>(deliveryQueue);
        List<Delivery> added = new ArrayList<>();

        for (Delivery candidate : remaining) {
            //int estimatedRouteTime = candidate.calculateTripTime();

        }
        

        return cluster;
    }

    /**
     * Assigns a cluster to the first available driver.
     * If no driver is currently available, waits for the soonest one.
     * After assignment, the driver goes to the back of the queue.
     */
    public void assignClusterToDriver(ArrayList<Delivery> cluster) {
        if (cluster.isEmpty() || driverQueue.isEmpty()) return;

        // The queue is sorted by availability -> peek at the soonest available driver
        Driver driver = driverQueue.poll();

        DriverStatus status = driver.getDriverStatus();

        // Only assign if driver is AVAILABLE or COMING_BACK (not already on a trip)
        if (status == DriverStatus.Available ||
            status == DriverStatus.ComingBack) {

            for (Delivery delivery : cluster)
                driver.addDelivery(delivery);

            driverQueue.add(driver); // Back of the queue (sorted by availability time)

        } else {
            // Driver is already on a trip -> put back and try next dispatch cycle
            driverQueue.add(driver);
        }
    }
}
