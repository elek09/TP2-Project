package simulator.model;

import simulator.misc.Utils;

public class DynamicSupplyRegion extends Region implements RegionInfo {
    private double _food;
    private double _growthRate;
    private int n = getHerbivorousSize();

    public DynamicSupplyRegion(double food, double growthRate) {
        this._food = food;
        this._growthRate = growthRate;
    }

    public double get_food(Animal a, double dt) {
        if (a._diet == Diet.CARNIVORE) {
            return 0.0;
        } else {
            double food = Math.min(_food, _multiplicativeFactor * Math.exp(-Math.max(0, n - _substractionNumHerb) * _speedFactorSheep) * dt);
            this._food -= food;
            return this._food;
        }
    }
    public void update(double dt) {
        if (Utils._rand.nextDouble() < 0.5) {
            this._food += this._growthRate * dt;
        }
    }

}
