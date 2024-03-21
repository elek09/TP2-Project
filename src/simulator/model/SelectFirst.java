package simulator.model;

import org.json.JSONObject;

import java.util.List;

public class SelectFirst implements SelectionStrategy{
    @Override
    public Animal select(Animal a, List<Animal> as) {
        if (as.isEmpty())
            return null;

        return as.getFirst();
    }

    @Override
    public SelectionStrategy create_instance(JSONObject mateStrategyData) {
        return null;
    }
}
