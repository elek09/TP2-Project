package simulator.model;

import org.json.JSONObject;
import simulator.misc.Utils;
import simulator.misc.Vector2D;

import java.util.List;
import java.util.Vector;
import java.util.function.Predicate;

public abstract class Animal implements Entity, AnimalInfo, Constants{
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
        // Check for null or empty genetic_code, sight_range and init_speed are positive, and mate_strategy is not null
        if (genetic_code == null || genetic_code.isEmpty() || sight_range <= 0 || init_speed <= 0 || mate_strategy == null) {
            throw new IllegalArgumentException("Invalid parameters");
        }

        // Assign values to attributes
        _genetic_code = genetic_code;
        _diet = diet;
        _state = State.NORMAL;
        _pos = pos;
        _dest = null;
        _energy = _maxenergy;
        _speed = Utils.get_randomized_parameter(init_speed, _toleranceSpeed);
        _age = 0;
        _desire = _lowestdesire ;
        _sight_range = sight_range;
        _mate_target = null;
        _baby = null;
        _region_mngr = null;
        _mate_strategy = mate_strategy;
    }
    protected Animal(Animal p1, Animal p2){
        _genetic_code = p1.get_genetic_code();
        _diet = p1.get_diet();
        _state = State.NORMAL;
        _pos = p1.get_position().plus( Vector2D.get_random_vector(-1,1).scale(_multiplicativeFactor * (Utils._rand.nextGaussian()+1)));
        _dest = null;
        _energy = (p1.get_energy() + p2.get_energy()) / 2.0;
        _speed = Utils.get_randomized_parameter((p1.get_speed() + p2.get_speed())/2, _tolerance);
        _age = 0;
        _desire = _lowestdesire;
        _sight_range = Utils.get_randomized_parameter( (p1.get_sight_range() + p2.get_sight_range())/2, _tolerance );
        _mate_target = null;
        _baby = null;
        _region_mngr = null;
        _mate_strategy = p2.get_mate_strategy();
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
            if(IsOutOfMap()) {
                _pos = adjust_position(_pos);
            }
        }
    }

    public Vector2D adjust_position (Vector2D pos){
        double cols = pos.getX();
        double rows = pos.getY();

        double width = _region_mngr.get_width();
        double height = _region_mngr.get_height();

        while (cols >= width)
            cols = (cols - width);

        while (cols < 0)
            cols = (cols + width);

        while (rows >= height)
            rows = (rows - height);

        while (rows < 0)
            rows = (rows + height);

        return new Vector2D(cols, rows);
    }
    protected boolean IsOutOfMap(){
        return this._pos.getX() < 0 || this._pos.getX() > _region_mngr.get_width() || this._pos.getY() < 0 || this._pos.getY() > _region_mngr.get_height();
    }

    public Animal deliver_baby(){
        if (this.is_pregnant()){
            Animal baby = _baby;
            set_baby(null);
            return baby;
        }
        return null;
    }
    public void set_baby(Animal b){
        this._baby = b;
    }
    protected void move(double speed){
        Vector2D destination = get_destination();
        _pos = _pos.plus(destination.minus(_pos).direction().scale(speed));
    }
    public JSONObject as_JSON(){
        JSONObject json = new JSONObject();
        Vector2D pos = this.get_position();

        json.put("pos", pos.toString());
        json.put("gcode", get_genetic_code());
        json.put("diet", get_diet().toString());
        json.put("state", get_diet().toString());

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
    @Override
    public SelectionStrategy get_mate_strategy(){
        return this._mate_strategy;
    }


    public Animal searchForMate(AnimalMapView reg_mngr, SelectionStrategy strategy) {
        Predicate<Animal> filter = a -> a.get_genetic_code().equals(this._genetic_code) && !a.is_pregnant() && a.get_state() == State.MATE && a != this;

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
    protected void checkEnergy(){
        if (_energy > _maxenergy) {
            _energy = _maxenergy;
        } else if (_energy < _lowestenergy) {
            _energy = _lowestenergy;
        }
    }

    protected void checkDesire(){
        if (_desire > _maxdesire) {
            _desire = _maxdesire;
        } else if (_desire < _lowestdesire) {
            _desire = _lowestdesire;
        }
    }
    protected void setDesire(double d){
        this._desire = d;
    }
}
