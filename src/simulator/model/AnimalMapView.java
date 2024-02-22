package simulator.model;

import simulator.misc.Vector2D;

import java.util.List;

public interface AnimalMapView {
    public List<Animal> get_animals_in_range(Animal e, double filter);
    public int get_cols();
    public int get_rows();
    public int get_width();
    public int get_height();
    public int get_region_width();
    public int get_region_height();
    public Vector2D adjust_position(Vector2D pos);

}
