package simulator.factories;

import org.json.JSONObject;
/**
 * Builder for the DynamicSupplyRegion class object.
 */
public class DynamicSupplyRegionBuilder extends Builder{
    public DynamicSupplyRegionBuilder(String type_tag, String desc) {
        super(type_tag, desc);
    }

    @Override
    protected Object create_instance(JSONObject data) throws IllegalArgumentException {
        return null;
    }
}
