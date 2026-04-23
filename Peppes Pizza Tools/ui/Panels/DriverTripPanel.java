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
import utils.LanguageManager;

public class DriverTripPanel extends JPanel implements ThemedComponent {
    private DriverOverview drivers;
    private ArrayList<Driver> driverList;
    private Rectangle screenBounds;

    public DriverTripPanel(DeliveryManager deliveryManager, Rectangle screenBounds) {
        this.drivers = deliveryManager.getDriverOverview();
        this.driverList = drivers.getDrivers();
        this.screenBounds = screenBounds;
        
        // Register for theme updates
        dashboardGUI.addThemeListener(evt -> applyTheme());
        
        applyTheme();
    }

    @Override
    public void applyTheme() {
        setBackground(dashboardGUI.theme.background());
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        drawDriverAndDeliveryOverview(g2d);
    }

    private void drawDriverAndDeliveryOverview(Graphics2D g2d) {
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

            // ---- Draw Driver Image ---- \\
            int imageBottomY = drawDriverImage(g2d, driver, rowX, rowY, driverBoxWidth, rowHeight);

            // ---- Draw Name Badge ---- \\
            drawNameBadge(g2d, driver, rowX, imageBottomY, driverBoxWidth);

            // ---- Draw Trip Section ---- \\
            drawTripSection(g2d, driver, rowY, driverBoxWidth, tripBoxWidth, rowHeight);
        }
    }

    // ==========================================================
    // EXTRACTED HELPER METHODS
    // ==========================================================

    /**
     * Draws the driver's profile picture and returns the bottom Y coordinate.
     */
    private int drawDriverImage(Graphics2D g2d, Driver driver, int rowX, int rowY, int driverBoxWidth, int rowHeight) {
        Image driverImage = driver.getDriverPicture();
        if (driverImage == null) {
            return rowY + (rowHeight / 2) - 15; // Fallback if no image exists
        }

        int imgWidth = driverImage.getWidth(null);
        int imgHeight = driverImage.getHeight(null);
        int imgX = rowX + ((driverBoxWidth - imgWidth) / 2);
        int imgY = rowY + ((rowHeight - imgHeight) / 2) - 15;

        drawRoundedImage(g2d, driverImage, imgX, imgY, imgWidth, imgHeight, 30);
        return imgY + imgHeight;
    }

    /**
     * Draws the dynamic name badge box with the driver's name, divider, and status dot.
     */
    private void drawNameBadge(Graphics2D g2d, Driver driver, int rowX, int baseY, int driverBoxWidth) {
        Font nameFont = new Font(dashboardGUI.fontName, Font.BOLD, 18);
        Font statusFont = new Font(dashboardGUI.fontName, Font.PLAIN, 12);

        g2d.setFont(nameFont);
        FontMetrics nameMetrics = g2d.getFontMetrics();
        String driverName = driver.getName();
        int nameWidth = nameMetrics.stringWidth(driverName);

        g2d.setFont(statusFont);
        FontMetrics statusMetrics = g2d.getFontMetrics();
        DriverStatus status = driver.getDriverStatus();
        String statusString = LanguageManager.get(status.name());
        int statusWidth = statusMetrics.stringWidth(statusString);

        // Elements sizing & padding
        int padX = 16;       // Left and right padding of the badge
        int padY = 8;        // Top and bottom padding
        int divPadX = 10;    // Space around the divider
        int dotSize = 10;    // Diameter of the status dot
        int dotPadX = 6;     // Space between dot and status text

        // Calculate total content width and box dimensions
        int contentWidth = nameWidth + (divPadX * 2) + 1 + dotSize + dotPadX + statusWidth;
        int maxContentHeight = Math.max(nameMetrics.getHeight(), statusMetrics.getHeight());
        
        int boxWidth = contentWidth + (padX * 2);
        int boxHeight = maxContentHeight + (padY * 2);

        // Position the box perfectly centered below the image
        int boxX = rowX + ((driverBoxWidth - boxWidth) / 2);
        int boxY = baseY + 12;

        // 1. Draw the Box Background
        drawRoundedBox(g2d, boxX, boxY, boxWidth, boxHeight, dashboardGUI.theme.cardColor());

        // Setup coordinates for drawing elements sequentially (Left to Right)
        int currentX = boxX + padX;
        int centerY = boxY + (boxHeight / 2);

        // 2. Draw Name
        g2d.setFont(nameFont);
        g2d.setColor(dashboardGUI.theme.primaryTextColor());
        int nameY = boxY + ((boxHeight - nameMetrics.getHeight()) / 2) + nameMetrics.getAscent();
        g2d.drawString(driverName, currentX, nameY);
        currentX += nameWidth + divPadX;

        // 3. Draw Divider
        g2d.setColor(dashboardGUI.theme.subtleBorderColor());
        int divHeight = maxContentHeight - 2;
        g2d.drawLine(currentX, centerY - (divHeight / 2), currentX, centerY + (divHeight / 2));
        currentX += 1 + divPadX;

        // 4. Draw Status Dot
        switch (status) {
            case AVAILABLE -> g2d.setColor(dashboardGUI.theme.onTimeTextColor());
            case ON_TRIP -> g2d.setColor(dashboardGUI.theme.lateTextColor());
            case COMING_BACK, ON_BREAK -> g2d.setColor(dashboardGUI.theme.warningTextColor());
            default -> g2d.setColor(dashboardGUI.theme.primaryTextColor());
        }
        g2d.fillOval(currentX, centerY - (dotSize / 2), dotSize, dotSize);
        currentX += dotSize + dotPadX;

        // 5. Draw Status Text
        g2d.setFont(statusFont);
        g2d.setColor(dashboardGUI.theme.primaryTextColor());
        int statusY = boxY + ((boxHeight - statusMetrics.getHeight()) / 2) + statusMetrics.getAscent();
        g2d.drawString(statusString, currentX, statusY);
    }

    /**
     * Draws the trip cards, addresses, and triggers the time badge drawing.
     */
    private void drawTripSection(Graphics2D g2d, Driver driver, int rowY, int driverBoxWidth, int tripBoxWidth, int rowHeight) {
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
                
                // Draw the trip card box
                drawRoundedBox(g2d,
                    tripSectionX + gapX,
                    currentTripY + gapY,
                    tripBoxWidth - (gapX * 2),
                    singleTripHeight - (gapY * 2),
                    dashboardGUI.theme.cardColor()
                );

                // Address text
                g2d.setFont(new Font(dashboardGUI.fontName, Font.BOLD, 25));
                FontMetrics metrics = g2d.getFontMetrics();
                String tripName = trip.getAddress().toString();
                int tripTextX = tripSectionX + ((tripBoxWidth - metrics.stringWidth(tripName)) / 2);
                int tripTextY = currentTripY + (singleTripHeight / 2) - 8;
                g2d.setColor(dashboardGUI.theme.primaryTextColor());
                g2d.drawString(tripName, tripTextX, tripTextY);

                // Draw Time Badge inside the trip card
                drawTimeBadge(g2d, trip, tripSectionX, currentTripY, tripBoxWidth, singleTripHeight);
            }
        } else {
            // Draw fallback text when driver has no trips
            g2d.setFont(new Font(dashboardGUI.fontName, Font.ITALIC, 16));
            g2d.setColor(dashboardGUI.theme.mutedTextColor());
            g2d.drawString(LanguageManager.get("no_active_deliveries"), tripSectionX + 20, rowY + 40);
        }
    }

    /**
     * Draws the dynamic time badge (e.g. "15 min left") on the right side of the trip card.
     */
    private void drawTimeBadge(Graphics2D g2d, Delivery trip, int tripSectionX, int currentTripY, int tripBoxWidth, int singleTripHeight) {
        int minsLeft = trip.minutesLeft();
        String timeText = minsLeft > 0
            ? minsLeft + " " + LanguageManager.get("min_left")
            : Math.abs(minsLeft) + " " + LanguageManager.get("min_late");

        g2d.setFont(new Font(dashboardGUI.fontName, Font.BOLD, 25));
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

    // ==========================================================
    // UTILITY DRAWING METHODS
    // ==========================================================

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