package simulator.model;

import simulator.misc.Vector2D;

public interface AnimalInfo extends JSONable{
    State get_state();
    Vector2D get_position();
    String get_genetic_code();
    Diet get_diet();
    double get_speed();
    double get_sight_range();
    double get_energy();
    double get_age();
    Vector2D get_destination();
    boolean is_pregnant();
    SelectionStrategy get_mate_strategy();

}
