package simulator.factories;

import org.json.JSONObject;
import simulator.model.DefaultRegion;

/**
 * Builder for the DefaultRegion class object.
 */
public class DefaultRegionBuilder<T> extends Builder<T> {
    public DefaultRegionBuilder() {
        super("default", "Infinite food supply");
    }

    @Override
    protected T create_instance(JSONObject data) throws IllegalArgumentException {
        return (T) new DefaultRegion();
    }

    @Override
    protected void fill_in_data(JSONObject o) {
    }
}
