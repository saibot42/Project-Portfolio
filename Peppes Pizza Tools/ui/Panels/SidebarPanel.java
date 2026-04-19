package ui.Panels;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import ui.dashboardGUI;
import ui.Themes.ThemedComponent;

public class SidebarPanel extends JPanel implements ThemedComponent {
    private JLabel clockLabel;
    private JLabel dateLabel;
    private JLabel iconLabel;
    private ImageIcon peppesIcon = new ImageIcon("assets/peppesIcon.png");
    private final int sidebarWidth = 100;

    public SidebarPanel(JPanel contentArea) {
        setPreferredSize(new Dimension(sidebarWidth, 0));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Register for real-time theme updates
        dashboardGUI.addThemeListener(evt -> applyTheme());

        drawLogo();
        add(Box.createVerticalStrut(16));
        add(createDivider());

        add(Box.createVerticalGlue());
        addToggleButton(contentArea);

        add(Box.createVerticalGlue());
        addSettingsButton();

        add(createDivider());
        add(Box.createVerticalStrut(16));
        addClock();

        // Initial style application
        applyTheme();
    }

    /**
     * Updates colors and repaints when the theme changes
     */
    @Override
    public void applyTheme() {
        setBackground(dashboardGUI.theme.sidebarColor());
        
        if (clockLabel != null) {
            clockLabel.setForeground(dashboardGUI.theme.primaryTextColor());
        }
        if (dateLabel != null) {
            dateLabel.setForeground(dashboardGUI.theme.mutedTextColor());
        }
        
        // Repaint triggers the custom paintComponent methods in the icons and dividers
        repaint();
    }

    // Shared helper — paints the rounded card background and border for any button
    private void paintButtonBackground(Graphics2D g2, int width, int height, boolean hovered) {
        g2.setColor(hovered ? dashboardGUI.theme.cardBorderColor() : dashboardGUI.theme.cardColor());
        g2.fillRoundRect(0, 0, width, height, 14, 14);
        g2.setColor(dashboardGUI.theme.cardBorderColor());
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(0, 0, width - 1, height - 1, 14, 14);
    }

    private void applyButtonStyle(JButton btn) {
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(sidebarWidth - 16, 56));
        btn.setPreferredSize(new Dimension(sidebarWidth - 16, 56));
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private Component createDivider() {
        JPanel divider = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                // Dynamic painting fetches current theme color on every refresh
                g.setColor(dashboardGUI.theme.subtleBorderColor());
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        divider.setMaximumSize(new Dimension(sidebarWidth - 16, 1));
        divider.setPreferredSize(new Dimension(sidebarWidth - 16, 1));
        divider.setAlignmentX(Component.CENTER_ALIGNMENT);
        return divider;
    }

    private void drawLogo() {
        ImageIcon scaledIcon = new ImageIcon(
            peppesIcon.getImage().getScaledInstance(sidebarWidth - 20, sidebarWidth - 20, Image.SCALE_SMOOTH)
        );
        iconLabel = new JLabel(scaledIcon);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(20));
        add(iconLabel);
    }

    private void addToggleButton(JPanel contentArea) {
        JButton btn = new JButton() {
            boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                    @Override public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Optional: Uncomment to use background cards on buttons
                // paintButtonBackground(g2, getWidth(), getHeight(), hovered);

                int cx = getWidth() / 2;
                int cy = getHeight() / 2;
                g2.setColor(dashboardGUI.theme.primaryTextColor());
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                // Swap/Arrows Icon
                g2.drawLine(cx - 12, cy - 6,  cx + 12, cy - 6);
                g2.drawLine(cx + 8,  cy - 10, cx + 12, cy - 6);
                g2.drawLine(cx + 8,  cy - 2,  cx + 12, cy - 6);
                g2.drawLine(cx - 12, cy + 6,  cx + 12, cy + 6);
                g2.drawLine(cx - 8,  cy + 2,  cx - 12, cy + 6);
                g2.drawLine(cx - 8,  cy + 10, cx - 12, cy + 6);

                g2.dispose();
            }

            @Override protected void paintBorder(Graphics g) {}
        };

        applyButtonStyle(btn);
        btn.addActionListener(e -> {
            CardLayout cl = (CardLayout) contentArea.getLayout();
            cl.next(contentArea);
        });
        add(btn);
    }

    private void addSettingsButton() {
        JButton btn = new JButton() {
            boolean hovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                    @Override public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int cx = getWidth() / 2;
                int cy = getHeight() / 2;
                int outerR = 10;
                int innerR = 6;
                
                g2.setColor(dashboardGUI.theme.primaryTextColor());
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                
                // Gear/Settings Icon
                g2.drawOval(cx - innerR, cy - innerR, innerR * 2, innerR * 2);
                for (int i = 0; i < 8; i++) {
                    double angle = Math.toRadians(i * 45.0);
                    int x1 = (int)(cx + innerR * Math.cos(angle));
                    int y1 = (int)(cy + innerR * Math.sin(angle));
                    int x2 = (int)(cx + outerR * Math.cos(angle));
                    int y2 = (int)(cy + outerR * Math.sin(angle));
                    g2.drawLine(x1, y1, x2, y2);
                }

                g2.dispose();
            }

            @Override protected void paintBorder(Graphics g) {}
        };

        applyButtonStyle(btn);
        btn.addActionListener(e ->
            SettingsMenu.open(SwingUtilities.getWindowAncestor(this))
        );
        add(Box.createVerticalStrut(8));
        add(btn);
        add(Box.createVerticalStrut(8));
    }

    private void addClock() {
        clockLabel = new JLabel("00:00");
        clockLabel.setFont(new Font("Arial", Font.BOLD, 26));
        clockLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(clockLabel);

        dateLabel = new JLabel(
            java.time.LocalDate.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("dd MMM"))
        );
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        add(Box.createVerticalStrut(4));
        add(dateLabel);
        add(Box.createVerticalStrut(20));

        // Regular clock update
        new Timer(1000, e -> {
            clockLabel.setText(
                java.time.LocalTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))
            );
        }).start();
    }
}