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

    }

    @Override
    public double getFood(Animal a, double dt) {
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
        for (Animal a : animals) {
            ArrayAnimals.put(a.as_JSON());
        }
        ObjectAnimal.put("animals", ArrayAnimals);
        return ObjectAnimal;
    }

    public int getHerbivorousSize() {
        int count = 0;
        for (Animal a : animals) {
            if (a.get_diet() == Diet.HERBIVORE) {
                count++;
            }
        }
        return count;
    }



}
