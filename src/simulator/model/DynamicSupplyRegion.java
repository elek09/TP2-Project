package simulator.model;

public class DynamicSupplyRegion extends Region implements RegionInfo {
    private double _food;
    private double _grothRate;
    private int n = getHerbivorousSize();

    public DynamicSupplyRegion(double food, double growthRate) {
        this._food = food;
        this._grothRate = growthRate;
    }

public double get_food(Animal a, double dt) {
        if (a._diet == Diet.CARNIVORE) {
            return 0.0;
        } else {
            double food = this._food;
            this._food = Math.min(_food,60.0*Math.exp(-Math.max(0,n-_substractionNumHerb)*2.0)*dt);
            return food;
        }
    }

}
