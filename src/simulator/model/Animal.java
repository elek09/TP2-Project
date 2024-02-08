package simulator.model;

import simulator.misc.Utils;
import simulator.misc.Vector2D;

public abstract class Animal implements Entity, AnimalInfo{
    protected String _genetic_code;
    protected Diet _diet;
    protected State _state;
    protected Vector2D _pos;
    protected Vector2D _dest;
    protected double _energy;
    protected double _speed;
    protected double _age;
    protected double _desire;
    protected double _sight_range;
    protected Animal _mate_target;
    protected Animal _baby;
    protected AnimalMapView _region_mngr;
    protected SelectionStrategy _mate_strategy;

    protected Animal(String genetic_code, Diet diet, double sight_range, double init_speed, SelectionStrategy mate_strategy, Vector2D pos) {
        // Check for null or empty genetic_code
        if (genetic_code == null || genetic_code.isEmpty()) {
            throw new IllegalArgumentException("Genetic code cannot be null or empty");
        }

        // Check for positive sight_range and init_speed
        if (sight_range <= 0 || init_speed <= 0) {
            throw new IllegalArgumentException("Sight range and initial speed must be positive");
        }

        // Check mate_strategy for null
        if (mate_strategy == null) {
            throw new IllegalArgumentException("Mate strategy cannot be null");
        }

        // Assign values to attributes
        _genetic_code = genetic_code;
        _diet = diet;
        _state = State.Normal;

        _pos = pos;

        _dest = null;
        _energy = 100.0;
        _speed = Utils.get_randomized_parameter(init_speed, 0.1);
        _age = 0;
        _desire = 0.0;
        _sight_range = sight_range;
        _mate_target = null;
        _baby = null;
        _region_mngr = null;
        _mate_strategy = mate_strategy;
    }
    protected Animal(Animal p1, Animal p2){

        _genetic_code = p1._genetic_code;
        _diet = p1._diet;
        _state = State.Normal;
        _pos = p1.get_position().plus( Vector2D.get_random_vector(-1,1).scale(60.0 * (Utils._rand.nextGaussian()+1)));
        _dest = null;
        _energy = (p1._energy + p2._energy) / 2.0;
        _speed = Utils.get_randomized_parameter((p1.get_speed() + p2.get_speed())/2, 0.2);
        _age = 0;
        _desire = 0.0;
        _sight_range = Utils.get_randomized_parameter( (p1.get_sight_range() + p2.get_sight_range())/2, 0.2 );
        _mate_target = null;
        _baby = null;
        _region_mngr = null;
        _mate_strategy = p2._mate_strategy;

    }

    void init(AnimalMapView reg_mngr){
        _region_mngr = reg_mngr;
        if (this._pos == null){
            //_pos = _region_mngr.get_random_position();
        }
    }




}
