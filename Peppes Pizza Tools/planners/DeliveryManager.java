package planners;

import java.util.ArrayList;

import Structures.Delivery;
import Structures.Driver;

public class DeliveryManager {
    private DeliveryOverview deliveries;
    private DriverOverview drivers;
    private ArrayList<Delivery> deliveryList;
    private ArrayList<Driver> driverList;


     public DeliveryManager(DeliveryOverview deliveries, DriverOverview drivers) {
        this.deliveries = deliveries;
        this.drivers = drivers;
        this.deliveryList = deliveries.getAllDeliveries();
        this.driverList = drivers.getDrivers();
    }

    public void assignDeliveryToDriver(Delivery delivery, Driver driver) {
        delivery.assignTripToDriver(driver);
        driver.addDelivery(delivery);
    }

    public DriverOverview getDriverOverview() {
        return drivers;
    }

    public DeliveryOverview getDeliveryOverview() {
        return deliveries;
    }

    public ArrayList<Driver> getDriverList() {
        return driverList;
    }

    public ArrayList<Delivery> getDeliveryList() {
        return deliveryList;
    }

    /**
     * Returns a list of unassigned, pending deliveries
     * @return a list of deliveries
     */
    public ArrayList<Delivery> getPendingDeliveries(){
        ArrayList<Delivery> inActiveDeliveries = new ArrayList<>();
        
        for(Delivery delivery : deliveryList) {
            if (delivery.getDriver() == null) {
                inActiveDeliveries.add(delivery);
            }
        }

        return inActiveDeliveries;
    }

    /**
     * Returns all deliveries that are currently being delivered or is assigned to a driver
     * @return a list of list of deliveres
     */
    public ArrayList<ArrayList<Delivery>> getDeliveriesInTransit() {
        ArrayList<ArrayList<Delivery>> activeDeliveries = new ArrayList<>();
        for (Driver driver : driverList) {
            ArrayList<Delivery> driverDeliveries = driver.getDeliveriesForDriver();
            if (driverDeliveries != null && !driverDeliveries.isEmpty()) {
                activeDeliveries.add(driverDeliveries);
            }
        }
        return activeDeliveries;
        }
}
