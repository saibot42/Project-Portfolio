package ui;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import Structures.Delivery;
import Structures.Delivery.DeliveryStatus;
import Structures.Driver;
import planners.DeliveryManager;

public class DeliveryListPanel extends JPanel {
    private DeliveryManager deliveryManager;
    private JList<Delivery> inTransitList;
    private JList<Delivery> pendingList;

    public DeliveryListPanel(DeliveryManager deliveryManager) {
        this.deliveryManager = deliveryManager;
        setLayout(new BorderLayout());
        setBackground(dashboardGUI.theme.background());

        inTransitList = new JList<>();
        inTransitList.setCellRenderer(new DeliveryCardRenderer());
        inTransitList.setFixedCellHeight(80);
        inTransitList.setBackground(dashboardGUI.theme.background());

        pendingList = new JList<>();
        pendingList.setCellRenderer(new DeliveryCardRenderer());
        pendingList.setFixedCellHeight(80);
        pendingList.setBackground(dashboardGUI.theme.background());

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(dashboardGUI.theme.background());
        container.add(createSectionHeader("In Transit"));
        container.add(createScrollPane(inTransitList));
        container.add(Box.createVerticalStrut(8));
        container.add(createSectionHeader("Waiting"));
        container.add(createScrollPane(pendingList));

        add(container, BorderLayout.CENTER);
        refresh();
    }

    private JScrollPane createScrollPane(JList<Delivery> list) {
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBackground(dashboardGUI.theme.background());
        scrollPane.getViewport().setBackground(dashboardGUI.theme.background());
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setBackground(dashboardGUI.theme.background());
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor = dashboardGUI.theme.cardColor();
                trackColor = dashboardGUI.theme.background();
            }
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }
            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }
            private JButton createZeroButton() {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(0, 0));
                return btn;
            }
        });
        return scrollPane;
    }

    private JLabel createSectionHeader(String text) {
        JLabel label = new JLabel(text.toUpperCase());
        label.setFont(new Font(dashboardGUI.fontName, Font.BOLD, 25));
        label.setForeground(dashboardGUI.theme.primaryTextColor());
        label.setBorder(BorderFactory.createEmptyBorder(16, 0, 8, 0));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    public void refresh() {
        ArrayList<ArrayList<Delivery>> inTransit = deliveryManager.getDeliveriesInTransit();
        ArrayList<Delivery> flatInTransit = new ArrayList<>();
        for (ArrayList<Delivery> group : inTransit) {
            flatInTransit.addAll(group);
        }
        inTransitList.setListData(flatInTransit.toArray(new Delivery[0]));

        ArrayList<Delivery> pending = deliveryManager.getPendingDeliveries();
        pendingList.setListData(pending.toArray(new Delivery[0]));
    }

    private Color statusBackground(DeliveryStatus status) {
        switch (status) {
            case ON_TIME: return dashboardGUI.theme.onTimeColor();
            case WARNING: return dashboardGUI.theme.warningColor();
            case LATE:    return dashboardGUI.theme.lateColor();
            default:      return dashboardGUI.theme.cardColor();
        }
    }

    private Color statusForeground(DeliveryStatus status) {
        switch (status) {
            case ON_TIME: return dashboardGUI.theme.onTimeTextColor();
            case WARNING: return dashboardGUI.theme.warningTextColor();
            case LATE:    return dashboardGUI.theme.lateTextColor();           
            default:      return dashboardGUI.theme.primaryTextColor();
        }
    }

    private class DeliveryCardRenderer implements ListCellRenderer<Delivery> {
        @Override
        public Component getListCellRendererComponent(JList<? extends Delivery> list,
                Delivery delivery, int index, boolean isSelected, boolean cellHasFocus) {

            DeliveryStatus status = delivery.getStatus();
            int mins = delivery.minutesLeft();

            // Outer wrapper — controls spacing between cards and left/right margin
            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setOpaque(false);
            wrapper.setBorder(BorderFactory.createEmptyBorder(4, 20, 4, 20));

            // Inner card — custom painted with rounded corners
            JPanel card = new JPanel(new BorderLayout(12, 0)) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(dashboardGUI.theme.cardColor());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                    g2.setColor(dashboardGUI.theme.cardColor());
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                    g2.dispose();
                }
            };
            card.setOpaque(false);
            card.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));

            // Left: status dot
            JPanel dot = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillOval(0, 0, getWidth(), getHeight());
                }
            };
            dot.setPreferredSize(new Dimension(10, 10));
            dot.setBackground(statusForeground(status));
            dot.setOpaque(false);

            JPanel dotWrapper = new JPanel(new GridBagLayout());
            dotWrapper.setOpaque(false);
            dotWrapper.add(dot);
            card.add(dotWrapper, BorderLayout.WEST);

            // Center: address + driver
            JPanel center = new JPanel();
            center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
            center.setOpaque(false);

            JLabel address = new JLabel(delivery.getAddress().toString());
            address.setFont(new Font(dashboardGUI.fontName, Font.BOLD, 20));
            address.setForeground(dashboardGUI.theme.primaryTextColor());

            Driver driver = delivery.getDriver();
            JLabel driverLabel = new JLabel(driver != null ? driver.getName() : "Unassigned");
            driverLabel.setFont(new Font(dashboardGUI.fontName, Font.PLAIN, 15));
            driverLabel.setForeground(dashboardGUI.theme.mutedTextColor());

            center.add(address);
            center.add(Box.createVerticalStrut(3));
            center.add(driverLabel);
            card.add(center, BorderLayout.CENTER);

            // Right: rounded time badge
            String badgeText = mins > 0 ? mins + " min left" : Math.abs(mins) + " min late";
            JLabel badge = new JLabel(badgeText, SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            badge.setFont(new Font(dashboardGUI.fontName, Font.BOLD, 12));
            badge.setOpaque(false);
            badge.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
            badge.setBackground(statusBackground(status));
            badge.setForeground(statusForeground(status));
            card.add(badge, BorderLayout.EAST);

            wrapper.add(card, BorderLayout.CENTER);
            return wrapper;
        }
    }
}