package simulator.model;

import simulator.misc.Vector2D;

import java.util.List;
import java.util.function.Predicate;

public interface AnimalMapView {
    int get_cols();
    int get_rows();
    int get_width();
    int get_height();
    int get_region_width();
    int get_region_height();


    double get_food(Animal a, double dt);

    List<Animal> get_animals_in_range(Animal a, Predicate<Animal> filter);

}
