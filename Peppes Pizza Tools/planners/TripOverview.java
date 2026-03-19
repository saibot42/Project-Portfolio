package planners;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.Iterator;

import Structures.Trip;

public class TripOverview implements Iterable<Trip>{
    private ArrayList<Trip> trips;

    public TripOverview() {
        this.trips = new ArrayList<>();
    }

    // Add a trip to the list
     public void addTrip(Trip trip) {
        trips.add(trip);
    }

    // Return the list of trips
    public ArrayList<Trip> getAllTrips() {
        return trips;
    }

    // Implement the Iterable interface
    @Override
    public Iterator<Trip> iterator() {
        return trips.iterator();
    }
}
