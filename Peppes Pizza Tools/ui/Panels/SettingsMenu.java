package ui.Panels;

import javax.swing.*;
import ui.dashboardGUI;
import ui.Themes.DarkMode;
import ui.Themes.LightMode;
import ui.Themes.ThemedComponent;
import utils.LanguageManager;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;

public class SettingsMenu extends JDialog implements ThemedComponent {

    private JLabel title;
    private JLabel themeLabel;
    private JLabel langLabel;
    private JButton darkBtn;
    private JButton lightBtn;
    private JButton engBtn;
    private JButton norBtn;
    private final PropertyChangeListener themeListener;

    public SettingsMenu(Window owner) {
        super(owner, ModalityType.DOCUMENT_MODAL);
        setUndecorated(true);
        setSize(500, 400);
        setLocationRelativeTo(owner);
        setBackground(new Color(0, 0, 0, 0));

        themeListener = evt -> applyTheme();
        dashboardGUI.addThemeListener(themeListener);

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

        root.add(createTitleRow());
        root.add(Box.createVerticalStrut(16));
        root.add(createDivider());
        root.add(Box.createVerticalStrut(20));

        themeLabel = createSectionLabel("");
        root.add(themeLabel);
        root.add(Box.createVerticalStrut(12));
        root.add(createThemeButtons());

        root.add(Box.createVerticalStrut(20));
        root.add(createDivider());
        root.add(Box.createVerticalStrut(20));

        langLabel = createSectionLabel("");
        root.add(langLabel);
        root.add(Box.createVerticalStrut(12));
        root.add(createLanguageButtons());

        setContentPane(root);
        applyTheme();

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                dashboardGUI.removeThemeListener(themeListener);
            }
        });
    }

    private JPanel createTitleRow() {
        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        titleRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        titleRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        title = new JLabel();
        title.setFont(new Font("Arial", Font.BOLD, 26));
        titleRow.add(title, BorderLayout.CENTER);

        JButton closeBtn = new JButton() {
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
                int cx = getWidth() / 2, cy = getHeight() / 2;
                if (hovered) {
                    g2.setColor(dashboardGUI.theme.lateColor());
                    g2.fillOval(2, 2, getWidth() - 4, getHeight() - 4);
                }
                g2.setColor(hovered ? dashboardGUI.theme.lateTextColor() : dashboardGUI.theme.mutedTextColor());
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(cx - 6, cy - 6, cx + 6, cy + 6);
                g2.drawLine(cx + 6, cy - 6, cx - 6, cy + 6);
                g2.dispose();
            }
            @Override protected void paintBorder(Graphics g) {}
        };
        closeBtn.setPreferredSize(new Dimension(32, 32));
        closeBtn.setContentAreaFilled(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> dispose());

        // Wrapper centers the button vertically in the title row
        JPanel closeBtnWrapper = new JPanel(new GridBagLayout());
        closeBtnWrapper.setOpaque(false);
        closeBtnWrapper.add(closeBtn);
        titleRow.add(closeBtnWrapper, BorderLayout.EAST);

        return titleRow;
    }

    @Override
    public void applyTheme() {
        title.setForeground(dashboardGUI.theme.primaryTextColor());
        themeLabel.setForeground(dashboardGUI.theme.primaryTextColor());
        langLabel.setForeground(dashboardGUI.theme.primaryTextColor());
        title.setText(LanguageManager.get("settings"));
        themeLabel.setText(LanguageManager.get("theme"));
        langLabel.setText(LanguageManager.get("language"));
        // --- THE FIX: Update Button Text & Foregrounds ---
        if (engBtn != null) {
            engBtn.setText(LanguageManager.get("english"));
            engBtn.setForeground(dashboardGUI.theme.primaryTextColor());
            engBtn.repaint();
        }
        if (norBtn != null) {
            norBtn.setText(LanguageManager.get("norwegian"));
            norBtn.setForeground(dashboardGUI.theme.primaryTextColor());
            norBtn.repaint();
        }
        if (engBtn != null) engBtn.repaint();
        if (norBtn != null) norBtn.repaint();
        repaint();
    }

    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 18));
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

        darkBtn = createStyledButton(null, g2 -> {
            int cx = 60, cy = 22;
            boolean active = dashboardGUI.theme instanceof DarkMode;

            // Crescent moon — fill circle then cut with background color
            Shape originalClip = g2.getClip();
            g2.setClip(new java.awt.geom.Ellipse2D.Float(cx - 10, cy - 10, 20, 20));
            g2.fillOval(cx - 10, cy - 10, 20, 20);
            g2.setColor(active ? dashboardGUI.theme.onTimeColor() : dashboardGUI.theme.cardColor());
            g2.fillOval(cx - 3, cy - 13, 20, 20);
            g2.setClip(originalClip);

        }, () -> dashboardGUI.theme instanceof DarkMode,
           e -> dashboardGUI.setTheme(new DarkMode()));

        lightBtn = createStyledButton(null, g2 -> {
            int cx = 60, cy = 22, r = 6;
            g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawOval(cx - r, cy - r, r * 2, r * 2);
            for (int i = 0; i < 8; i++) {
                double angle = Math.toRadians(i * 45);
                g2.drawLine(
                    (int)(cx + (r + 3) * Math.cos(angle)), (int)(cy + (r + 3) * Math.sin(angle)),
                    (int)(cx + (r + 7) * Math.cos(angle)), (int)(cy + (r + 7) * Math.sin(angle))
                );
            }
        }, () -> dashboardGUI.theme instanceof LightMode,
           e -> dashboardGUI.setTheme(new LightMode()));

        row.add(darkBtn);
        row.add(lightBtn);
        return row;
    }

    private JPanel createLanguageButtons() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        row.setOpaque(false);

        engBtn = createStyledButton(LanguageManager.get("english"), null,
            () -> LanguageManager.getLanguage() == LanguageManager.Language.EN,
            e -> { LanguageManager.setLanguage(LanguageManager.Language.EN); applyTheme(); });

        norBtn = createStyledButton(LanguageManager.get("norwegian"), null,
            () -> LanguageManager.getLanguage() == LanguageManager.Language.NO,
            e -> { LanguageManager.setLanguage(LanguageManager.Language.NO); applyTheme(); });

        row.add(engBtn);
        row.add(norBtn);
        return row;
    }

    private JButton createStyledButton(String text, IconPainter iconPainter,
            java.util.function.BooleanSupplier isActive,
            java.awt.event.ActionListener action) {
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

                boolean active = isActive.getAsBoolean();

                if (active) {
                    g2.setColor(dashboardGUI.theme.onTimeColor());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.setColor(dashboardGUI.theme.onTimeTextColor());
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                    g2.setColor(dashboardGUI.theme.onTimeTextColor());
                } else {
                    g2.setColor(hovered ? dashboardGUI.theme.cardBorderColor() : dashboardGUI.theme.cardColor());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.setColor(dashboardGUI.theme.cardBorderColor());
                    g2.setStroke(new BasicStroke(1f));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                    g2.setColor(dashboardGUI.theme.primaryTextColor());
                }

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
        btn.addActionListener(action);
        return btn;
    }

    @FunctionalInterface interface IconPainter { void paint(Graphics2D g2); }
    public static void open(Window owner) { new SettingsMenu(owner).setVisible(true); }
}