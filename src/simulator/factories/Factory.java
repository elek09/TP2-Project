package simulator.factories;

import org.json.JSONObject;

import java.util.List;

public interface Factory<T> {
    T createInstance(JSONObject info) throws IllegalArgumentException;
    List<JSONObject> getInfo();

    T create(JSONObject r);
}