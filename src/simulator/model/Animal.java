package simulator.model;

import org.json.JSONObject;
import simulator.misc.Utils;
import simulator.misc.Vector2D;

import java.util.List;
import java.util.function.Predicate;

public abstract class Animal implements Entity, AnimalInfo, Constants, AnimalMapView, FoodSupplier{
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
    protected FoodSupplier _food_mngr;
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
        _state = State.NORMAL;
        _pos = pos;
        _dest = null;
        _energy = _maxenergy;
        _speed = Utils.get_randomized_parameter(init_speed, 0.1);
        _age = 0;
        _desire = _lowestdesire ;
        _sight_range = sight_range;
        _mate_target = null;
        _baby = null;
        _region_mngr = null;
        _mate_strategy = mate_strategy;
    }
    protected Animal(Animal p1, Animal p2){
        _genetic_code = p1._genetic_code;
        _diet = p1._diet;
        _state = State.NORMAL;
        _pos = p1.get_position().plus( Vector2D.get_random_vector(-1,1).scale(_multiplicativeFactor * (Utils._rand.nextGaussian()+1)));
        _dest = null;
        _energy = (p1._energy + p2._energy) / 2.0;
        _speed = Utils.get_randomized_parameter((p1.get_speed() + p2.get_speed())/2, _tolerance);
        _age = 0;
        _desire = _lowestdesire;
        _sight_range = Utils.get_randomized_parameter( (p1.get_sight_range() + p2.get_sight_range())/2, _tolerance );
        _mate_target = null;
        _baby = null;
        _region_mngr = null;
        _mate_strategy = p2._mate_strategy;
    }

    public Animal() {
    }

    void init(AnimalMapView reg_mngr){
        _region_mngr = reg_mngr;
        this._dest = Vector2D.get_random_vector(0, _region_mngr.get_width()-1, 0, _region_mngr.get_height()-1);
        if (this._pos == null){
            _pos = Vector2D.get_random_vector(0, _region_mngr.get_width()-1, 0, _region_mngr.get_height()-1);
        }
        else{
            if(!IsOnTheMap(_pos)) _pos = _region_mngr.adjust_position(_pos);
        }
    }

    protected boolean IsOnTheMap(Vector2D pos){
        return !(this._pos.getX() < 0 || this._pos.getX() > _region_mngr.get_width() || this._pos.getY() < 0 || this._pos.getY() > _region_mngr.get_height());
    }

    public Animal deliver_baby(){
        if (_baby != null){
            Animal baby = _baby;
            _baby = null;
            return baby;
        }
        return null;
    }

    protected void move(double speed){
        _pos = _pos.plus(_dest.minus(_pos).direction().scale(speed));


    }
    public JSONObject as_JSON(){
        JSONObject json = new JSONObject();
        json.put("pos", this._pos.toString());
        json.put("gcode", this._genetic_code);
        json.put("diet", this._diet.toString());
        json.put("state", this._state.toString());
        return json;
    }

    public void update(double dt) {
    }
    @Override
    public State get_state() {
        return this._state;
    }

    @Override
    public Vector2D get_position() {
        return this._pos;
    }

    @Override
    public String get_genetic_code() {
        return this._genetic_code;
    }

    @Override
    public Diet get_diet() {
        return this._diet;
    }

    @Override
    public double get_speed() {
        return this._speed;
    }

    @Override
    public double get_sight_range() {
        return this._sight_range;
    }

    @Override
    public double get_energy() {
        return this._energy;
    }

    @Override
    public double get_age() {
        return this._age;
    }

    @Override
    public Vector2D get_destination() {
        return this._dest;
    }

    @Override
    public boolean is_pregnant() {
        return this._baby != null;
    }


    public Animal searchForMate(AnimalMapView reg_mngr, SelectionStrategy strategy) {
        Predicate<Animal> filter = a -> a.get_genetic_code() == this._genetic_code && !a.is_pregnant() && a.get_state() == State.MATE && a != this;

        List<Animal> animalsInRange = reg_mngr.get_animals_in_range(this, filter);

        return strategy.select(this, animalsInRange);
    }

    public Animal searchForDanger(AnimalMapView reg_mngr, SelectionStrategy strategy) {

        Predicate<Animal> filter = a -> a.get_diet() == Diet.CARNIVORE;

        List<Animal> animalsInRange = reg_mngr.get_animals_in_range(this, filter);


        return strategy.select(this, animalsInRange);
    }

    public Animal searchForHuntTarget(AnimalMapView reg_mngr, SelectionStrategy strategy) {
        Predicate<Animal> filter = a -> a.get_diet() == Diet.HERBIVORE;

        List<Animal> animalsInRange = reg_mngr.get_animals_in_range(this, filter);


        return strategy.select(this, animalsInRange);
    }
}
