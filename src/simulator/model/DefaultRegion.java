package simulator.model;

public class DefaultRegion extends Region implements RegionInfo{

    public double get_food(Animal a, double dt){
        int n = this.getHerbivores();
        if (a._diet == Diet.CARNIVORE){
            return 0.0;
        }
        else {
            return _multiplicativeFactor*Math.exp(-Math.max(0,n - _substractionNumHerb)* _speedFactorSheep)*dt;
        }
    }

    @Override
    public void update(double dt) {

    }
}
