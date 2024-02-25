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

        //it initialises the regions of the _regions array to new objects of type DefaultRegion (using the default constructor)         //done below
        //and initialises the _animal_region attribute .        //dont know how to do it    page 13

        for (int i = 0; i < _rows; i++){
            for (int j = 0; j < _cols; j++){
                _regions[i][j] = new DefaultRegion();
            }
        }
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

    public void unregister_animal(Animal a){
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
    public void update_animal_region(Animal a){
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
    /**
     * We have to query over all the regions that are in the sight of view of the animal,
     * Meaning that we need to check all the points in a circle of the sight range of the animal and check if the region is in the sight of view of the animal
     * and then check all the animals in the region and check if they are in the sight of view of the animal and return the ones that are.
     * @return A list of animals that are in the same region as the specified animal and match the filter.
     */
    //animal only could see the animals in the same region as it?

    //we could use the distanceTo method from the Vector2D class to calculate the distance between the two animal and check if it is in his sight range

    //so we used this distanceTo method check that which regions the animal can see and then we can check the animals in those regions and check if they are in the sight range of the animal

    /*@Override
    public List<Animal> get_animals_in_range22(Animal a, Predicate<Animal> filter) {
        List<Animal> animalsInRange = new ArrayList<>();

        // Get the list of regions that fall inside the field of view of the animal
        List<Region> regionsInView = get_regions_in_view(a);

        // Iterate over each region
        for (Region region : regionsInView) {
            // Get the list of animals in the current region
            List<Animal> animalsInRegion = region.getAnimals();

            // Iterate over each animal in the region
            for (Animal animal : animalsInRegion) {
                // Check if the animal is in the field of view of the given animal and satisfies the filter condition
//                if (is_in_view(a, animal) && filter.test(animal)) {
//                    // If so, add it to the list of animals in range
//                    animalsInRange.add(animal);
//                }
            }
        }

        return animalsInRange;
    }*/


    private boolean is_in_view(Animal a, Animal otherAnimal) {
        Vector2D positionA = a.get_position();
        Vector2D positionB = otherAnimal.get_position();

        double distance = positionA.distanceTo(positionB);

        if (distance <= a.get_sight_range()) {
            return true;
        } else {
            return false;
        }
    }

    public List<Region> get_regions_in_sight(Animal a) {
        List<Region> regionsInSight = new ArrayList<>();

        Vector2D animalPosition = a.get_position();
        double sightRange = a.get_sight_range();

        for (int i = 0; i < _rows; i++) {
            for (int j = 0; j < _cols; j++) {
                Vector2D regionPosition = new Vector2D(j * _region_width, i * _region_height);

                // Calculate the distance between the animal and the region
                double distance = animalPosition.distanceTo(regionPosition);

                if (distance <= sightRange) {
                    regionsInSight.add(_regions[i][j]);
                }
            }
        }

        return regionsInSight;
    }

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

    /*
    public List<Region> get_regions_in_view(Animal a) {
        List<Region> regionsInView = new ArrayList<>();

        // Get the field of view of the animal
        double fieldOfView = a.get_sight_range();

        // Get the position of the animal
        Vector2D animalPosition = a.get_position();

        // Iterate over each region
        for (Region[] region : _regions) {
            // Get the position of the region
           // Vector2D regionPosition = region;

            // Calculate the distance between the animal and the region
            double distance = 0;// = animalPosition.distanceTo(regionPosition);

            // If the region is within the field of view of the animal, add it to the list
            if (distance <= fieldOfView) {
                //regionsInView.add(region);
            }
        }

        return regionsInView;
    }*/


}
