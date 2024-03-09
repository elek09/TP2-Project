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
            checkEnergy();
        }

    }


    private void updateAsNormal(double dt) {
        if (_pos.distanceTo(_dest) < distanceDest) {
            _dest = new Vector2D(Math.random() * _region_mngr.get_width(), Math.random() * _region_mngr.get_height());
        }
        move(_speed * dt * Math.exp((this._energy - _maxenergy) * _movefactor));
        _age += dt;

        //Energy reduction always between 0 and 100
        _energy -= dt * _energyreductionSheep;
        checkEnergy();
        //Desire addition always between 0 and 100
        _desire += _desirereductionSheep * dt;
        checkDesire();


        if (this._danger_source == null) {
            _danger_source = searchForDanger(_region_mngr, this._danger_strategy);
            if(this._desire > 65.0){
                this._state = State.MATE;
            }
        }
        else {
            this._state = State.DANGER;
        }

    }


    private void updateAsDanger(double dt) {
        if (_danger_source != null) {
            if(_danger_source.get_state() == State.DEAD){
                _danger_source = null;
            }
            else{
                _dest = _pos.plus(_pos.minus(_danger_source.get_position()).direction());
                move(_speedFactorSheep * _speed * dt * Math.exp((_energy - _maxenergy) * _multiplicativeMath));
                this._age += dt;
            }

            _energy -= _energyreductionSheep * _multiplicativeTime * dt;
            checkEnergy();

            _desire += _desirereductionSheep * dt;
            checkDesire();

        }
        if ((_danger_source == null) || (this._pos.distanceTo(_danger_source.get_position()) <= this._sight_range)) {
            _danger_source = searchForDanger(_region_mngr, _danger_strategy);
            if(_danger_source == null) {
                if(_desire < _desireUpperBound) {
                    this._state = State.NORMAL;
                }
                else {
                    this._state = State.MATE;
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


                _energy -= _energyreductionSheep * _multiplicativeTime * dt;
                checkEnergy();

                this._desire += _desirereductionSheep * dt;
                checkDesire();

                if (this._pos.distanceTo(_mate_target.get_position()) < 8.0) {
                    this.setDesire(0);
                    this._mate_target.setDesire(0);
                    if (!is_pregnant() && Math.random() < _createBaby) {
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




}