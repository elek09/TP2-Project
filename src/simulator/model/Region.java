package simulator.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class Region implements Entity, FoodSupplier, RegionInfo, Constants {
    protected List<Animal> animals;

    public Region() {
        animals = new ArrayList<>();
    }

    @Override
    public void update(double dt) {
        for (int i = 0; i < animals.size(); i++) {
            animals.get(i).update(dt);
        }
    }
    @Override
    public double get_food(Animal a, double dt) {
        return 0;
    }

    public final void add_animal(Animal a) {
        animals.add(a);
    }
    public void remove_animal(Animal a) {
        animals.remove(a);
    }
    public final List<Animal> getAnimals() {
        return animals;
    }

    public JSONObject as_JSON(){
        JSONObject ObjectAnimal = new JSONObject();
        JSONArray ArrayAnimals = new JSONArray();
        for (int i = 0; i < animals.size(); i++) {
            ArrayAnimals.put(animals.get(i).as_JSON());
        }
        ObjectAnimal.put("animals", ArrayAnimals);
        return ObjectAnimal;
    }

    public int getHerbivorousSize() {
        int count = 0;

        for (int i = 0; i < animals.size(); i++) {
            if (animals.get(i).get_diet() == Diet.HERBIVORE) {
                count++;
            }
        }
        return count;
    }
}