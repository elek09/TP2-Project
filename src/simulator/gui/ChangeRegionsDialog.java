package simulator.gui;

import org.json.JSONObject;
import simulator.control.Controller;
import simulator.model.*;
import simulator.launcher.Main;

import javax.swing.table.DefaultTableModel;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChangeRegionsDialog extends JDialog implements EcoSysObserver {
    private DefaultComboBoxModel<String> _regionsModel;
    private DefaultComboBoxModel<String> _fromRowModel;
    private DefaultComboBoxModel<String> _toRowModel;
    private DefaultComboBoxModel<String> _fromColModel;
    private DefaultComboBoxModel<String> _toColModel;
    private DefaultTableModel _dataTableModel;
    private Controller _ctrl;
    private List<JSONObject> _regionsInfo;
    private String[] _headers = { "Key", "Value", "Description" };
    // TODO add any necessary attributes here...

    ChangeRegionsDialog(Controller ctrl) {
        super((Frame)null, true);
        _ctrl = ctrl;
        initGUI();
        _ctrl.addObserver(this);
    }

    private void initGUI() {
        setTitle("Change Regions");
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        setContentPane(mainPanel);

        // TODO create several panels to organize visual components in the dialog, and add them to the main panel.
        // Help text panel
        JPanel helpPanel = new JPanel();
        JLabel helpLabel = new JLabel("Help text goes here...");
        helpPanel.add(helpLabel);
        mainPanel.add(helpPanel);

        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        _dataTableModel = new DefaultTableModel(_headers, 0);
        JTable dataTable = new JTable(_dataTableModel);
        JScrollPane scrollPane = new JScrollPane(dataTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(tablePanel);

        // Comboboxes panel
        JPanel comboBoxPanel = new JPanel();
        // Create and add comboboxes here using _regionsModel, _fromRowModel, _toRowModel, _fromColModel, _toColModel
        // Add comboboxes to comboBoxPanel
        mainPanel.add(comboBoxPanel);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        okButton.addActionListener(e -> {
            // Implement OK button functionality
        });

        cancelButton.addActionListener(e -> {
            int _status = 0;
            setVisible(false);
        });
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel);

        setPreferredSize(new Dimension(700, 400));
        pack();
        setResizable(false);
        setVisible(false);

        // _regionsInfo will be used to set the information in the table
        _regionsInfo = Main.region_factory.get_info();

        // _dataTableModel is a table model that includes all the parameters of the region
        _dataTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // TODO make only column 1 editable
                return false;
            }
        };
        _dataTableModel.setColumnIdentifiers(_headers);

        // TODO create a JTable that uses _dataTableModel, and add it to the dialog.

        // _regionsModel is a combobox model that includes the types of regions
        _regionsModel = new DefaultComboBoxModel<>();

        // TODO add the description of all regions to _regionsModel, for this use the "desc" or "type" key of the JSONObjects in _regionsInfo,
        // as these provide information about what the factory can create.

        // TODO create a combobox that uses _regionsModel and add it to the dialog.

        // TODO create 4 combobox models for _fromRowModel, _toRowModel, _fromColModel, and _toColModel.

        // TODO create 4 comboboxes that use these models and add them to the dialog.

        // TODO create the OK and Cancel buttons and add them to the dialog.

        setPreferredSize(new Dimension(700, 400)); // you can use a different size
        pack();
        setResizable(false);
        setVisible(false);
    }

    @Override
    public void onRegister(double time, MapInfo map, List<AnimalInfo> animals) {

    }

    @Override
    public void onReset(double time, MapInfo map, List<AnimalInfo> animals) {

    }

    @Override
    public void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a) {

    }

    @Override
    public void onRegionSet(int row, int col, MapInfo map, RegionInfo r) {

    }
    @Override
    public void open(ChangeRegionsDialog parent) {
        setLocation(
                parent.getLocation().x + parent.getWidth() / 2 - getWidth() / 2,
                parent.getLocation().y + parent.getHeight() / 2 - getHeight() / 2
        );
        pack();
        setVisible(true);
    }

    @Override
    public void onAvanced(double currentTime, RegionManager regionManager, ArrayList<Animal> animals, double dt) {

    }

    // TODO the rest of the methods go here...
}

