package simulator.model;

public class DefaultRegion extends Region implements RegionInfo{
    public double get_food(Animal a, double dt){
        int n = animals.getHerbivorousSize();


        if (a._diet == Diet.CARNIVORE){
            return 0.0;
        }
        else {
            return 60.0*Math.exp(-Math.max(0,n - 5.0)*2.0)*dt;


        }



    }
}
