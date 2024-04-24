package simulator.factories;

import org.json.JSONObject;
import simulator.model.DynamicSupplyRegion;

/**
 * Builder for the DynamicSupplyRegion class object.
 */
public class DynamicSupplyRegionBuilder<T> extends Builder<T> {
    public DynamicSupplyRegionBuilder() {
        super("dynamic", "Creates a dynamic supply region with the specified parameters.");
    }

    @Override
    protected T create_instance(JSONObject data) throws IllegalArgumentException {
        fill_in_data(data);
        double factor = data.optDouble("factor", 2.0);
        double food = data.optDouble("food", 1000.0);
        return (T) new DynamicSupplyRegion(factor, food);
    }

    @Override
    protected void fill_in_data(JSONObject o) {
        o.put("factor", "food increase factor (optional, default 2.0)");
        o.put("food", "initial amount of food (optional, default 100.0)");
    }
}
