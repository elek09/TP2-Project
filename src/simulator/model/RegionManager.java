package simulator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class RegionManager implements AnimalMapView {
    private int _rows;
    private int _cols;
    private int _width;
    private int _height;
    private int _region_width;
    private int _region_height;
    private List<DefaultRegion> _regions;
    private Map<Animal, Region> _animalRegionMap;
    public RegionManager(int cols, int rows, int width, int height){
        _rows = rows;
        _cols = cols;
        _width = width;
        _height = height;
        _regions = new ArrayList<>();
    }

    public int get_cols(){
        return _cols;
    }
    public int get_rows(){
        return _rows;
    }
    public int get_width(){
        return _width;
    }
    public int get_height(){
        return _height;
    }
    public int get_region_width(){
        return _region_width;
    }
    public int get_region_height(){
        return _region_height;
    }
    public void set_region(int row, int co  l, Region r){
        if (row == _rows && col == _cols){
            _regions.add();
        }

    }

    @Override
    public List<Animal> get_animals_in_range(Animal e, Predicate<Animal> filter) {
        return null;
        //Mi madre trabaja en colombia
    }
}
