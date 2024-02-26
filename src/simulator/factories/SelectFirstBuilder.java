package simulator.factories;

import org.json.JSONObject;
/**
 * Builder for the SelectFirstBuilder class object.
 */
public class SelectFirstBuilder extends Builder{
    //i am not sure what is needed, we shall discuss it
    public SelectFirstBuilder(String type_tag, String desc) {
        super(type_tag, desc);
        //super("first", "");
    }

    @Override
    protected Object create_instance(JSONObject data) throws IllegalArgumentException {
        return null;
    }


}
