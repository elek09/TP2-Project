package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;
import simulator.model.MapInfo;
import simulator.model.Simulator;
import simulator.view.SimpleObjectViewer;
import simulator.view.SimpleObjectViewer.ObjInfo;

import java.io.OutputStream;

public class Controller {

    private final Simulator _sim;

    public Controller(Simulator sim) {
        this._sim = sim;
    }

    //seems okay
    public void load_data(JSONObject data) {
        JSONArray regions = data.optJSONArray("regions");
        if (regions != null) {
            for (int i = 0; i < regions.length(); i++) {
                JSONObject regionSpec = regions.getJSONObject(i);
                JSONArray rowRange = regionSpec.getJSONArray("row");
                JSONArray colRange = regionSpec.getJSONArray("col");
                JSONObject spec = regionSpec.getJSONObject("spec");
                int rf = rowRange.getInt(0);
                int rt = rowRange.getInt(1);
                int cf = colRange.getInt(0);
                int ct = colRange.getInt(1);
                for (int row = rf; row <= rt; row++) {
                    for (int col = cf; col <= ct; col++) {
                        _sim.set_region(row, col, spec);
                    }
                }
            }
        }

        JSONArray animals = data.getJSONArray("animals");
        for (int i = 0; i < animals.length(); i++) {
            JSONObject animalSpec = animals.getJSONObject(i);
            int amount = animalSpec.getInt("amount");
            JSONObject spec = animalSpec.getJSONObject("spec");
            for (int j = 0; j < amount; j++) {
                _sim.add_animal(spec);
            }
        }
    }

    public void run(double t, double dt, boolean sv, OutputStream out) {
        JSONObject init_state = _sim.as_JSON();
        JSONObject final_state;
        SimpleObjectViewer view = null;
        if (sv) {
            MapInfo m = (MapInfo) _sim.get_map_info();
            view = new SimpleObjectViewer("ECOSYSTEM", m.get_width(), m.get_height(), m.get_cols(), m.get_rows());
            view.update(to_animals_info(_sim.getAnimals()), _sim.get_time(), dt);

        }


        while (_sim.get_time() < t) {
            _sim.advance(dt);
            if (sv) {
                view.update(to_animals_info(_sim.getAnimals()), _sim.get_time(), dt);
            }
        }
        final_state = _sim.as_JSON();
        JSONObject output = new JSONObject();
        output.put("in", init_state);
        output.put("out", final_state);

        // Write output to OutputStream
        if (sv){
            view.close();
        }
    }
}

