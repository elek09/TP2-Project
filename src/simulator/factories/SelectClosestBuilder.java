package simulator.factories;

import org.json.JSONObject;
/**
 * Builder for the SelectClosest class object.
 */
public class SelectClosestBuilder extends Builder {
    public SelectClosestBuilder(String type_tag, String desc) {
        super(type_tag, desc);
    }

    @Override
    protected Object create_instance(JSONObject data) throws IllegalArgumentException {
        return null;
    }
}
