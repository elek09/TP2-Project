package simulator.factories;

import org.json.JSONObject;
import simulator.model.DefaultRegion;

/**
 * Builder for the DefaultRegion class object.
 */
public class DefaultRegionBuilder extends Builder {
    public DefaultRegionBuilder() {
        super("default", "Infinite food supply");
    }

    @Override
    protected Object create_instance(JSONObject data) throws IllegalArgumentException {
        return new DefaultRegion();
    }

    @Override
    protected void fill_in_data(JSONObject o) {
    }
}
