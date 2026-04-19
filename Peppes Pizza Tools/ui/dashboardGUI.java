package ui;

import javax.swing.*;
import java.awt.*;
import Structures.*;
import planners.DeliveryManager;
import ui.Panels.DeliveryListPanel;
import ui.Panels.DriverTripPanel;
import ui.Panels.MapPanel;
import ui.Panels.SidebarPanel;
import ui.Themes.DarkMode;
import ui.Themes.Theme;
import utils.*;

public class dashboardGUI extends JPanel {
    public static Theme theme = new DarkMode(); // Default to dark
    private static final java.beans.PropertyChangeSupport support = new java.beans.PropertyChangeSupport(dashboardGUI.class);
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
        Theme oldTheme = theme;
        theme = newTheme;
        // Notify anyone listening that "theme" has changed
        support.firePropertyChange("theme", oldTheme, newTheme);
    }

    public static void addThemeListener(java.beans.PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public static void removeThemeListener(java.beans.PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    private void startDynamicUpdates() {
        new Timer(1000, e -> {
            driverTripPanel.repaint();
            deliveryListPanel.refresh();
            mapPanel.repaint();
        }).start();
    }
}