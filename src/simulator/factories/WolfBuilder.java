package simulator.factories;

import org.json.JSONObject;
import simulator.misc.Vector2D;
import simulator.model.*;

/**
 * Builder for the Wolf class objects.
 */
public class WolfBuilder extends Builder{
    private SelectionStrategy _strategy;
    private SelectionStrategy mateStrategy;
    private SelectionStrategy huntStrategy;
    public WolfBuilder(SelectionStrategy strategy) {
        super("wolf", "Creates a wolf with the specified position and strategies.");
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

        return new Wolf(mateStrategy, huntStrategy, pos);
    }
    @Override
    protected void fill_in_data(JSONObject o) {
        JSONObject mateStrategyData = o.optJSONObject("mate_strategy");
        mateStrategy = mateStrategyData != null ? _strategy.create_instance(mateStrategyData) : new SelectFirst();

        JSONObject dangerStrategyData = o.optJSONObject("danger_strategy");
        huntStrategy = dangerStrategyData != null ? _strategy.create_instance(dangerStrategyData) : new SelectClosest();

    }
}
