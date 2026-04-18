package ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class SidebarPanel extends JPanel {
    private JLabel clockLabel;
    private JLabel iconLabel;
    private ImageIcon peppesIcon = new ImageIcon("assets/peppesIcon.png");
    private final int sidebarWidth = 100;

    public SidebarPanel(JPanel contentArea) {
        setPreferredSize(new Dimension(sidebarWidth, 0));
        setBackground(dashboardGUI.theme.sidebarColor());
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // TOP: Logo
        ImageIcon scaledIcon = new ImageIcon(
            peppesIcon.getImage().getScaledInstance(sidebarWidth - 20, sidebarWidth - 20, Image.SCALE_SMOOTH)
        );
        iconLabel = new JLabel(scaledIcon);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(20));
        add(iconLabel);
        add(Box.createVerticalStrut(16));
        add(createDivider());

        // Spring pushes button to middle
        add(Box.createVerticalGlue());

        // MIDDLE: Custom painted toggle button
        JButton toggleBtn = new JButton() {
            private boolean hovered = false;

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

                // Background
                g2.setColor(hovered
                    ? dashboardGUI.theme.cardBorderColor()
                    : dashboardGUI.theme.cardColor());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);

                // Border
                g2.setColor(dashboardGUI.theme.cardBorderColor());
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);

                // Switch icon
                int cx = getWidth() / 2;
                int cy = getHeight() / 2;
                g2.setColor(dashboardGUI.theme.primaryTextColor());
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                // Top line with right arrow
                g2.drawLine(cx - 12, cy - 6, cx + 12, cy - 6);
                g2.drawLine(cx + 8,  cy - 10, cx + 12, cy - 6);
                g2.drawLine(cx + 8,  cy - 2,  cx + 12, cy - 6);

                // Bottom line with left arrow
                g2.drawLine(cx - 12, cy + 6, cx + 12, cy + 6);
                g2.drawLine(cx - 8,  cy + 2,  cx - 12, cy + 6);
                g2.drawLine(cx - 8,  cy + 10, cx - 12, cy + 6);

                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                // Suppressed — we paint our own
            }
        };

        toggleBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        toggleBtn.setMaximumSize(new Dimension(sidebarWidth - 16, 56));
        toggleBtn.setPreferredSize(new Dimension(sidebarWidth - 16, 56));
        toggleBtn.setContentAreaFilled(false);
        toggleBtn.setFocusPainted(false);
        toggleBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggleBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout) contentArea.getLayout();
            cl.next(contentArea);
        });
        add(toggleBtn);

        // Spring pushes clock to bottom
        add(Box.createVerticalGlue());

        // Divider above clock
        add(createDivider());
        add(Box.createVerticalStrut(16));

        // BOTTOM: Clock
        clockLabel = new JLabel("00:00");
        clockLabel.setForeground(dashboardGUI.theme.primaryTextColor());
        clockLabel.setFont(new Font("Arial", Font.BOLD, 26));
        clockLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(clockLabel);

        // Date
        JLabel dateLabel = new JLabel(
            java.time.LocalDate.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("dd MMM"))
        );
        dateLabel.setForeground(dashboardGUI.theme.mutedTextColor());
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(4));
        add(dateLabel);
        add(Box.createVerticalStrut(20));

        // Timer
        new Timer(1000, e -> {
            clockLabel.setText(
                java.time.LocalTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))
            );
        }).start();
    }

    private Component createDivider() {
        JPanel divider = new JPanel();
        divider.setMaximumSize(new Dimension(sidebarWidth - 16, 1));
        divider.setPreferredSize(new Dimension(sidebarWidth - 16, 1));
        divider.setBackground(dashboardGUI.theme.subtleBorderColor());
        divider.setAlignmentX(Component.CENTER_ALIGNMENT);
        return divider;
    }
}