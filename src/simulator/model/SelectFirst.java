package simulator.model;

import org.json.JSONObject;

import java.util.List;

/**
 * Selects the first animal in the list
 */
public class SelectFirst implements SelectionStrategy {
    @Override
    public Animal select(Animal a, List<Animal> as) {
        if (as.isEmpty())
            return null;

        return as.get(0);
    }

    @Override
    public SelectionStrategy create_instance(JSONObject mateStrategyData) {
        return null;
    }
}
