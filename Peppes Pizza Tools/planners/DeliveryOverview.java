package planners;
import java.util.ArrayList;
import java.util.Iterator;

import Structures.Delivery;

public class DeliveryOverview implements Iterable<Delivery>{
    private ArrayList<Delivery> deliveries;

    public DeliveryOverview() {
        this.deliveries = new ArrayList<>();
    }

    // Add a delivery to the list
     public void addDelivery(Delivery delivery) {
        deliveries.add(delivery);
    }

    // Return the list of deliveries
    public ArrayList<Delivery> getAllDeliveries() {
        return deliveries;
    }

    // Implement the Iterable interface
    @Override
    public Iterator<Delivery> iterator() {
        return deliveries.iterator();
    }
}
