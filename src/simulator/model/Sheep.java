package simulator.model;

import simulator.misc.Vector2D;

public class Sheep extends Animal{

    private Animal _danger_source;
    private SelectionStrategy _danger_strategy;
    public Sheep (SelectionStrategy mate_strategy, SelectionStrategy danger_strategy, Vector2D pos){
        super();
        this._mate_strategy = mate_strategy;
        this._danger_strategy = danger_strategy;
        this._pos = pos;
        this._speed = _speedConst;
        this._sight_range = _sightrangeConst;
    }
    protected Sheep(Sheep p1, Animal p2){
        super(p1, p2);
        this._danger_strategy = p1._danger_strategy;
        this._danger_source = null;
    }
    public void update(double dt){
        if (this._state == State.Dead){
            return;
        }
        if (this._state == State.Normal){
            updateAsNormal(dt);
        }
        if (this._state == State.Danger){
            updateAsDanger(dt);

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
            //TODO: Search for a new danger source

            if (this._desire > 65.0) {
                this._state = State.Mate;
            }
        } else {
            this._state = State.Danger;
        }
    }
    private void updateAsDanger(double dt){
        if(this._danger_source != null){
            if(this._state == State.Dead){
                this._danger_source = null;
                return;
            }
            else {
                this._dest = _pos.plus(_pos.minus(_danger_source.get_position()).direction());
                move(2.0 * _speed * dt * Math.exp( (_energy - 100.0 ) * 0.007 ));
                this._age += dt;
                this._energy -= 20.0 * 1.2*dt;
            }

        }
        else{
            updateAsNormal(dt);
        }




    }
}
