package simulator.model;

import simulator.misc.Utils;
import simulator.misc.Vector2D;

import java.util.List;
import java.util.function.Predicate;

public class Sheep extends Animal {

    private Animal _danger_source;
    private SelectionStrategy _danger_strategy;

    public Sheep(SelectionStrategy mate_strategy, SelectionStrategy danger_strategy, Vector2D pos) {
        super();
        this._mate_strategy = mate_strategy;
        this._danger_strategy = danger_strategy;
        this._pos = pos;
        this._speed = _speedConst;
        this._sight_range = _sightrangeConst;
    }

    protected Sheep(Sheep p1, Animal p2) {
        super(p1, p2);
        this._danger_strategy = p1._danger_strategy;
        this._danger_source = null;
    }

    public void update(double dt) {
        if (this._state == State.Dead) {
            return;
        }
        else if (this._state == State.Normal) {
            updateAsNormal(dt);
        }
        else if (this._state == State.Danger) {
            updateAsDanger(dt);

        }
        else if (this._state == State.Mate) {
            updateAsMate(dt);
        }
        else if (this._energy==0.0||this._age>8.0){
            this._state = State.Dead;
        }
        else if (this._state!=State.Dead){
            _energy += Animal.get_food(this,dt);     //FoodSupplier Interface
        }
        //TODO: Here must be a check for the sheep to see if it's inside of the board in order to change or not the
        // position of the sheep.
        // if (this._pos.getX() )


    }

    private void updateAsNormal(double dt) {
        if (_pos.distanceTo(_dest) < 8.0) {
            _dest = new Vector2D(Math.random() * 800, Math.random() * 600);
        }
        move(this._speed * dt * Math.exp((this._energy - _maxenergy) * _movefactor));
        this._age += dt;
        this._energy -= dt * _energyreduction;
        assert this._energy > _lowestenergy && this._energy <= _maxenergy;
        this._desire += _desirereduction * dt;
        assert this._desire > _lowestdesire && this._desire <= _maxdesire;
        if (this._danger_source == null) {
            //TODO: Search for a new danger source
            searchForDanger(_region_mngr);

            if (this._desire > _desireUpperBound) {
                this._state = State.Mate;
            }
        } else {
            this._state = State.Danger;
        }
    }

    private void updateAsDanger(double dt) {
        if (this._danger_source != null) {
            if (this._state == State.Dead) {
                this._danger_source = null;
                return;
            } else {
                this._dest = _pos.plus(_pos.minus(_danger_source.get_position()).direction());
                move(_speedFactor * _speed * dt * Math.exp((_energy - _maxenergy) * _multiplicativeMath));
                this._age += dt;
                this._energy -= _energyreduction * _multiplicativeTime * dt;
                assert this._energy > _lowestenergy && this._energy <= _maxenergy;
                this._desire += _desirereduction * dt;
                assert this._desire > _lowestdesire && this._desire <= _maxdesire;

            }

        } else {
            if (this._desire >= _desireUpperBound) {
                this._state = State.Mate;
            } else {
                this._state = State.Normal;
            }
            searchForDanger(_region_mngr);
            updateAsNormal(dt);
        }


    }

    private void updateAsMate(double dt) {
        if (this._mate_target != null) {
            if (this._state == State.Dead || this._sight_range < _pos.distanceTo(_mate_target.get_position())) {
                this._mate_target = null;
                return;
            } else {


            }
        } else if (this._mate_target == null)
            //Searches for a mate and if there is no mate, it will update as normal
            if (!searchForMate(_region_mngr)) {
                updateAsNormal(dt);

            } else {
                this._dest = _mate_target.get_position();
                move(_speedFactor * dt * Math.exp((_energy - _maxenergy) * _multiplicativeMath));
                this._age += dt;
                this._energy -= _energyreduction * _multiplicativeTime * dt;
                assert this._energy > _lowestenergy && this._energy <= _maxenergy;
                this._desire += _desirereduction * dt;
                assert this._desire > _lowestdesire && this._desire <= _maxdesire;
                if (this._pos.distanceTo(_mate_target.get_position()) < 8.0) {
                    this._desire = 0;
                    _mate_target._desire = 0;
                    if (this._baby == null && Math.random() < 0.9) {
                        this._baby = new Sheep(this, _mate_target);
                    }
                    this._state = State.Normal;
                }

            }
            else if (this._danger_source != null){
                this._state = State.Danger;
        }
            else if (this._danger_source == null){
                if (this._desire < _desireUpperBound){
                    this._state = State.Normal;
                }
                searchForDanger(_region_mngr);
            }
    }

    public void searchForDanger(AnimalMapView reg_mngr) {
        for (Animal a : reg_mngr.get_animals_in_range(this, this._sight_range)) {
            if (a.get_diet() == Diet.CARNIVORE) {
                this._danger_source = a;
                break;
            }
            //Finish if we did the region manager class
        }

    }

    public boolean searchForMate(AnimalMapView reg_mngr) {
        for (Animal a : reg_mngr.get_animals_in_range(this, this._sight_range)) {
            if (/*we will have to figure out the corresponding selection strategy*/ a.get_genetic_code() == this._genetic_code) {
                this._mate_target = a;
                return true;
            }
        }
        return false;
    }


    @Override
    public List<Animal> get_animals_in_range(Animal e, Predicate<Animal> filter) {
        return null;
    }

    @Override
    public int get_cols() {
        return 0;
    }

    @Override
    public int get_rows() {
        return 0;
    }

    @Override
    public int get_width() {
        return 0;
    }

    @Override
    public int get_height() {
        return 0;
    }

    @Override
    public int get_region_width() {
        return 0;
    }

    @Override
    public int get_region_height() {
        return 0;
    }

    @Override
    public Vector2D adjust_position(Vector2D pos) {
        return null;
    }
}