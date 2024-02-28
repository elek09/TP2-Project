package simulator.factories;

import org.json.JSONObject;
import simulator.model.DynamicSupplyRegion;

/**
 * Builder for the DynamicSupplyRegion class object.
 */
public class DynamicSupplyRegionBuilder extends Builder{
    public DynamicSupplyRegionBuilder() {
        super("dynamic", "Creates a dynamic supply region with the specified parameters.");
    }

    @Override
    protected Object create_instance(JSONObject data) throws IllegalArgumentException {
        double factor = data.optDouble("factor", 2.0);
        double food = data.optDouble("food", 1000.0);
        return new DynamicSupplyRegion(factor, food);
    }
}
