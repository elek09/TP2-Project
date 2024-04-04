package simulator.model;

import simulator.view.ChangeRegionsDialog;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public interface EcoSysObserver {
    void onRegister(double time, MapInfo map, List<AnimalInfo> animals);
    void onReset(double time, MapInfo map, List<AnimalInfo> animals);
    void onAnimalAdded(double time, MapInfo map, List<AnimalInfo> animals, AnimalInfo a);
    void onRegionSet(int row, int col, MapInfo map, RegionInfo r);
    void open(Component parent);
    void onAvanced(double currentTime, RegionManager regionManager, ArrayList<Animal> animals, double dt);
}