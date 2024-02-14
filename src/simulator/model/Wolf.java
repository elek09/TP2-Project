package simulator.model;

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
    }

    private void searchForDanger(){}
}
