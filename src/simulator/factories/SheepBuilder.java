package simulator.factories;

import org.json.JSONObject;
import simulator.misc.Vector2D;
import simulator.model.*;

/**
 * Builder for the Sheep class object.
 */
public class SheepBuilder extends Builder<Animal> {
    private final SelectionStrategy _strategy;
    private SelectionStrategy mateStrategy;
    private SelectionStrategy dangerStrategy;

    public SheepBuilder(SelectionStrategy strategy) {
        super("sheep", "Creates a Sheep instance.");
        _strategy = strategy;
    }

    /**
     * Creates a new Sheep instance.
     *
     * @param data JSON object containing data for the instance creation
     * @return a new Sheep instance
     * @throws IllegalArgumentException if the data is invalid
     */
    @Override
    protected Animal create_instance(JSONObject data) throws IllegalArgumentException {
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

    /**
     * Fills in additional data into the provided JSON object.
     *
     * @param o JSON object to fill in data
     */
    @Override
    protected void fill_in_data(JSONObject o) {
        JSONObject mateStrategyData = o.optJSONObject("mate_strategy");
        mateStrategy = mateStrategyData != null ? _strategy.create_instance(mateStrategyData) : new SelectFirst();

        JSONObject dangerStrategyData = o.optJSONObject("danger_strategy");
        dangerStrategy = dangerStrategyData != null ? _strategy.create_instance(dangerStrategyData) : new SelectClosest();

    }
}
