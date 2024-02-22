package simulator.model;

import org.json.JSONArray;
import org.json.JSONObject;
import simulator.misc.Vector2D;

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
    private Region[][] _regions;
    private Map<Animal, Region> _animal_region;
    private Animal a;
    private Predicate<Animal> filter;

    public RegionManager(int cols, int rows, int width, int height){
        this._rows = rows;
        this._cols = cols;
        this._width = width;
        this._height = height;
        this._regions = new DefaultRegion[_rows][_cols];
        this._region_width = width/cols;
        this._region_height = height/rows;
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
    public void set_region(int row, int col, Region r) {
        // Check if the row and col are within the valid range
        if (row >= 0 && row < _rows && col >= 0 && col < _cols) {
            // Get the current region at the specified row and column
            Region currentRegion = _regions[row][col];

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
            _regions[row][col] = r;
        } else {
            // Throw an exception if the row or col are out of range
            throw new IllegalArgumentException("Row or column out of range.");
        }
    }


    public void register_animal(Animal a) {
        // Calculate the row and column of the region based on the animal's position
        int row = (int) a.get_position().getY() / _region_height; // Assuming you have a method in Animal to get its position
        int col = (int) a.get_position().getX() / _region_width; // and the position has methods to get x and y coordinates

        // Check if the row and col are within the valid range
        if (row >= 0 && row < _rows && col >= 0 && col < _cols) {
            // Get the region at the specified row and column
            Region r = _regions[row][col];

            // Add the animal to the region
            r.add_animal(a);

            // Update the _animal_region map
            _animal_region.put(a, r);
        } else {
            // Throw an exception if the row or col are out of range
            throw new IllegalArgumentException("Animal's position is out of range.");
        }
    }

    void unregister_animal(Animal a){
        // Calculate the row and column of the region based on the animal's position
        // Cast to int is needed because in Animal, position is a Vector2D and getX and getY return double (?).
        int row = (int) a.get_position().getY() / _region_height;
        int col = (int) a.get_position().getX() / _region_width;

        // Check if the row and col are within the valid range
        if (row >= 0 && row < _rows && col >= 0 && col < _cols) {
            // Get the region at the specified row and column
            Region r = _regions[row][col];

            // Remove the animal from the region
            r.remove_animal(a);

            // Update the _animal_region map
            _animal_region.remove(a, r);
        } else {
            // Throw an exception if the row or col are out of range
            throw new IllegalArgumentException("Animal's position is out of range.");
        }
    }
    void update_animal_region(Animal a){
        // Calculate the row and column of the region based on the animal's position
        int row = (int) a.get_position().getY() / _region_height;
        int col = (int) a.get_position().getX() / _region_width ;

        // Check if the row and col are within the valid range
        if (row >= 0 && row < _rows && col >= 0 && col < _cols) {
            // Get the region at the specified row and column
            Region r = _regions[row][col];

            // Get the current region of the animal
            Region currentRegion = _animal_region.get(a);

            // If the animal has moved to a different region
            if (!r.equals(currentRegion)) {
                // Remove the animal from the current region
                currentRegion.remove_animal(a);

                // Add the animal to the new region
                r.add_animal(a);

                // Update the _animal_region map
                _animal_region.put(a, r);
            }
        } else {
            // Throw an exception if the row or col are out of range
            throw new IllegalArgumentException("Animal's position is out of range.");
        }

    }
    public double get_food(Animal a, double dt){
        // Calculate the row and column of the region based on the animal's position
        int row = (int) a.get_position().getY() / _region_height;
        int col = (int) a.get_position().getX() / _region_width;

        // Check if the row and col are within the valid range
        if (row >= 0 && row < _rows && col >= 0 && col < _cols) {
            // Get the region at the specified row and column
            Region r = _regions[row][col];

            // Get the food from the region
            return r.getFood(a, dt);
        } else {
            // Throw an exception if the row or col are out of range
            throw new IllegalArgumentException("Animal's position is out of range.");
        }
    }
    void update_all_regions(double dt){
        for (int i = 0; i < _rows; i++){
            for (int j = 0; j < _cols; j++){
                _regions[i][j].update(dt);
            }
        }
    }
    //This is weird cuz I don't know pretty much anything about the filter stuff.

    @Override
    public List<Animal> get_animals_in_range(Animal a, double filter){

        List<Animal> animalsInRange = new ArrayList<>();
        // Calculate the row and column of the region based on the animal's position
        int row = (int) a.get_position().getY() / _region_height;
        int col = (int) a.get_position().getX() / _region_width;

        // Check if the row and col are within the valid range
        if (row >= 0 && row < _rows && col >= 0 && col < _cols) {
            // Get the region at the specified row and column
            Region r = _regions[row][col];

            // Get the animals in the region that match the filter
            for (Animal animal : r.getAnimals()) {
                if (filter.test(animal)) {
                    animalsInRange.add(animal);
                }
            }
        } else {
            // Throw an exception if the row or col are out of range
            throw new IllegalArgumentException("Animal's position is out of range.");
        }

        return animalsInRange;
    }
    public JSONObject as_JSON(){
        JSONObject json = new JSONObject();
        JSONArray regions = new JSONArray();
        for (int i = 0; i < _rows; i++){
            for (int j = 0; j < _cols; j++){
                regions.put(_regions[i][j].as_JSON());
            }
        }
        json.put("regions", regions);
        return json;
    }
    public Vector2D adjust_position(Vector2D pos){
        double x = pos.getX();
        double y = pos.getY();
        while (x >= this._width) x = (x - this._width);
        while (x < 0) x = (x + this._width);
        while (y >= this._height) y = (y - this._height);
        while (y < 0) y = (y + this._height);

        return new Vector2D(x, y);
    }

}
