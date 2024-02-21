package simulator.factories;

import org.json.JSONObject;

import java.util.List;

public interface Factory<T> {
    public List<JSONObject> get_info();
    public T createInstance(JSONObject info) throws IllegalArgumentException;
}