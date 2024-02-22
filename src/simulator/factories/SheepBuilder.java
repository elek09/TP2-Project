package simulator.factories;

import org.json.JSONObject;

/**
 * Builder for the Sheep class object.
 */
public class SheepBuilder extends Builder{
    public SheepBuilder(String type_tag, String desc) {
        super(type_tag, desc);
    }

    @Override
    protected Object create_instance(JSONObject data) throws IllegalArgumentException {
        return null;
    }
}
