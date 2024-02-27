package simulator.factories;

import org.json.JSONObject;
import simulator.model.SelectClosest;

/**
 * Builder for the SelectClosest class object.
 */
public class SelectClosestBuilder extends Builder {
    public SelectClosestBuilder() {
        super("closest", "Creates a SelectClosest strategy.");
    }

    @Override
    protected Object create_instance(JSONObject data) throws IllegalArgumentException {
        return new SelectClosest();
    }
}
