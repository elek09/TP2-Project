package simulator.factories;

import org.json.JSONObject;
import simulator.model.SelectFirst;

/**
 * Builder for the SelectFirstBuilder class object.
 */
public class SelectFirstBuilder extends Builder {
    public SelectFirstBuilder() {
        super("first", "Creates a SelectFirst strategy.");
    }

    @Override
    protected Object create_instance(JSONObject data) throws IllegalArgumentException {
        return new SelectFirst();
    }


}
