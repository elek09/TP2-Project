package simulator.view;

import simulator.model.*;
import simulator.control.Controller;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RegionsTableModel extends AbstractTableModel implements EcoSysObserver {
    private static final long serialVersionUID = 1L;
    private Controller _ctrl;
    private List<RegionInfo> _regions;
    private List<String> _columns;
    private List<List<Object>> _data;

    RegionsTableModel(Controller ctrl) {
        // TODO initialise the corresponding data structures
        _ctrl = ctrl;
        _columns = new ArrayList<>();
        _data = new ArrayList<>();
        _regions = new ArrayList<>();
        _ctrl.addObserver(this);
    }
    @Override
    public int getRowCount() {
        return 0;
    }

    @Override
    public int getColumnCount() {
        return 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        RegionInfo r = _regions.get(rowIndex);
        switch (columnIndex) {
            default:
                return null;
        }
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
    public void open(Component parent) {

    }

    @Override
    public void onAvanced(double currentTime, RegionManager regionManager, ArrayList<Animal> animals, double dt) {

    }
}
