package simulator.factories;

import org.json.JSONObject;

/**
 * Builder for the Wolf class objects.
 */
public class WolfBuilder extends Builder{
    public WolfBuilder(String type_tag, String desc) {
        super(type_tag, desc);
    }

    @Override
    protected Object create_instance(JSONObject data) throws IllegalArgumentException {
        return null;
    }
}
