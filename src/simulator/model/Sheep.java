package simulator.model;

import simulator.misc.Vector2D;

import java.util.List;
import java.util.function.Predicate;

public class Sheep extends Animal {

    private Animal _danger_source;
    private SelectionStrategy _danger_strategy;


    public Sheep(SelectionStrategy mate_strategy, SelectionStrategy danger_strategy, Vector2D pos) {
        super("Sheep", Diet.HERBIVORE, _sightrangeConst, _speedConst, mate_strategy, pos);
        this._mate_strategy = mate_strategy;
        this._danger_strategy = danger_strategy;
        this._danger_source = null;
    }

    protected Sheep(Sheep p1, Animal p2) {
        super(p1, p2);
        this._danger_strategy = p1.get_danger_strategy();
        this._danger_source = null;
    }
    public SelectionStrategy get_danger_strategy() {
        return this._danger_strategy;
    }
    @Override
    public void update(double dt) {
        switch(_state){
            case NORMAL:
                updateAsNormal(dt);
                break;
            case DANGER:
                updateAsDanger(dt);
                break;
            case MATE:
                updateAsMate(dt);
                break;
            case DEAD:
                break;
        }
        if (IsOutOfMap()) {
            _pos =  adjust_position(_pos);
            this._state = State.NORMAL;
        }

        if (_energy <= _lowestenergy || _age > _ageLimit){
            _state = State.DEAD;
        }

        State state = this.get_state();
        if (state != State.DEAD){
            _energy += this._region_mngr.get_food(this,dt);
            if (_energy > _maxenergy) {
                _energy = _maxenergy;
            }
            else if (_energy < 0) {
                _energy = 0;
            }
        }

    }


    private void updateAsNormal(double dt) {
        if (_pos.distanceTo(_dest) < 8.0) {
            _dest = new Vector2D(Math.random() * _region_mngr.get_width(), Math.random() * _region_mngr.get_height());
        }
        move(_speed * dt * Math.exp((this._energy - _maxenergy) * _movefactor));
        _age += dt;

        //IMPORTANT
        if (_energy > _maxenergy) {
            _energy = _maxenergy;
        } else if (_energy < 0) {
            _energy = 0;
        }
        else {
            _energy -= dt * _energyreductionSheep;
        }

        if(_desire > _maxdesire){
            _desire = _maxdesire;
        }
        else if(_desire < _lowestdesire){
            _desire = _lowestdesire;
        }
        else {
            _desire += _desirereductionSheep * dt;
        }

        if (this._danger_source == null) {
            _danger_source=searchForDanger(_region_mngr, this._danger_strategy);
        }

        if (this._danger_source != null) {
            this._state = State.DANGER;
        }
        else if (_danger_source == null && _desire > 65.0) {
            this._state = State.MATE;
        }
    }


    private void updateAsDanger(double dt) {
        if (_danger_source != null && _danger_source.get_state() == State.DEAD) {
            _danger_source = null;
        }
        if (_danger_source == null) {
            this._state = State.NORMAL;
            return;
        }

        if (_danger_source != null) {
            _dest = _pos.plus(_pos.minus(_danger_source.get_position()).direction());
            move(_speedFactorSheep * _speed * dt * Math.exp((_energy - _maxenergy) * _multiplicativeMath));
            this._age += dt;

            //IMPORTANT
            if (_energy > _maxenergy) {
                _energy = _maxenergy;
            } else if (_energy < 0) {
                _energy = 0;
            }
            else {
                _energy -= _energyreductionSheep * _multiplicativeTime * dt;
            }

            if(_desire > _maxdesire){
                _desire = _maxdesire;
            }
            else if(_desire < _lowestdesire){
                _desire = _lowestdesire;
            }
            else {
                this._desire += _desirereductionSheep * dt;
            }
        } else {
            _danger_source=searchForDanger(_region_mngr, this._danger_strategy);

            if (_danger_source == null) {
                if (_desire < _desireUpperBound) {
                    _state = State.NORMAL;
                } else {
                    _state = State.MATE;
                }
            }
        }


    }

    private void updateAsMate(double dt) {
        if (this._mate_target != null && (this._state == State.DEAD || this._sight_range < _pos.distanceTo(_mate_target.get_position()))) {
            this._mate_target = null;
        } else if (this._mate_target == null) {
            _mate_target=searchForMate(_region_mngr, this._mate_strategy);
            if  (_mate_target == null) {
                updateAsNormal(dt);
            } else {
                _dest = _mate_target.get_position();
                move(_speedFactorSheep * dt * Math.exp((_energy - _maxenergy) * _multiplicativeMath));
                this._age += dt;

                //IMPORTANT
                if (_energy > _maxenergy) {
                    _energy = _maxenergy;
                } else if (_energy < 0) {
                    _energy = 0;
                } else {
                    _energy -= _energyreductionSheep * _multiplicativeTime * dt;
                }

                if (_desire > _maxdesire) {
                    _desire = _maxdesire;
                } else if (_desire < _lowestdesire) {
                    _desire = _lowestdesire;
                } else {
                    this._desire += _desirereductionSheep * dt;
                }


                if (this._pos.distanceTo(_mate_target.get_position()) < 8.0) {
                    this._desire = 0;
                    _mate_target._desire = 0;
                    if (!is_pregnant() && Math.random() < 0.9) {
                        _baby = new Sheep(this, _mate_target);
                    }
                    _mate_target = null;
                }
            }
        }
        if (this._danger_source == null){
            _danger_source=searchForDanger(_region_mngr, this._danger_strategy);
        }
        if (this._danger_source != null){
            _state = State.DANGER;
        }
        if (this._danger_source == null && this._desire < _desireUpperBound){
            this._state = State.NORMAL;
        }
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
    public double get_food(Animal a, double dt) {
        return 0;
    }

    @Override
    public List<Animal> get_animals_in_range(Animal a, Predicate<Animal> filter) {
        return null;
    }



}