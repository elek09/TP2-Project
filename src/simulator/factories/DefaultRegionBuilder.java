package simulator.factories;

import org.json.JSONObject;
/**
 * Builder for the DefaultRegion class object.
 */
public class DefaultRegionBuilder extends Builder{
    public DefaultRegionBuilder(String type_tag, String desc) {
        super(type_tag, desc);
    }

    @Override
    protected Object create_instance(JSONObject data) throws IllegalArgumentException {
        return null;
    }
}
