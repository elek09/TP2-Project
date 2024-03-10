package simulator.factories;

import org.json.JSONObject;
import simulator.model.DefaultRegion;

/**
 * Builder for the DefaultRegion class object.
 */
public class DefaultRegionBuilder extends Builder {
    public DefaultRegionBuilder() {
        super("default", "Creates a default region with the specified food capacity.");
    }

    @Override
    protected Object create_instance(JSONObject data) throws IllegalArgumentException {
        return new DefaultRegion();
    }
}
