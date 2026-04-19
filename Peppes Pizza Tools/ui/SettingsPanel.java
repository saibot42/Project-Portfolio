package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SettingsPanel extends JDialog {

    public SettingsPanel(Window owner) {
        super(owner, ModalityType.APPLICATION_MODAL);
        setUndecorated(true);
        setSize(500, 400);
        setLocationRelativeTo(owner);
        setBackground(new Color(0, 0, 0, 0));

        JPanel root = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(dashboardGUI.theme.background());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(dashboardGUI.theme.cardBorderColor());
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };
        root.setOpaque(false);
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JLabel title = new JLabel("Settings");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(dashboardGUI.theme.primaryTextColor());
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        root.add(title);

        root.add(Box.createVerticalStrut(16));
        root.add(createDivider());
        root.add(Box.createVerticalStrut(20));

        root.add(createSectionLabel("Theme"));
        root.add(Box.createVerticalStrut(12));
        root.add(createThemeButtons());

        root.add(Box.createVerticalStrut(20));
        root.add(createDivider());
        root.add(Box.createVerticalStrut(20));

        root.add(createSectionLabel("Language"));
        root.add(Box.createVerticalStrut(12));
        root.add(createLanguageButtons());

        root.add(Box.createVerticalStrut(24));
        root.add(createDivider());
        root.add(Box.createVerticalStrut(16));

        JButton closeBtn = createTextButton("Close");
        closeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeBtn.addActionListener(e -> dispose());
        root.add(closeBtn);

        setContentPane(root);
    }

    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 15));
        label.setForeground(dashboardGUI.theme.primaryTextColor());
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private Component createDivider() {
        JPanel divider = new JPanel();
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        divider.setPreferredSize(new Dimension(0, 1));
        divider.setBackground(dashboardGUI.theme.subtleBorderColor());
        divider.setAlignmentX(Component.CENTER_ALIGNMENT);
        return divider;
    }

    private JPanel createThemeButtons() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton darkBtn = createStyledButton(null, g2 -> {
            int cx = 60, cy = 22;
            g2.setColor(dashboardGUI.theme.primaryTextColor());
            g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawArc(cx - 10, cy - 10, 20, 20, 30, 300);
            g2.drawArc(cx - 4, cy - 10, 20, 20, 120, -240);
        });
        darkBtn.addActionListener(e -> {
            dashboardGUI.setTheme(new DarkMode());
            SwingUtilities.updateComponentTreeUI(SwingUtilities.getWindowAncestor(this));
        });

        JButton lightBtn = createStyledButton(null, g2 -> {
            int cx = 60, cy = 22;
            int r = 6;
            g2.setColor(dashboardGUI.theme.primaryTextColor());
            g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawOval(cx - r, cy - r, r * 2, r * 2);
            for (int i = 0; i < 8; i++) {
                double angle = Math.toRadians(i * 45);
                int x1 = (int)(cx + (r + 3) * Math.cos(angle));
                int y1 = (int)(cy + (r + 3) * Math.sin(angle));
                int x2 = (int)(cx + (r + 7) * Math.cos(angle));
                int y2 = (int)(cy + (r + 7) * Math.sin(angle));
                g2.drawLine(x1, y1, x2, y2);
            }
        });
        lightBtn.addActionListener(e -> {
            dashboardGUI.setTheme(new LightMode());
            SwingUtilities.updateComponentTreeUI(SwingUtilities.getWindowAncestor(this));
        });

        // Fix 1: actually add the buttons to the row
        row.add(darkBtn);
        row.add(lightBtn);
        return row;
    }

    @FunctionalInterface
    interface IconPainter {
        void paint(Graphics2D g2);
    }

    private JButton createStyledButton(String text, IconPainter iconPainter) {
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
                g2.setColor(hovered ? dashboardGUI.theme.cardBorderColor() : dashboardGUI.theme.cardColor());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(dashboardGUI.theme.cardBorderColor());
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                if (iconPainter != null) iconPainter.paint(g2);
                g2.dispose();
                if (text != null) super.paintComponent(g);
            }

            @Override protected void paintBorder(Graphics g) {}
        };

        if (text != null) {
            btn.setText(text);
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.setForeground(dashboardGUI.theme.primaryTextColor());
        }

        btn.setPreferredSize(new Dimension(120, 44));
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel createLanguageButtons() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Fix 2: language buttons use createStyledButton with text, not createTextButton
        JButton engBtn = createStyledButton("EN", null);
        engBtn.addActionListener(e -> System.out.println("English"));

        JButton norBtn = createStyledButton("NO", null);
        norBtn.addActionListener(e -> System.out.println("Norwegian"));

        row.add(engBtn);
        row.add(norBtn);
        return row;
    }

    private JButton createTextButton(String text) {
        JButton btn = new JButton(text) {
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
                g2.setColor(hovered ? dashboardGUI.theme.cardBorderColor() : dashboardGUI.theme.cardColor());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(dashboardGUI.theme.cardBorderColor());
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
            @Override protected void paintBorder(Graphics g) {}
        };

        btn.setFont(new Font("Arial", Font.PLAIN, 13));
        btn.setForeground(dashboardGUI.theme.mutedTextColor());
        btn.setPreferredSize(new Dimension(80, 36));
        btn.setMaximumSize(new Dimension(80, 36));
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void open(Window owner) {
        new SettingsPanel(owner).setVisible(true);
    }
}