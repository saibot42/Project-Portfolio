package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import planners.DeliveryOverview;

public class DeliveryListPanel extends JPanel {
    private DeliveryOverview deliveries;

    public DeliveryListPanel(DeliveryOverview deliveries) {
        this.deliveries = deliveries;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel inTransitLabel = new JLabel("In Transit");
        inTransitLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JList<String> inTransitList = new JList<>();
        JList<String> pendingList = new JList<>();

        //// TODO: Populate JList
        ///  
        /// 
        ///

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.add(new JLabel("In Transit"));
        container.add(new JScrollPane(inTransitList));
        container.add(new JLabel("Waiting"));
        container.add(new JScrollPane(pendingList));

        add(container, BorderLayout.CENTER);
    }

    
}
