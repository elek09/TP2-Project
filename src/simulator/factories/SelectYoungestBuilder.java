package simulator.factories;

import org.json.JSONObject;
/**
 * Builder for the SelectYoungest class object.
 */
public class SelectYoungestBuilder extends Builder{
    public SelectYoungestBuilder(String type_tag, String desc) {
        super(type_tag, desc);
    }

    @Override
    protected Object create_instance(JSONObject data) throws IllegalArgumentException {
        return null;
    }
}
