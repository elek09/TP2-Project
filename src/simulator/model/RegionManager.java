package simulator.model;

import org.json.JSONArray;
import org.json.JSONObject;
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
        this._regions = new DefaultRegion[_rows][_cols];
        this._region_width = width / cols;
        this._region_height = height / rows;
        this._animal_region = new HashMap<>();
        //it initialises the regions of the _regions array to new objects of type DefaultRegion (using the default constructor)         //done below
        //and initialises the _animal_region attribute .        //dont know how to do it    page 13

        for (int i = 0; i < _rows; i++) {
            for (int j = 0; j < _cols; j++) {
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
        a.init(this);
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

    public void unregister_animal(Animal a) {
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

    public void update_animal_region(Animal a) {
        // Calculate the row and column of the region based on the animal's position
        int row = (int) a.get_position().getY() / _region_height;
        int col = (int) a.get_position().getX() / _region_width;

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

    @Override
    public double get_food(Animal a, double dt) {
        // Calculate the row and column of the region based on the animal's position
        int row = (int) a.get_position().getY() / _region_height;
        int col = (int) a.get_position().getX() / _region_width;

        // Check if the row and col are within the valid range
        if (row >= 0 && row < _rows && col >= 0 && col < _cols) {
            // Get the region at the specified row and column
            Region r = _regions[row][col];

            // Get the food from the region
            return r.get_food(a, dt);
        } else {
            // Throw an exception if the row or col are out of range
            throw new IllegalArgumentException("Animal's position is out of range.");
        }
    }

    void update_all_regions(double dt) {
        for (int i = 0; i < _rows; i++) {
            for (int j = 0; j < _cols; j++) {
                _regions[i][j].update(dt);
            }
        }
    }

    private boolean is_in_view(Animal a, Animal otherAnimal) {
        Vector2D positionA = a.get_position();
        Vector2D positionB = otherAnimal.get_position();

        double distance = positionA.distanceTo(positionB);

        return distance <= a.get_sight_range();
    }

    public List<Region> get_regions_in_sight(Animal a) {
        List<Region> regionsInSight = new ArrayList<>();    //Create an empty list of regions
        Vector2D animalPosition = a.get_position();         //Get the position of the animal
        double sightRange = a.get_sight_range();            //Get the sight range of the animal

        for (int i = 0; i < _rows; i++) {
            for (int j = 0; j < _cols; j++) {
                Vector2D regionPosition = new Vector2D(j * this._region_width, i * this._region_height);    //Calculate the position of the region

                // Calculate the distance between the animal and the region
                double distance = animalPosition.distanceTo(regionPosition);

                if (distance <= sightRange) {
                    regionsInSight.add(_regions[i][j]);
                }
            }
        }

        return regionsInSight;
    }
/*
     @Override
     public List<Animal> get_animals_in_range(Animal a, Predicate<Animal> filter) {
         List<Animal> animalsInRange = new ArrayList<>();
         List<Region> regionsInSight = get_regions_in_sight(a);

         for (Region region : regionsInSight) {
             List<Animal> animalsInRegion = region.getAnimals();

             for (Animal animal : animalsInRegion) {
                 if (is_in_view(a, animal) && filter.test(animal)) {
                     animalsInRange.add(animal);
                 }
             }
         }
         return animalsInRange;
     }*/
    @Override
    public List<Animal> get_animals_in_range(Animal a, Predicate<Animal> filter) {
        List<Animal> animals_in_range = new ArrayList<>();
        Vector2D pos = a.get_position();
        double sight_range = a.get_sight_range();
        double row = pos.getX();
        double col = pos.getY();

        int col_mx = (int) (Math.max(0, col + sight_range) / _width);
        int col_mn = (int) (Math.max(0, col - sight_range) / _width);
        int row_mx = (int) (Math.max(0, row + sight_range) / _height);
        int row_mn = (int) (Math.max(0, row - sight_range) / _height);

        for (int f = row_mn; f < row_mx; f++) {
            for (int c = col_mn; c < col_mx; c++) {
                Region reg = _regions[f][c];
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
        for (int i = 0; i < _rows; i++) {
            for (int j = 0; j < _cols; j++) {
                Region region = _regions[i][j];
                if (region != null) {
                    JSONObject regionJson = region.as_JSON();
                    regionJson.put("row", i);
                    regionJson.put("col", j);
                    regionJson.put("data", region.as_JSON());
                    regions.put(regionJson);
                }
            }
        }
        json.put("regions", regions);
        return json;
    }


}
