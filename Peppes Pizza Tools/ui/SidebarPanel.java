package ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SidebarPanel extends JPanel {
    private JLabel clockLabel;
    private JLabel iconLabel;
    private ImageIcon peppesIcon = new ImageIcon("Assets/peppesIcon.png");
    private Integer sidebarWidth = 100;
    private Integer sidebarHeight = 0; // DOES NOT MATTER -> Overriden by parent

    public SidebarPanel(JPanel contentArea) {
        setPreferredSize(new Dimension(sidebarWidth, sidebarHeight));
        setBackground(new Color(40, 40, 40));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // TOP: Logo
        ImageIcon scaledIcon = new ImageIcon(peppesIcon.getImage().getScaledInstance(sidebarWidth, sidebarWidth, Image.SCALE_SMOOTH));
        iconLabel = new JLabel(scaledIcon);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(20));
        add(iconLabel);

        // Spring pushes button down to the middle
        add(Box.createVerticalGlue());

        // MIDDLE: Toggle button
        JButton toggleBtn = new JButton("⇄");
        toggleBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        toggleBtn.setPreferredSize(new Dimension(70, 70));
        toggleBtn.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 24));
        toggleBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout) contentArea.getLayout();
            cl.next(contentArea);
        });
        add(toggleBtn);

        // Spring pushes clock down to the bottom
        add(Box.createVerticalGlue());

        // BOTTOM: Clock
        clockLabel = new JLabel("00:00");
        clockLabel.setForeground(Color.WHITE);
        clockLabel.setFont(new Font("Arial", Font.BOLD, 30));
        clockLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(clockLabel);
        add(Box.createVerticalStrut(20)); // Small bottom margin

        // Timer
        new Timer(1000, e -> {
            clockLabel.setText(
                java.time.LocalTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))
            );
        }).start();
    }
}