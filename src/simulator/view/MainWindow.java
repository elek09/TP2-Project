package simulator.view;

import simulator.control.Controller;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private Controller _ctrl;

    public MainWindow(Controller ctrl) {
        super("[ECOSYSTEM SIMULATOR]");
        _ctrl = ctrl;
        initGUI();
    }

    private void initGUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        // Create and add ControlPanel in the PAGE_START section of mainPanel
        ControlPanel controlPanel = new ControlPanel(_ctrl);
        mainPanel.add(controlPanel, BorderLayout.PAGE_START);

        // Create and add StatusBar in the PAGE_END section of mainPanel
        StatusBar statusBar = new StatusBar(_ctrl);
        mainPanel.add(statusBar, BorderLayout.PAGE_END);

        // Definition of the tables panel (use a vertical BoxLayout)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Create and add the species table to the contentPanel
        SpeciesTableModel speciesModel = new SpeciesTableModel(_ctrl);
        JTable speciesTable = new JTable(speciesModel);
        JPanel speciesPanel = createTitledPanel("Species", speciesTable, 500, 250);
        contentPanel.add(speciesPanel);

        // Create and add the regions table to the contentPanel
        RegionsTableModel regionsModel = new RegionsTableModel(_ctrl);
        JTable regionsTable = new JTable(regionsModel);
        JPanel regionsPanel = createTitledPanel("Regions", regionsTable, 500, 250);
        contentPanel.add(regionsPanel);

        // Add window listener
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                ViewUtils.quit(MainWindow.this);
            }
        });

        // Set window properties
        setLocation(50, 50);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private JPanel createTitledPanel(String title, JTable table, int width, int height) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createTitledBorder(title)
        ));
        table.setPreferredScrollableViewportSize(new Dimension(width, height));
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
}
