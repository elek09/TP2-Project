package simulator.factories;

import org.json.JSONObject;
import simulator.misc.Vector2D;
import simulator.model.*;

/**
 * Builder for the Wolf class objects.
 */
public class WolfBuilder extends Builder{
    private SelectionStrategy _strategy;
    public WolfBuilder(SelectionStrategy strategy) {
        super("wolf", "Creates a wolf with the specified position and strategies.");
        _strategy = strategy;
    }
    @Override
    protected Animal create_instance(JSONObject data) throws IllegalArgumentException{
        JSONObject mateStrategyData = data.optJSONObject("mate_strategy");
        SelectionStrategy mateStrategy = mateStrategyData != null ? _strategy.create_instance(mateStrategyData) : new SelectFirst();

        JSONObject huntStrategyData = data.optJSONObject("hunt_strategy");
        SelectionStrategy huntStrategy = huntStrategyData != null ? _strategy.create_instance(huntStrategyData) : new SelectFirst();

        JSONObject posData = data.optJSONObject("pos");
        Vector2D pos = posData != null ? new Vector2D(posData.getJSONArray("x_range").getDouble(0), posData.getJSONArray("y_range").getDouble(0)) : null;

        /*private Vector2D parsePosition(JSONObject data) {
            if (data.has("pos")) {
                JSONObject posData = data.getJSONObject("pos");
                double minX = posData.getJSONArray("x_range").getDouble(0);
                double maxX = posData.getJSONArray("x_range").getDouble(1);
                double minY = posData.getJSONArray("y_range").getDouble(0);
                double maxY = posData.getJSONArray("y_range").getDouble(1);
                double randomX = minX + Math.random() * (maxX - minX);
                double randomY = minY + Math.random() * (maxY - minY);
                return new Vector2D(randomX, randomY);
            } else {
                return null;
            }
        }*/

        return new Wolf(mateStrategy, huntStrategy, pos);
    }
}
