package ui.Panels;

import java.awt.*;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import Structures.Address;
import Structures.Delivery;
import Structures.Pixel;
import planners.DeliveryManager;
import ui.dashboardGUI;
import ui.Themes.ThemedComponent;
import ui.Themes.LightMode;
import utils.MapManager;

public class MapPanel extends JPanel implements ThemedComponent {
    private MapManager mapManager;
    private DeliveryManager deliveryManager;
    private Address pAddress;
    private Image peppesIcon;
    
    private Image darkMap;   // Original dark asset provided by MapManager
    private Image lightMap;  // Generated once and cached to prevent lag
    private Image currentMap;

    public MapPanel(MapManager mapManager, DeliveryManager deliveryManager, Address pAddress) {
        this.mapManager = mapManager;
        this.deliveryManager = deliveryManager;
        this.pAddress = pAddress;
        
        // Load assets
        this.darkMap = mapManager.getMapImage();
         // Load the original map image
        java.net.URL mapUrl = MapPanel.class.getResource("/assets/peppesIcon.png");
        if (mapUrl == null) {
            throw new RuntimeException("Could not find  image! Check folder capitalization.");
        }
        this.peppesIcon = new ImageIcon(mapUrl).getImage();

        // Register for real-time theme updates
        dashboardGUI.addThemeListener(evt -> applyTheme());

        // Lock panel size to the original map dimensions
        int w = darkMap.getWidth(null);
        int h = darkMap.getHeight(null);
        setPreferredSize(new Dimension(w, h));
        // Add this in MapPanel constructor after setPreferredSize:
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                repaint();
            }
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                repaint();
            }
        });


        // Initial setup
        applyTheme();
    }

    /**
     * Updates the map reference. 
     * If LightMode is selected for the first time, it generates the light map.
     */
    @Override
    public void applyTheme() {
        setBackground(dashboardGUI.theme.background());

        if (dashboardGUI.theme instanceof LightMode) {
            // "Lazy Load": Generate the light version only once per session
            if (lightMap == null) {
                lightMap = generateLightMap(darkMap);
            }
            currentMap = lightMap;
        } else {
            currentMap = darkMap;
        }

        repaint();
    }

    /**
     * Programmatic Light-Mode Generator.
     * Inverts land colors but uses a blue-threshold check to protect water.
     */
    private Image generateLightMap(Image source) {
    RGBImageFilter filter = new RGBImageFilter() {
        @Override
        public int filterRGB(int x, int y, int rgb) {
            int a = (rgb >> 24) & 0xFF;
            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = rgb & 0xFF;

            // 1. DETECT ROADS (The nearly-black pixels)
            // If all channels are very low, it's a road/path.
            boolean isRoad = (r < 25 && g < 25 && b < 25);

            // 2. DETECT WATER (Blue dominance)
            // Your map's water is roughly (30, 60, 100). Blue is much higher than Red.
            boolean isWater = (b > r + 30);

            if (isWater) {
                // Make water paler: Mix current blue with white
                r = Math.min(255, r + 100);
                g = Math.min(255, g + 130);
                b = Math.min(255, b + 150);
                return (a << 24) | (r << 16) | (g << 8) | b;
            } 
            
            if (isRoad) {
                // Make roads significantly darker/sharper against the light land
                // We'll set them to a dark slate gray
                return (a << 24) | (60 << 16) | (60 << 8) | 60;
            }

            // 3. LAND TRANSFORMATION
            // Invert the dark land but keep it soft (not pure white)
            r = Math.min(255, (255 - r));
            g = Math.min(255, (255 - g));
            b = Math.min(255, (255 - b));
            
            // Add a slight "warmth" or "paper" tint to the land so it's not blinding
            r = Math.max(0, r - 10);
            g = Math.max(0, g - 10);
            
            return (a << 24) | (r << 16) | (g << 8) | b;
        }
    };

    return createImage(new FilteredImageSource(source.getSource(), filter));
}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (currentMap != null) {
            g2d.drawImage(currentMap, 0, 0, getWidth(), getHeight(), null);
        }

        drawGrid(g2d);
        highlightDeliveryLocations(g2d);
    }

    private void drawGrid(Graphics2D g2d) {
        g2d.setColor(dashboardGUI.theme.transparentColor());
        int cellW = mapManager.getPixelGrid().getCellWidth();
        int cellH = mapManager.getPixelGrid().getCellHeight();
        
        for (int x = 0; x < mapManager.getPixelGrid().getGridWidth(); x++) {
            for (int y = 0; y < mapManager.getPixelGrid().getGridHeight(); y++) {
                g2d.drawRect(x * cellW, y * cellH, cellW, cellH);
            }
        }
    }

    private void highlightDeliveryLocations(Graphics2D g2d) {
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Fall back to map image size if panel hasn't been laid out yet
        if (panelWidth <= 0) panelWidth = darkMap.getWidth(null);
        if (panelHeight <= 0) panelHeight = darkMap.getHeight(null);
        if (panelWidth <= 0 || panelHeight <= 0) return;

        int mapImageWidth = darkMap.getWidth(null);
        int mapImageHeight = darkMap.getHeight(null);
        if (mapImageWidth <= 0 || mapImageHeight <= 0) return;

        double scaleX = (double) panelWidth / mapImageWidth;
        double scaleY = (double) panelHeight / mapImageHeight;

        int cellWidth = (int)(mapManager.getPixelGrid().getCellWidth() * scaleX);
        int cellHeight = (int)(mapManager.getPixelGrid().getCellHeight() * scaleY);
        if (cellWidth <= 0 || cellHeight <= 0) return;

        // Restaurant icon
        int iconSize = Math.max(20, cellWidth * 12);
        Pixel peppesPixel = mapManager.ConvertMapToGrid(pAddress.getMapCoordinate());
        if (peppesPixel == null) return;

        int iconX = (peppesPixel.getX() * cellWidth) + (cellWidth / 2) - (iconSize / 2);
        int iconY = (peppesPixel.getY() * cellHeight) + (cellHeight / 2) - (iconSize / 2);
        if (peppesIcon != null) {
            g2d.drawImage(peppesIcon, iconX, iconY, iconSize, iconSize, null);
        }

        // Delivery dots
        int dotSize = Math.max(8, cellWidth * 3);
        for (Delivery delivery : deliveryManager.getPendingDeliveries()) {
            Pixel pixel = mapManager.ConvertMapToGrid(delivery.getAddress().getMapCoordinate());
            if (pixel == null) continue;

            int cx = (pixel.getX() * cellWidth) + (cellWidth / 2);
            int cy = (pixel.getY() * cellHeight) + (cellHeight / 2);

            switch (delivery.getStatus()) {
                case ON_TIME -> g2d.setColor(dashboardGUI.theme.onTimeTextColor());
                case WARNING -> g2d.setColor(dashboardGUI.theme.warningTextColor());
                case LATE    -> g2d.setColor(dashboardGUI.theme.lateTextColor());
                default      -> g2d.setColor(dashboardGUI.theme.primaryTextColor());
            }
            g2d.fillOval(cx - (dotSize / 2), cy - (dotSize / 2), dotSize, dotSize);
            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawOval(cx - (dotSize / 2), cy - (dotSize / 2), dotSize, dotSize);
        }
    }
}