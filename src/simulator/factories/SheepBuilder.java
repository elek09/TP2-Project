package simulator.factories;

import org.json.JSONObject;
import simulator.misc.Vector2D;
import simulator.model.*;

/**
 * Builder for the Sheep class object.
 */
public class SheepBuilder extends Builder{
    private SelectionStrategy _strategy;
    private SelectionStrategy mateStrategy;
    private SelectionStrategy dangerStrategy;
    public SheepBuilder(SelectionStrategy strategy) {
        super("sheep", "Creates a Sheep instance.");
        _strategy = strategy;
    }

    @Override
    protected Animal create_instance(JSONObject data) throws IllegalArgumentException{
        fill_in_data(data);
        JSONObject posData = data.optJSONObject("pos");
        Vector2D pos = posData != null ? new Vector2D(
                Vector2D.get_random_vector(
                        posData.getJSONArray("x_range").getDouble(0),
                        posData.getJSONArray("x_range").getDouble(1),
                        posData.getJSONArray("y_range").getDouble(0),
                        posData.getJSONArray("y_range").getDouble(1))) : null;

        return new Sheep(mateStrategy, dangerStrategy, pos);
    }
    @Override
    protected void fill_in_data(JSONObject o) {
        JSONObject mateStrategyData = o.optJSONObject("mate_strategy");
        mateStrategy = mateStrategyData != null ? _strategy.create_instance(mateStrategyData) : new SelectFirst();

        JSONObject dangerStrategyData = o.optJSONObject("danger_strategy");
        dangerStrategy = dangerStrategyData != null ? _strategy.create_instance(dangerStrategyData) : new SelectClosest();

    }
}
