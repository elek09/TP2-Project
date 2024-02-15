package simulator.model;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.model.Constants;
import simulator.factories.Factory;

public class Simulator implements JSONable {

    private final int cols;
    private final int rows;
    private final int width;
    private final int height;
    private final Factory<Animal> animalsFactory;
    private final Factory<Region> regionsFactory;
    private final RegionManager regionManager;
    private final List<Animal> animals;
    private double currentTime;

    public Simulator(int cols, int rows, int width, int height, Factory<Animal> animalsFactory, Factory<Region> regionsFactory) {
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

    private void set_region(int row, int col, JSONObject r) {
        Region region = regionsFactory.create(r);
        regionManager.add_region(row, col, region);
    }

    public void set_region(int row, int col, JSONObject r) {
        Region region = new Region(r);      // absctract class
        set_region(row, col, region);
    }

    private void add_animal(Animal a) {
        animals.add(a);
        regionManager.registerAnimal(a);
    }

    public void add_animal(JSONObject a_json) {
        Animal animal = animalsFactory.create(a_json);
        add_animal(animal);
    }

    public MapInfo getMapInfo() {
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

        // Remove dead animals
        animals.removeIf(animal -> animal.get_state() == State.Dead);
        regionManager.removeDeadAnimals();

        // Update animals and regions
        for (Animal animal : animals) {
            animal.update(dt);
            regionManager.updateRegion(animal);
        }
        regionManager.updateAllRegions();

        // Check for pregnancy and add babies
        for (Animal animal : animals) {
            if (animal.is_pregnant()) {
                Animal baby = animal.deliver_baby();
                add_animal(baby);
            }
        }
    }

    @Override
    public JSONObject asJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("time", currentTime);
        jsonObject.put("state", regionManager.asJSON());
        return jsonObject;
    }
}

