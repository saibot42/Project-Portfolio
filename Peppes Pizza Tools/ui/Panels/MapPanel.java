package ui.Panels;

import java.awt.*;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

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
        this.peppesIcon = new ImageIcon("assets/peppesIcon.png").getImage();

        // Register for real-time theme updates
        dashboardGUI.addThemeListener(evt -> applyTheme());

        // Lock panel size to the original map dimensions
        int w = darkMap.getWidth(null);
        int h = darkMap.getHeight(null);
        setPreferredSize(new Dimension(w, h));

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

        // 1. Draw the active map
        if (currentMap != null) {
            g2d.drawImage(currentMap, 0, 0, getWidth(), getHeight(), null);
        }

        // 2. Draw invisible grid (kept for coordinate logic)
        drawGrid(g2d);

        // 3. Draw Icons and Dots
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
        int cellWidth = mapManager.getPixelGrid().getCellWidth();
        int cellHeight = mapManager.getPixelGrid().getCellHeight();
        
        if (cellWidth <= 0 || cellHeight <= 0) return;

        // --- RESTAURANT ICON ---
        int iconSize = cellWidth * 12;
        Pixel peppesPixel = mapManager.ConvertMapToGrid(pAddress.getMapCoordinate());

        int iconX = (peppesPixel.getX() * cellWidth) + (cellWidth / 2) - (iconSize / 2);
        int iconY = (peppesPixel.getY() * cellHeight) + (cellHeight / 2) - (iconSize / 2);
        g2d.drawImage(peppesIcon, iconX, iconY, iconSize, iconSize, null);

        // --- DELIVERY DOTS ---
        int dotSize = cellWidth * 3;

        for (Delivery delivery : deliveryManager.getPendingDeliveries()) {
            Pixel pixel = mapManager.ConvertMapToGrid(delivery.getAddress().getMapCoordinate());

            int cx = (pixel.getX() * cellWidth) + (cellWidth / 2);
            int cy = (pixel.getY() * cellHeight) + (cellHeight / 2);

            // Select color based on theme semantic colors
            switch (delivery.getStatus()) {
                case ON_TIME -> g2d.setColor(dashboardGUI.theme.onTimeTextColor());
                case WARNING -> g2d.setColor(dashboardGUI.theme.warningTextColor());
                case LATE    -> g2d.setColor(dashboardGUI.theme.lateTextColor());
                default      -> g2d.setColor(dashboardGUI.theme.primaryTextColor());
            }

            // Fill dot
            g2d.fillOval(cx - (dotSize / 2), cy - (dotSize / 2), dotSize, dotSize);

            // Subtle border for high visibility on light or dark maps
            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawOval(cx - (dotSize / 2), cy - (dotSize / 2), dotSize, dotSize);
        }
    }
}