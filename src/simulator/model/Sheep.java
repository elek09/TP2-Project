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
        this._speed = 35.0;
        this._sight_range = 40.0;
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
            //Don't know why it is not working
            if (distanceTo(_dest) < 8.0){
                _dest = new Vector2D(Math.random() * 800, Math.random() * 600);
            }
            move(this._speed * dt * Math.exp((this._energy - 100.0) * 0.007));
            this._age += dt;
            this._energy -= dt * 20.0;
            assert this._energy > 0 && this._energy <= 100.0;
            this._desire += 40.0 * dt;
            assert this._desire > 0 && this._desire <= 100.0;
            if (this._danger_source == null){
                //TODO: Search for a new danger source

                if (this._desire > 65.0){
                    this._state = State.Mate;
                }
            }
            else{
                this._state = State.Danger;
            }
        }
        if (this._state == State.Danger){

        }


    }
}
