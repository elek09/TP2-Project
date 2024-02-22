package simulator.factories;

import org.json.JSONObject;
/**
 * Builder for the SelectFirstBuilder class object.
 */
public class SelectFirstBuilder extends Builder{
    public SelectFirstBuilder(String type_tag, String desc) {
        super(type_tag, desc);
    }

    @Override
    protected Object create_instance(JSONObject data) throws IllegalArgumentException {
        return null;
    }


}
