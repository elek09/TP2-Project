package simulator.model;

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
        if (this._state == State.DEAD) {
            return;
        }
        else if (this._state == State.NORMAL) {
            updateAsNormal(dt);
        }
        else if (this._state == State.DANGER) {
            updateAsDanger(dt);
        }
        else if (this._state == State.MATE) {
            updateAsMate(dt);
        }
        else if (this._energy==0.0||this._age>8.0){
            this._state = State.DEAD;
        }
        else if (this._state != State.DEAD){
            _energy += this.getFood(this,dt);     //FoodSupplier Interface      we will need to look into this bc the adding function in getFood seems a bit off now
        }

        //TODO: Here must be a check for the sheep to see if it's inside of the board in order to change or not the
        // position of the sheep.
        // if (this._pos.getX() )
    }

    //Checked
    private void updateAsNormal(double dt) {
        if (_pos.distanceTo(_dest) < 8.0) {
            _dest = new Vector2D(Math.random() * 800, Math.random() * 600);
        }
        move(this._speed * dt * Math.exp((this._energy - _maxenergy) * _movefactor));
        this._age += dt;
        this._energy -= dt * _energyreductionSheep;
        assert this._energy > _lowestenergy && this._energy <= _maxenergy;
        this._desire += _desirereductionSheep * dt;
        assert this._desire > _lowestdesire && this._desire <= _maxdesire;
        if (this._danger_source == null) {
            //TODO: Search for a new danger source
            searchForDanger(_region_mngr);

            if (this._desire > _desireUpperBound) {
                this._state = State.MATE;
            }
        } else {
            this._state = State.DANGER;
        }
    }

    //Its seems good now
    private void updateAsDanger(double dt) {
        if (this._danger_source != null) {
            if (this._state == State.DEAD) {
                this._danger_source = null;
                return;
            } else {
                this._dest = _pos.plus(_pos.minus(_danger_source.get_position()).direction());
                move(_speedFactorSheep * _speed * dt * Math.exp((_energy - _maxenergy) * _multiplicativeMath));
                this._age += dt;

                this._energy -= _energyreductionSheep * _multiplicativeTime * dt;
                assert this._energy > _lowestenergy && this._energy <= _maxenergy;

                this._desire += _desirereductionSheep * dt;
                assert this._desire > _lowestdesire && this._desire <= _maxdesire;
            }
        } else {
            if (this._desire >= _desireUpperBound) {
                this._state = State.MATE;
            } else {
                this._state = State.NORMAL;
            }
            searchForDanger(_region_mngr);
            updateAsNormal(dt);
        }


    }

    //Its seems good now
    private void updateAsMate(double dt) {
        if (this._mate_target != null) {
            if (this._state == State.DEAD || this._sight_range < _pos.distanceTo(_mate_target.get_position())) {
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
                move(_speedFactorSheep * dt * Math.exp((_energy - _maxenergy) * _multiplicativeMath));
                this._age += dt;

                this._energy -= _energyreductionSheep * _multiplicativeTime * dt;
                assert this._energy > _lowestenergy && this._energy <= _maxenergy;

                this._desire += _desirereductionSheep * dt;
                assert this._desire > _lowestdesire && this._desire <= _maxdesire;

                if (this._pos.distanceTo(_mate_target.get_position()) < 8.0) {
                    this._desire = 0;
                    _mate_target._desire = 0;
                    if (this._baby == null && Math.random() < 0.9) {
                        this._baby = new Sheep(this, _mate_target);
                    }
                    this._state = State.NORMAL;
                }

            }
            else if (this._danger_source != null){
                this._state = State.DANGER;
        }
            else if (this._danger_source == null){
                searchForDanger(_region_mngr);
            }
            else if (this._danger_source != null){
                if (this._desire < _desireUpperBound){
                    this._state = State.NORMAL;
                }
                else {
                    this._state = State.DANGER;
                }
            }
    }

    public void searchForDanger(AnimalMapView reg_mngr) {

        /*for (Animal a : reg_mngr.get_animals_in_range(this, this._sight_range)) {
            if (a.get_diet() == Diet.CARNIVORE) {
                this._state = State.DANGER;
                break;
            }
        }*/
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
    public List<Animal> get_animals_in_range(Animal a, Predicate<Animal> filter) {
        return null;
    }

    @Override
    public Vector2D adjust_position(Vector2D pos) {
        return null;
    }

    @Override
    public double getFood(Animal a, double dt) {
        return 0;
    }
}