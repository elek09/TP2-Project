package simulator.model;

import org.json.JSONArray;
import org.json.JSONObject;
import simulator.misc.Utils;
import simulator.misc.Vector2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RegionManager implements AnimalMapView {
    private final int _rows;
    private final int _cols;
    private final int _width;
    private final int _height;
    private final int _region_width;
    private final int _region_height;
    private Region[][] _regions;
    private Map<Animal, Region> _animal_region;

    public RegionManager(int cols, int rows, int width, int height) {
        this._rows = rows;
        this._cols = cols;
        this._width = width;
        this._height = height;
        this._regions = new DefaultRegion[_cols][_rows];
        this._region_width = width / cols;
        this._region_height = height / rows;
        this._animal_region = new HashMap<Animal, Region>();


        for (int i = 0; i < _cols; i++) {
            for (int j = 0; j < _rows; j++) {
                _regions[i][j] = new DefaultRegion();
            }
        }
    }


    public int get_cols() {
        return _cols;
    }

    public int get_rows() {
        return _rows;
    }

    public int get_width() {
        return _width;
    }

    public int get_height() {
        return _height;
    }

    public int get_region_width() {
        return _region_width;
    }

    public int get_region_height() {
        return _region_height;
    }

    public void set_region(int row, int col, Region r) {
        // Check if the row and col are within the valid range
        if (row >= 0 && row < _rows && col >= 0 && col < _cols) {
            // Get the current region at the specified row and column
            Region currentRegion = _regions[col][row];

            // Add all the animals from the current region to the new region
            for (Map.Entry<Animal, Region> entry : _animal_region.entrySet()) {
                if (entry.getValue().equals(currentRegion)) {
                    // Add the animal to the new region
                    r.add_animal(entry.getKey()); // Assuming you have a method in Region to add an animal

                    // Update the _animal_region map
                    _animal_region.put(entry.getKey(), r);
                }
            }

            // Set the region at the specified row and column to the new region
            _regions[col][row] = r;
        } else {
            // Throw an exception if the row or col are out of range
            throw new IllegalArgumentException("Row or column out of range.");
        }
    }


    public void register_animal(Animal a) {
        a.init(this);
        // Calculate the row and column of the region based on the animal's position
        int row = (int) a.get_position().getY() / _region_height; // Assuming you have a method in Animal to get its position
        int col = (int) a.get_position().getX() / _region_width; // and the position has methods to get x and y coordinates

        // Check if the row and col are within the valid range
        if (row >= 0 && row < _rows && col >= 0 && col < _cols) {
            // Get the region at the specified row and column
            Region r = _regions[col][row];
            // Add the animal to the region
            r.add_animal(a);
            // Update the _animal_region map
            _animal_region.put(a, r);
        } else {
            // Throw an exception if the row or col are out of range
            throw new IllegalArgumentException("Animal's position is out of range.");
        }
    }

    public void unregister_animal(Animal a) {
        Region reg = _animal_region.get(a);

        if (reg != null) {
            reg.remove_animal(a);
        }
        _animal_region.remove(a);
    }

    public void update_animal_region(Animal a) {
        double x = a.get_position().getX();
        double y = a.get_position().getY();

        int row = (int) Utils.constrain_value_in_range(y / _region_height, 0, _rows - 1);
        int col = (int) Utils.constrain_value_in_range(x / _region_width, 0, _cols - 1);
        Region regCurrent = _animal_region.get(a);
        Region regNew = _regions[col][row];

        if(regNew != regCurrent){
            if(regCurrent != null){
                regCurrent.remove_animal(a);
            }
            if(regNew != null){
                regNew.add_animal(a);
                _animal_region.put(a, regNew);
            }
        }
    }


    public double get_food(Animal a, double dt) {
        Region reg = _animal_region.get(a);
        double food = 0;
        if (reg != null){
            food = reg.get_food(a, dt);

        }
        return food;
    }

    void update_all_regions(double dt) {
        for (int i = 0; i < _cols; i++) {
            for (int j = 0; j < _rows; j++) {
                _regions[i][j].update(dt);
            }
        }
    }
    @Override
    public List<Animal> get_animals_in_range(Animal a, Predicate<Animal> filter) {
        List<Animal> animals_in_range = new ArrayList<>();
        Vector2D pos = a.get_position();
        double sight_range = a.get_sight_range();
        double col = pos.getX();
        double row = pos.getY();

        int col_mx = (int) (Math.max(0, col + sight_range) / _region_width);
        int col_mn = (int) (Math.max(0, col - sight_range) / _region_width);
        int row_mx = (int) (Math.max(0, row + sight_range) / _region_height);
        int row_mn = (int) (Math.max(0, row - sight_range) / _region_height);

        if (col_mx >= _cols){
            col_mx = _cols - 1;
        }
        else if(col_mn < 0){
            col_mn = 0;
        }
        else if(row_mx >= _rows){
            row_mx = _rows - 1;
        }
        else if(row_mn < 0 || row_mn >= _rows){
            row_mn = 0;
        }

        for (int f = row_mn; f < row_mx; f++) {
            for (int c = col_mn; c < col_mx; c++) {
                Region reg = _regions[c][f];
                for (Animal animal : reg.getAnimals()) {
                    if (filter.test(animal)) {
                        animals_in_range.add(animal);
                    }
                }

            }
        }
        return animals_in_range;
}



    public JSONObject as_JSON() {
        JSONObject json = new JSONObject();
        JSONArray regions = new JSONArray();
        for (int i = 0; i < _cols; i++) {
            for (int j = 0; j < _rows; j++) {
                Region region = _regions[i][j];
                if (region != null) {
                    JSONObject regionJson = region.as_JSON();
                    regionJson.put("row", j);
                    regionJson.put("col", i);
                    regionJson.put("data", region.as_JSON());
                    regions.put(regionJson);
                }
            }
        }
        json.put("regions", regions);
        return json;
    }

}
