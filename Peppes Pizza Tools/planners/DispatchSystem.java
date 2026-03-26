package planners;

import java.rmi.UnexpectedException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

import Structures.Trip;
import graph.V;
import graph.WeightedGraph;

public class DispatchSystem {
    private PriorityQueue<Trip> queue;
    private WeightedGraph<V, Integer> graph;
    private TripOverview trips;
    private DriverOverview driver;
    
    public DispatchSystem(WeightedGraph<V, Integer> graph, TripOverview trips, DriverOverview driver) {
        this.graph = graph;
        this.trips = trips;
        this.driver = driver;

        PriorityQueue<Trip> queue = new PriorityQueue<>(trips.getAllTrips()); // Initial priority based on

        
    }

    public ArrayList<Trip> nextDrive() {
        ArrayList<Trip> nextDrive = new ArrayList<Trip>();
        //TODO: This is not a good way to handle trips. Either we need to use graph (best) or TripOverview (easy)
        Trip firstTrip = queue.peek();
        //V vTrip = graph.vertices().iterator().next();

        LocalDateTime orderTime = firstTrip.getOrderTime();
        Integer deliveryTime = firstTrip.calculateTripTime(null, null);
        

        return nextDrive;
    }
}
