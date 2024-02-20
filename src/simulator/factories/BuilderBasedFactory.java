package simulator.factories;

import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BuilderBasedFactory<T> implements Factory<T> {
    private Map<String, Builder<T>> _builders;
    private List<JSONObject> _builders_info;

    public BuilderBasedFactory() {
        for (Builder<T> b : _builders) {
            _builders.put(b.get_type_tag(), b);
            LinkedList<JSONObject> info = new LinkedList<>();
        }

    }
    public BuilderBasedFactory(List<Builder<T>> builders) {
        this();
        for (Builder<T> b : builders) {
            
        }
    }
    public void add_builder(Builder<T> b) {
        // add an entry "b.getTag() |-> b" to _builders. 
        // ...
        // add b.get_info() to _buildersInfo
        // ...º
    }

    @Override
    public T create_instance(JSONObject info) {
        return null;
    }

    @Override
    public List<JSONObject> get_info() {
        return null;
    }
}
