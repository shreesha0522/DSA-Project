import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class NetworkTopologyGUI extends JFrame {
    private JPanel graphPanel;
    private JTextArea logArea;
    private JButton optimizeButton, calculatePathButton;

    public NetworkTopologyGUI() {
        setTitle("Network Topology Optimizer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        graphPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawNetwork(g);
            }
        };
        graphPanel.setPreferredSize(new Dimension(600, 600));
        add(graphPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        optimizeButton = new JButton("Optimize Network");
        calculatePathButton = new JButton("Calculate Shortest Path");
        logArea = new JTextArea(5, 30);

        controlPanel.add(optimizeButton);
        controlPanel.add(calculatePathButton);
        add(controlPanel, BorderLayout.NORTH);
        add(new JScrollPane(logArea), BorderLayout.SOUTH);

        optimizeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logArea.append("Optimizing network topology...\n");
                // Implement optimization logic here
            }
        });

        calculatePathButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logArea.append("Calculating shortest path...\n");
                // Implement shortest path calculation here
            }
        });
    }

    private void drawNetwork(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillOval(100, 100, 30, 30); // Example node
        g.fillOval(200, 200, 30, 30); // Example node
        g.setColor(Color.BLACK);
        g.drawLine(115, 115, 215, 215); // Example edge
        g.drawString("Cost: 10, BW: 50", 150, 170);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NetworkTopologyGUI().setVisible(true));
    }
}
