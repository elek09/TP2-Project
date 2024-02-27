package simulator.model;

import org.json.JSONObject;

import java.util.List;

public interface SelectionStrategy {
    Animal select(Animal a, List<Animal> as);

    SelectionStrategy create_instance(JSONObject mateStrategyData);
}
