package simulator.model;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import simulator.factories.Factory;

public class Simulator implements JSONable {

    private int cols;
    private int rows;
    private int width;
    private int height;
    private final Factory<Animal> animalsFactory;
    private final Factory<Region> regionsFactory;
    private final RegionManager regionManager;
    private final List<Animal> animals;
    private double currentTime;


    public Simulator(int width, int height, int cols, int rows, Factory<Animal> animalsFactory, Factory<Region> regionsFactory) {
        this.cols = cols;
        this.rows = rows;
        this.width = width;
        this.height = height;
        this.animalsFactory = animalsFactory;
        this.regionsFactory = regionsFactory;
        this.regionManager = new RegionManager(cols, rows, width, height);
        this.animals = new ArrayList<>();
        this.currentTime = 0.0;
    }


    private void set_region(int row, int col, Region r) {
        regionManager.set_region(row, col, r);
    }

    public void set_region(int row, int col, JSONObject r_json) {
        Region region = regionsFactory.createInstance(r_json);
        set_region(row, col, region);
    }

    private void add_animal(Animal a) {
        animals.add(a);
        regionManager.register_animal(a);
    }

    public void add_animal(JSONObject a_json) {
        Animal animal = animalsFactory.createInstance(a_json);
        add_animal(animal);
    }

    public RegionManager get_map_info() {
        return regionManager;
    }

    public List<? extends AnimalInfo> getAnimals() {
        return Collections.unmodifiableList(animals);
    }

    public double get_time() {
        return currentTime;
    }

    public void advance(double dt) {
        currentTime += dt;
        //probably better to use the functions what we wrote for this like unregister_animal thats why i modified below, but we can discuss it
        for (Animal animal : animals) {
            if (animal.get_state() == State.DEAD) {
                regionManager.unregister_animal(animal);
            }
        }

        // 3. For each animal: call its update(dt) and then ask the region manager to update its current region status.
        for (Animal animal : animals) {
            animal.update(dt);
            regionManager.update_animal_region(animal);
        }

        // Ask the region manager to update all regions.
        regionManager.update_all_regions(dt);

        // Check for pregnancy and add babies
        for (Animal animal : animals) {
            if (animal.is_pregnant()) {
                Animal baby = animal.deliver_baby();
                add_animal(baby);
            }
        }
    }

    @Override
    public JSONObject as_JSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", currentTime);
        jsonObject.put("state", regionManager.as_JSON());
        return jsonObject;
    }
}

