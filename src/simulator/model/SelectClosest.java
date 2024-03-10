package simulator.model;

import org.json.JSONObject;

import java.util.List;

public class SelectClosest implements SelectionStrategy{
    @Override
    public Animal select(Animal a, List<Animal> as) {
        if(!as.isEmpty()){
            Animal closest = as.getFirst();
            for (Animal animal : as) {
                if (a.get_position().distanceTo(animal.get_position()) < a.get_position().distanceTo(closest.get_position())) {
                    closest = animal;
                }
            }
            return closest;
        }
        return null;
    }

    @Override
    public SelectionStrategy create_instance(JSONObject mateStrategyData) {
        return null;
    }
}
