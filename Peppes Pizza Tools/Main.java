import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import java.awt.*;

import Structures.*;
import utils.*;
import ui.*;
import graph.V;
import graph.WeightedGraph;
import planners.Clustering;
import planners.DriverOverview;
import planners.DeliveryOverview;
import planners.calculateDistances;
import planners.Clustering;
import utils.MapManager;

public class Main {
    
    public static void main(String[] args) throws Exception {

        Address peppesAddress = new Address("Åsamyrane", 82, 5116, "Ulset", new MapCoordinate(60.46143652267689, 5.322624149347732));
        ArrayList<Customer> customers = createCustomers();
        DeliveryOverview deliveryOverview = createDeliveries(customers);
        DriverOverview driverOverview = createDrivers(deliveryOverview);

        startGUI(deliveryOverview, driverOverview);

        //WeightedGraph<V, Integer> graph = createGraph(peppesAddress, customers);

        //Clustering clustering = new Clustering(graph, graph.getFirstNode());
    }

    private static ArrayList<Customer> createCustomers() {
        ArrayList<Customer> customers = new ArrayList<Customer>();

        customers.add(new Customer("John", "Doe", new Address("Jens Rolfsensgate", 8, 5032, "Bergen", new MapCoordinate(60.40216530414372, 5.325331675564423))));
        customers.add(new Customer("May", "Andersen", new Address("Nordre toppe", 8, 5136, "Mjølkeråen", new MapCoordinate(60.48576050440339, 5.2735504196192))));
        customers.add(new Customer("Emma", "Svensen", new Address("Prestestien", 4, 5118, "Ulset", new MapCoordinate(60.4674790360069, 5.309251949428394))));
        customers.add(new Customer("Lucas", "Pettersen", new Address("Breimyra", 256, 5134, "Flaktveit", new MapCoordinate(60.46287002092276, 5.362031604966438))));
        customers.add(new Customer("Ola", "Jensen", new Address("Helleveien", 30, 5045, "Bergen", new MapCoordinate(60.42302228156412, 5.302408706639207))));
        customers.add(new Customer("Lena", "Berg", new Address("Kongleveien", 4, 5105, "Eidsvåg i Åsane", new MapCoordinate(60.437424830973434, 5.307710778593399))));
        
        return customers;
    }

    private static DeliveryOverview createDeliveries(ArrayList<Customer> customers) {
        DeliveryOverview deliveryOverview = new DeliveryOverview();
        LocalDate today = LocalDate.now();
        
        // 2. Combine today's date with the parsed time string for each delivery
        deliveryOverview.addDelivery(new Delivery(LocalDateTime.of(today, LocalTime.parse("12:30:00")), customers.get(0)));
        deliveryOverview.addDelivery(new Delivery(LocalDateTime.of(today, LocalTime.parse("14:00:00")), customers.get(1)));
        deliveryOverview.addDelivery(new Delivery(LocalDateTime.of(today, LocalTime.parse("15:00:00")), customers.get(2)));
        deliveryOverview.addDelivery(new Delivery(LocalDateTime.of(today, LocalTime.parse("16:30:00")), customers.get(3)));
        deliveryOverview.addDelivery(new Delivery(LocalDateTime.of(today, LocalTime.parse("17:45:00")), customers.get(4)));
        deliveryOverview.addDelivery(new Delivery(LocalDateTime.of(today, LocalTime.parse("18:15:00")), customers.get(5)));

        return deliveryOverview;
    }

    private static DriverOverview createDrivers(DeliveryOverview deliveries) {
        DriverOverview driverOverview = new DriverOverview();
        //Add drivers
        Driver driver1 = new Driver("Stian", new ImageIcon("assets/stian.jpg").getImage());
        Driver driver2 = new Driver("Nocco", new ImageIcon("assets/nicho.jpg").getImage());
        Driver driver3 = new Driver("Regine", new ImageIcon("assets/regina.jpg").getImage());

        driverOverview.addDriver(driver1);
        driverOverview.addDriver(driver2);
        driverOverview.addDriver(driver3);

        //TODO 1: Assigning drivers should either be done automatically by the system or there should be an interactive component that allows manual selection
        //TODO 2: When registering a delivery to the driver, it should automatically update the driver and the deliveries class. Observer?
        //Assign drivers to deliveries
        deliveries.getAllDeliveries().get(0).assignTripToDriver(driver1);
        deliveries.getAllDeliveries().get(1).assignTripToDriver(driver2);
        deliveries.getAllDeliveries().get(2).assignTripToDriver(driver2);
        deliveries.getAllDeliveries().get(3).assignTripToDriver(driver3);

        //Add delivery to drivers list of deliveries
        driver1.addDelivery(deliveries.getAllDeliveries().get(0));
        driver2.addDelivery(deliveries.getAllDeliveries().get(1));
        driver2.addDelivery(deliveries.getAllDeliveries().get(2));
        driver3.addDelivery(deliveries.getAllDeliveries().get(3));

        return driverOverview;
    }

    private static void startGUI(DeliveryOverview deliveries, DriverOverview drivers) {
        // Get screen size to determine the appropriate size for the image
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        Rectangle screenBounds = gd.getDefaultConfiguration().getBounds();

        // Get the screen width and height
        int screenWidth = screenBounds.width;
        int screenHeight = screenBounds.height;

        // Load the original map image
        Image mapImage = new ImageIcon("assets/better_map.jpg").getImage();

        // Resize the map image to fit the panel height, while maintaining the aspect ratio
        double scalingFactor = (double) screenHeight / mapImage.getHeight(null);
        int scaledWidth = (int) (mapImage.getWidth(null) * scalingFactor);

        mapImage = mapImage.getScaledInstance(scaledWidth, screenHeight, Image.SCALE_SMOOTH);

        // Set the map bounds
        MapBounds mapBounds = new MapBounds(60.531074598249866, 60.39172710670641,5.4011371170849545, 5.251750010413164);
        PixelGrid grid = new PixelGrid(0, 0, mapImage.getWidth(null), mapImage.getHeight(null));
        // Create the MapManager with map bounds and image path
        MapManager mapManager = new MapManager(mapBounds, mapImage, grid);
        
        // Create the DashboardGUI with the mapManager
        dashboardGUI dashboardGUI = new dashboardGUI(mapManager, deliveries, drivers, screenBounds);

        // Create a JFrame to display the GUI
        JFrame frame = new JFrame("Map and Grid Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gd.setFullScreenWindow(frame);
        frame.add(dashboardGUI);
        frame.setVisible(true);
    }

    private static WeightedGraph<V, Integer> createGraph(Address peppesAddress, ArrayList<Customer> customers) {
        WeightedGraph<V, Integer> graph = new WeightedGraph<>();
        List<V> vertices = new ArrayList<V>(); //Does not include the source node

        V source = new V(peppesAddress);
        graph.addVertex(source);

        //Add vertexes
        for (Customer customer : customers) {
           V vertex = new V(customer.getAddress());
           graph.addVertex(vertex);
           vertices.add(vertex);
        }

        addEdges(graph, source, vertices);

        return graph;
    }

    private static void addEdges(WeightedGraph<V, Integer> graph, V source, List<V> v) {
        // ----- Distances from source ----- \\
        graph.addEdge(source, v.get(0), 13);
        graph.addEdge(source, v.get(1), 10);
        graph.addEdge(source, v.get(2), 4);
        graph.addEdge(source, v.get(3), 8);
        graph.addEdge(source, v.get(4), 6);
        graph.addEdge(source, v.get(5), 7);
        
        // ----- Distances from v1 ----- \\
         graph.addEdge(v.get(0), v.get(1), 17);
        graph.addEdge(v.get(0), v.get(2), 12);
        graph.addEdge(v.get(0), v.get(3), 17);
        graph.addEdge(v.get(0), v.get(4), 7);
        graph.addEdge(v.get(0), v.get(5), 11);

        // ----- Distances from v2 ----- \\
        graph.addEdge(v.get(1), v.get(2), 10);
        graph.addEdge(v.get(1), v.get(3), 12);
        graph.addEdge(v.get(1), v.get(4), 13);
        graph.addEdge(v.get(1), v.get(5), 14);
    
        // ----- Distances from v3 ----- \\
        graph.addEdge(v.get(2), v.get(3), 11);
        graph.addEdge(v.get(2), v.get(4), 8);
        graph.addEdge(v.get(2), v.get(5), 7);

        // ----- Distances from v4 ----- \\
        graph.addEdge(v.get(3), v.get(4), 12);
        graph.addEdge(v.get(3), v.get(5), 14);;

        // ----- Distances from v5 ----- \\
        graph.addEdge(v.get(4), v.get(5), 7);

    }
}
