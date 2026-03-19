package Structures;
import java.sql.Time;

public class Trip {
    private Time orderTime;
    private Customer customer;
    private Address address;
    private Driver driver;

    public Trip(Time orderTime, Customer customer) {
        this.orderTime = orderTime;
        this.address = customer.getAddress();
        this.customer = customer;
    }

    public Time getOrderTime() {
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
