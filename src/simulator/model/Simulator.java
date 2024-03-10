package simulator.model;

import org.json.JSONObject;
import simulator.factories.Factory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Simulator implements JSONable {

    private final Factory<Animal> animalsFactory;
    private final Factory<Region> regionsFactory;
    private final RegionManager regionManager;
    private final List<Animal> animals;
    private double currentTime;

    /**
     * Constructor for the Simulator class
     *
     * @param width          The width of the map
     * @param height         The height of the map
     * @param cols           The number of columns in the map
     * @param rows           The number of rows in the map
     * @param animalsFactory The factory for creating animals
     * @param regionsFactory The factory for creating regions
     */
    public Simulator(int width, int height, int cols, int rows, Factory<Animal> animalsFactory, Factory<Region> regionsFactory) {
        this.animalsFactory = animalsFactory;
        this.regionsFactory = regionsFactory;
        this.regionManager = new RegionManager(cols, rows, width, height);
        this.animals = new ArrayList<>();
        this.currentTime = 0.0;
    }

    /**
     * Sets the region at the specified row and column to the given region.
     *
     * @param row The row index of the region.
     * @param col The column index of the region.
     * @param r   The region to be set.
     */
    private void set_region(int row, int col, Region r) {
        regionManager.set_region(row, col, r);
    }

    /**
     * Sets the region at the specified row and column to the given region.
     *
     * @param row    The row index of the region.
     * @param col    The column index of the region.
     * @param r_json The JSON object representing the region to be set.
     */
    public void set_region(int row, int col, JSONObject r_json) {
        if (r_json == null) {
            Region region = regionsFactory.createInstance(r_json);
            set_region(row, col, region);
        }
    }

    /**
     * Adds the provided animal to the simulator.
     *
     * @param a The JSON object representing the animal to be added.
     */
    private void add_animal(Animal a) {
        animals.add(a);
        regionManager.register_animal(a);
    }

    public void add_animal(JSONObject a_json) {
        Animal animal = animalsFactory.createInstance(a_json);
        add_animal(animal);
    }

    /**
     * Returns the information about the map (regions) managed by the simulator.
     *
     * @return The region manager containing map information.
     */
    public RegionManager get_map_info() {
        return regionManager;
    }

    /**
     * Retrieves an unmodifiable list of animals currently in the simulator.
     *
     * @return An unmodifiable list of animal information.
     */
    public List<? extends AnimalInfo> getAnimals() {
        return Collections.unmodifiableList(animals);
    }

    public double get_time() {
        return currentTime;
    }

    /**
     * Advances the simulation by the specified time increment.
     * Updates the time, animals' states, and their regions accordingly.
     *
     * @param dt The time increment for the simulation advancement.
     */
    public void advance(double dt) {
        currentTime += dt;
        for (int i = 0; i < animals.size(); i++) {
            Animal animal = animals.get(i);
            if (animal.get_state() == State.DEAD) {
                animals.remove(animal);
                regionManager.unregister_animal(animal);
            } else {
                animal.update(dt);
                regionManager.update_animal_region(animal);
                if (animal.is_pregnant()) {
                    Animal baby = animal.deliver_baby();
                    add_animal(baby);
                }
            }
        }
        regionManager.update_all_regions(dt);
    }

    @Override
    public JSONObject as_JSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", currentTime);
        jsonObject.put("state", regionManager.as_JSON());
        return jsonObject;
    }
}

