package planners;

import java.util.ArrayList;
import java.util.Iterator;

import Structures.Driver;

public class DriverOverview implements Iterable<Driver>{
    private ArrayList<Driver> drivers;

    public DriverOverview() {
        this.drivers = new ArrayList<>();
    }

    // Add a driver to a list
     public void addDriver(Driver driver) {
        drivers.add(driver);
    }

    // Return the list of drivers
    public ArrayList<Driver> getDrivers() {
        return drivers;
    }

    // Implement the Iterable interface
    @Override
    public Iterator<Driver> iterator() {
        return drivers.iterator();
    }
}

