package ui.Panels;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import javax.swing.JPanel;

import Structures.Delivery;
import Structures.Driver;
import Structures.Driver.DriverStatus;
import planners.DeliveryManager;
import planners.DriverOverview;
import ui.dashboardGUI;
import ui.Themes.ThemedComponent;

public class DriverTripPanel extends JPanel implements ThemedComponent {
    private DeliveryManager deliveryManager;
    private DriverOverview drivers;
    private ArrayList<Driver> driverList;
    private Rectangle screenBounds;

    public DriverTripPanel(DeliveryManager deliveryManager, Rectangle screenBounds) {
        this.deliveryManager = deliveryManager;
        this.drivers = deliveryManager.getDriverOverview();
        this.driverList = drivers.getDrivers();
        this.screenBounds = screenBounds;
        
        // Register for theme updates
        dashboardGUI.addThemeListener(evt -> applyTheme());
        
        applyTheme();
    }

    @Override
    public void applyTheme() {
        // Update the basic panel properties
        setBackground(dashboardGUI.theme.background());
        // Since this panel is custom-painted, a repaint is all it needs to see the new colors
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        drawDriverandTripOverview(g2d);
    }

    private void drawDriverandTripOverview(Graphics2D g2d) {
        if (driverList == null || driverList.isEmpty()) return;

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int driverBoxWidth = (int)(panelWidth * 0.3);
        int tripBoxWidth = panelWidth - driverBoxWidth;
        int numDrivers = driverList.size();
        int rowHeight = panelHeight / numDrivers;

        for (int i = 0; i < numDrivers; i++) {
            Driver driver = driverList.get(i);
            int rowX = 0;
            int rowY = i * rowHeight;

            // --- DRIVER SECTION ---
            Image driverImage = driver.getDriverPicture();
            if (driverImage != null) {
                int imgWidth = driverImage.getWidth(null);
                int imgHeight = driverImage.getHeight(null);

                int imgX = rowX + ((driverBoxWidth - imgWidth) / 2);
                int imgY = rowY + ((rowHeight - imgHeight) / 2) - 15;

                drawRoundedImage(g2d, driverImage, imgX, imgY, imgWidth, imgHeight, 30);

                // Name box beneath image
                int nameBoxY = imgY + imgHeight + 8;
                int nameBoxWidth = driverBoxWidth / 2;
                int nameBoxHeight = (rowY + rowHeight) - nameBoxY - 12;
                int nameBoxX = imgX + ((imgWidth - nameBoxWidth) / 2);
                drawRoundedBox(g2d, nameBoxX, nameBoxY, nameBoxWidth, nameBoxHeight, dashboardGUI.theme.cardColor());

                // Measure text
                g2d.setFont(new Font("Arial", Font.BOLD, 22));
                FontMetrics nameMetrics = g2d.getFontMetrics();
                String driverName = driver.getName();

                g2d.setFont(new Font("Arial", Font.PLAIN, 13));
                FontMetrics statusMetrics = g2d.getFontMetrics();
                DriverStatus status = driver.getDriverStatus();
                String statusString = status.toString();

                int gap = 6; 
                int totalTextHeight = nameMetrics.getHeight() + gap + statusMetrics.getHeight();
                int blockStartY = nameBoxY + ((nameBoxHeight - totalTextHeight) / 2);

                // Draw name
                int nameX = rowX + ((driverBoxWidth - nameMetrics.stringWidth(driverName)) / 2);
                int nameY = blockStartY + nameMetrics.getAscent();
                g2d.setFont(new Font("Arial", Font.BOLD, 22));
                g2d.setColor(dashboardGUI.theme.primaryTextColor());
                g2d.drawString(driverName, nameX, nameY);

                // Draw status with Dynamic Colors
                int statusX = rowX + ((driverBoxWidth - statusMetrics.stringWidth(statusString)) / 2);
                int statusY = nameY + nameMetrics.getDescent() + gap + statusMetrics.getAscent();
                g2d.setFont(new Font("Arial", Font.PLAIN, 13));
                
                switch (status) {
                    case AVAILABLE -> g2d.setColor(dashboardGUI.theme.onTimeTextColor());
                    case ON_TRIP -> g2d.setColor(dashboardGUI.theme.lateTextColor());
                    case COMING_BACK, ON_BREAK -> g2d.setColor(dashboardGUI.theme.warningTextColor());
                    default -> g2d.setColor(dashboardGUI.theme.primaryTextColor());
                }
                g2d.drawString(statusString, statusX, statusY);
            }

            // --- TRIP(S) SECTION ---
            int tripSectionX = driverBoxWidth;
            ArrayList<Delivery> driverTrips = driver.getDeliveriesForDriver();
            
            if (driverTrips != null && !driverTrips.isEmpty()) {
                int numTrips = driverTrips.size();
                int singleTripHeight = rowHeight / numTrips;

                for (int j = 0; j < numTrips; j++) {
                    Delivery trip = driverTrips.get(j);
                    int currentTripY = rowY + (j * singleTripHeight);

                    int gapX = 16;
                    int gapY = 10;
                    drawRoundedBox(g2d,
                        tripSectionX + gapX,
                        currentTripY + gapY,
                        tripBoxWidth - (gapX * 2),
                        singleTripHeight - (gapY * 2),
                        dashboardGUI.theme.cardColor()
                    );

                    // Address text
                    g2d.setFont(new Font("Arial", Font.BOLD, 24));
                    FontMetrics metrics = g2d.getFontMetrics();
                    String tripName = trip.getAddress().toString();
                    int tripTextX = tripSectionX + ((tripBoxWidth - metrics.stringWidth(tripName)) / 2);
                    int tripTextY = currentTripY + (singleTripHeight / 2) - 8;
                    g2d.setColor(dashboardGUI.theme.primaryTextColor());
                    g2d.drawString(tripName, tripTextX, tripTextY);

                    // Time badge logic
                    int minsLeft = trip.minutesLeft();
                    String timeText = minsLeft > 0 ? minsLeft + " min left" : Math.abs(minsLeft) + " min late";

                    g2d.setFont(new Font("Arial", Font.BOLD, 16));
                    FontMetrics timeMetrics = g2d.getFontMetrics();
                    int timeTextWidth = timeMetrics.stringWidth(timeText);
                    int timeTextX = tripSectionX + ((tripBoxWidth - timeTextWidth) / 2);
                    int timeTextY = currentTripY + (singleTripHeight / 2) + timeMetrics.getHeight();

                    // Dynamic Badge background based on theme
                    g2d.setColor(minsLeft > 15 ? dashboardGUI.theme.onTimeColor()
                               : minsLeft > 0  ? dashboardGUI.theme.warningColor()
                               :                 dashboardGUI.theme.lateColor());
                    
                    int badgePadX = 12;
                    int badgePadY = 4;
                    g2d.fillRoundRect(
                        timeTextX - badgePadX,
                        timeTextY - timeMetrics.getAscent() - badgePadY,
                        timeTextWidth + (badgePadX * 2),
                        timeMetrics.getHeight() + (badgePadY * 2),
                        10, 10
                    );

                    // Dynamic Badge text based on theme
                    g2d.setColor(minsLeft > 15 ? dashboardGUI.theme.onTimeTextColor()
                               : minsLeft > 0  ? dashboardGUI.theme.warningTextColor()
                               :                 dashboardGUI.theme.lateTextColor());
                    g2d.drawString(timeText, timeTextX, timeTextY);
                }
            } else {
                g2d.setFont(new Font("Arial", Font.ITALIC, 16));
                g2d.setColor(dashboardGUI.theme.mutedTextColor());
                g2d.drawString("No active deliveries", tripSectionX + 20, rowY + 40);
            }
        }
    }

    private void drawRoundedBox(Graphics2D g2d, int x, int y, int width, int height, Color color) {
        g2d.setColor(color);
        g2d.fillRoundRect(x, y, width, height, 20, 20);
        g2d.setColor(dashboardGUI.theme.cardBorderColor());
        g2d.setStroke(new BasicStroke(1f));
        g2d.drawRoundRect(x, y, width, height, 20, 20);
    }

    private void drawRoundedImage(Graphics2D g, Image img, int x, int y, int width, int height, int cornerRadius) {
        if (img == null) return;
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        RoundRectangle2D roundedShape = new RoundRectangle2D.Float(x, y, width, height, cornerRadius, cornerRadius);
        g2d.setClip(roundedShape);
        g2d.drawImage(img, x, y, width, height, null);
        g2d.dispose();
    }
}