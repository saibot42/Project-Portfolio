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
    private static final java.beans.PropertyChangeSupport themeSupport = new java.beans.PropertyChangeSupport(dashboardGUI.class);
    private static final java.beans.PropertyChangeSupport langSupport = new java.beans.PropertyChangeSupport(dashboardGUI.class);

    
    private MapPanel mapPanel;
    private DriverTripPanel driverTripPanel;
    private DeliveryListPanel deliveryListPanel;
    private JPanel contentArea; // Store as field
    public static final String fontName = "Inter";

    public dashboardGUI(MapManager mapManager, DeliveryManager deliveryManager, Rectangle screenBounds, Address pAddress) {
        setLayout(new BorderLayout());

        mapPanel = new MapPanel(mapManager, deliveryManager, pAddress);
        driverTripPanel = new DriverTripPanel(deliveryManager, screenBounds);
        deliveryListPanel = new DeliveryListPanel(deliveryManager);
        
        // Create once, reuse for both sidebar and layout
        contentArea = new JPanel(new CardLayout());
        contentArea.add(driverTripPanel, "DRIVERS");
        contentArea.add(deliveryListPanel, "DELIVERIES");

        SidebarPanel sidebar = new SidebarPanel(contentArea, screenBounds);

        add(mapPanel, BorderLayout.WEST);
        add(contentArea, BorderLayout.CENTER);
        add(sidebar, BorderLayout.EAST);

        startDynamicUpdates();

        // At the end of the constructor, after all panels are added:
        SwingUtilities.invokeLater(() -> {
            SwingUtilities.invokeLater(() -> {
                mapPanel.repaint();
            });
        });
    }

    public static void setLanguage(LanguageManager.Language lang) {
        LanguageManager.Language old = LanguageManager.getLanguage();
        LanguageManager.setLanguage(lang);
        langSupport.firePropertyChange("language", old, lang);
    }

    public static void addLanguageListener(java.beans.PropertyChangeListener listener) {
        langSupport.addPropertyChangeListener(listener);
    }

    public static void removeLanguageListener(java.beans.PropertyChangeListener listener) {
        langSupport.removePropertyChangeListener(listener);
    }

    public static void setTheme(Theme newTheme) {
        Theme oldTheme = theme;
        theme = newTheme;
        // Notify anyone listening that "theme" has changed
        themeSupport.firePropertyChange("theme", oldTheme, newTheme);
    }

    public static void addThemeListener(java.beans.PropertyChangeListener listener) {
        themeSupport.addPropertyChangeListener(listener);
    }

    public static void removeThemeListener(java.beans.PropertyChangeListener listener) {
        themeSupport.removePropertyChangeListener(listener);
    }

    private void startDynamicUpdates() {
        new Timer(1000, e -> {
            driverTripPanel.repaint();
            deliveryListPanel.refresh();
            mapPanel.repaint();
        }).start();
    }
}