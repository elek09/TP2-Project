package simulator.model;

import org.json.JSONObject;

import java.util.List;

public class SelectYoungest implements SelectionStrategy{
    @Override
    public Animal select(Animal a, List<Animal> as) {
        Animal youngest = as.getFirst();
        for (Animal animal : as) {
            if (animal.get_age() < youngest.get_age()) {
                youngest = animal;
            }
        }
        return youngest;
    }

    @Override
    public SelectionStrategy create_instance(JSONObject mateStrategyData) {
        return null;
    }
}
