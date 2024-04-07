package simulator.view;

import simulator.control.Controller;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
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
        speciesTable.setPreferredSize(new Dimension(500, 250));
        contentPanel.add(new JScrollPane(speciesTable));

        // Create and add the regions table to the contentPanel
        RegionsTableModel regionsModel = new RegionsTableModel(_ctrl);
        JTable regionsTable = new JTable(regionsModel);
        regionsTable.setPreferredSize(new Dimension(500, 250));
        contentPanel.add(new JScrollPane(regionsTable));

        // Add window listener
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                ViewUtils.quit(MainWindow.this);
            }
        });

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        pack();
        setVisible(true);
    }
}
