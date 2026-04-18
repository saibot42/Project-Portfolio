package ui;

import javax.swing.*;
import java.awt.*;
import Structures.*;
import planners.DeliveryManager;
import utils.*;

public class dashboardGUI extends JPanel {
    public static Theme theme = new DarkMode(); // Default to dark
    private MapPanel mapPanel;
    private DriverTripPanel driverTripPanel;
    private DeliveryListPanel deliveryListPanel;
    private JPanel contentArea; // Store as field
    public static final String fontName = "Roboto";

    public dashboardGUI(MapManager mapManager, DeliveryManager deliveryManager, Rectangle screenBounds, Address pAddress) {
        setLayout(new BorderLayout());

        mapPanel = new MapPanel(mapManager, deliveryManager, pAddress);
        driverTripPanel = new DriverTripPanel(deliveryManager, screenBounds);
        deliveryListPanel = new DeliveryListPanel(deliveryManager);
        
        // Create once, reuse for both sidebar and layout
        contentArea = new JPanel(new CardLayout());
        contentArea.add(driverTripPanel, "DRIVERS");
        contentArea.add(deliveryListPanel, "DELIVERIES");

        SidebarPanel sidebar = new SidebarPanel(contentArea);

        add(mapPanel, BorderLayout.WEST);
        add(contentArea, BorderLayout.CENTER);
        add(sidebar, BorderLayout.EAST);

        startDynamicUpdates();
    }

    public static void setTheme(Theme newTheme) {
        theme = newTheme;
    }

    private void startDynamicUpdates() {
        new Timer(1000, e -> {
            driverTripPanel.repaint();
            deliveryListPanel.refresh();
            mapPanel.repaint();
        }).start();
    }
}