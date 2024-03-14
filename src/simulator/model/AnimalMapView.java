package simulator.model;

import simulator.misc.Vector2D;

import java.lang.annotation.Native;
import java.util.List;
import java.util.function.Predicate;

public interface AnimalMapView extends MapInfo, FoodSupplier {

    List<Animal> get_animals_in_range(Animal a, Predicate<Animal> filter);
    

}
