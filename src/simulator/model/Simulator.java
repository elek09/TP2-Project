package simulator.model;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import simulator.factories.Factory;

public class Simulator implements JSONable {

    private final Factory<Animal> animalsFactory;
    private final Factory<Region> regionsFactory;
    private final RegionManager regionManager;
    private final List<Animal> animals;
    private double currentTime;


    public Simulator(int width, int height, int cols, int rows, Factory<Animal> animalsFactory, Factory<Region> regionsFactory) {
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
        for(int i = 0; i < animals.size(); i++){
            Animal animal = animals.get(i);
            if (animal.get_state() == State.DEAD) {
                animals.remove(animal);
                regionManager.unregister_animal(animal);
            }
            else{
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

