package simulator.model;

import org.json.JSONObject;
import simulator.misc.Vector2D;

public class Wolf extends Animal{

    private Animal _hunt_target;
    private SelectionStrategy _hunting_strategy;

    public Wolf(SelectionStrategy mate_strategy, SelectionStrategy danger_strategy, Vector2D pos) {
        super();
        this._mate_strategy = mate_strategy;
        this._hunting_strategy = danger_strategy;
        this._pos = pos;
        this._speed = _speedConst;
        this._sight_range = _sightrangeConst;
    }

    protected Wolf(Wolf p1, Animal p2) {
        super(p1, p2);
        this._hunting_strategy = p1._hunting_strategy;
        this._hunt_target = null;
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
            //An object of type Wolf can never be in the DANGER state.

        }
        else if (this._state == State.Mate) {
            updateAsMate(dt);
        }


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
            searchForDanger();
            //TODO: Search for a new danger source

            if (this._desire > _desireUpperBound) {
                this._state = State.Mate;
            }
        } else {
            this._state = State.Danger;
        }
    }

    //i think we dont need this, but we shall discuss it
    //(An object of type Wolf can never be in the DANGER state.)
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
            searchForDanger();
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
            if (!searchForMate()) {
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
                        this._baby = new Wolf(this, _mate_target);
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
            searchForDanger();
        }
    }

    private boolean searchForMate() {
        return false;
    }   //single as fuck sorry bro find a donky u bitch  hahahaha

    /*private void searchForDanger(){}      i think there is no need for this function*/

    /*To find an animal to hunt, it asks the region manager for the list of herbivorous animals in the field of view,
using the get_animals_in_range method, and then chooses one using the corresponding selection
strategy.*/
    private void searchForPrey() {
        if (this._hunt_target == null) {
            this._hunt_target = _region_mngr.get_animal_in_range(this, _sight_range, Diet.HERBIVORE, _hunting_strategy);
        }
    }

    public void set_hunting_strategy(SelectionStrategy s) {
        this._hunting_strategy = s;
    }

    public SelectionStrategy get_hunting_strategy() {
        return this._hunting_strategy;
    }

    //Found it on the internet just kept here for reference maybe there is some stuff which what we can work with
    public JSONObject as_JSON() {
        JSONObject ObjectWolf = new JSONObject();
        ObjectWolf.put("pos", _pos.as_JSON());
        ObjectWolf.put("energy", _energy);
        ObjectWolf.put("age", _age);
        ObjectWolf.put("state", _state.toString());
        ObjectWolf.put("dest", _dest.as_JSON());
        ObjectWolf.put("speed", _speed);
        ObjectWolf.put("sight_range", _sight_range);
        ObjectWolf.put("desire", _desire);
        ObjectWolf.put("genetic_code", _genetic_code.as_JSON());
        ObjectWolf.put("diet", _diet.toString());
        ObjectWolf.put("mate_strategy", _mate_strategy.toString());
        ObjectWolf.put("hunting_strategy", _hunting_strategy.toString());
        if (_baby != null) {
            ObjectWolf.put("baby", _baby.as_JSON());
        }
        if (_mate_target != null) {
            ObjectWolf.put("mate_target", _mate_target.as_JSON());
        }
        if (_hunt_target != null) {
            ObjectWolf.put("hunt_target", _hunt_target.as_JSON());
        }
        return ObjectWolf;
    }

    public void set_hunt_target(Animal a) {
        this._hunt_target = a;
    }

    public Animal get_hunt_target() {
        return this._hunt_target;
    }


}
