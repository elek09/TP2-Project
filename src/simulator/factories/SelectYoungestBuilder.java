package simulator.factories;

import org.json.JSONObject;
import simulator.model.SelectYoungest;

/**
 * Builder for the SelectYoungest class object.
 */
public class SelectYoungestBuilder extends Builder {
    public SelectYoungestBuilder() {
        super("youngest", "Creates a SelectYoungest strategy.");
    }

    @Override
    protected Object create_instance(JSONObject data) throws IllegalArgumentException {
        return new SelectYoungest();
    }
}
