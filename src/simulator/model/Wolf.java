package simulator.model;

import org.json.JSONObject;
import simulator.misc.Vector2D;

import java.util.List;
import java.util.function.Predicate;

public class Wolf extends Animal{
    private Animal _hunt_target;
    private SelectionStrategy _hunting_strategy;
    public Wolf(SelectionStrategy mate_strategy, SelectionStrategy danger_strategy, Vector2D pos) {
        super("Wolf", Diet.CARNIVORE, _sightrangeConst, _speedConst, mate_strategy, pos);
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
        if (this._state == State.DEAD) {
            return;
        }
        else if (!IsOnTheMap(this._pos)){
            this._state = State.NORMAL;
            updateAsNormal(dt);
        }
        else if (this._energy==0.0||this._age>16.0){
            this._state = State.DEAD;
        }
        else if (this._state!=State.DEAD){
            _energy += this._region_mngr.get_food(this,dt);
        }
        else if (this._state == State.HUNGER) {
            updateAsHunger(dt);
        }
        else if (this._state == State.MATE) {
            updateAsMate(dt);
        }
        else if (this._state == State.DANGER) {
            return;
        }
    }

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

        if (this._energy < 50) {
            _state = State.HUNGER;
        } else if (this._desire > _desireUpperBound) {
            this._state = State.MATE;
        }
    }

    private void updateAsHunger(double dt) {
        if (this._hunt_target==null||this._hunt_target._state == State.DEAD || this._hunt_target.get_position().distanceTo(_pos)>_sight_range){
            this._hunt_target = searchForHuntTarget(_region_mngr, this._hunting_strategy);
            return;
        } else if (this._hunt_target == null){
            updateAsNormal(dt);
        } else if (this._hunt_target!=null){
            this._dest = _hunt_target.get_position();
            move(_speedFactorWolf * dt * Math.exp((_energy - _maxenergy) * _multiplicativeMath));

            this._age += dt;
            this._energy -= _energyreductionWolf * _multiplicativeTime * dt;
            assert this._energy > _lowestenergy && this._energy <= _maxenergy;

            this._desire += _desirereductionWolf * dt;
            assert this._desire > _lowestdesire && this._desire <= _maxdesire;

            if (this._pos.distanceTo(_hunt_target.get_position()) < 8.0) {
                this._hunt_target._state = State.DEAD;
                this._hunt_target = null;
                this._energy += _plusEnergy;
                if(this._energy>50.0){
                    if(this._desire<_desireUpperBound){
                        this._state = State.NORMAL;
                    }
                    else{
                        this._state = State.MATE;
                    }
                }
            }
        }
    }

    private void updateAsMate(double dt){
        if(this._mate_target._state==State.DEAD||this._mate_target.get_position().distanceTo(_pos)>_sight_range){
            this._mate_target = null;
            return;
        } else if(this._mate_target==null){
            if(searchForMate(_region_mngr, this._mate_strategy)!=null){
                this._mate_target=searchForMate(_region_mngr, this._mate_strategy);
            } else {
                updateAsNormal(dt);
            }
        } else if(this._mate_target!=null){
            _mate_target=searchForMate(_region_mngr, this._mate_strategy);
            this._dest = _mate_target.get_position();

            move(_speedFactorWolf * dt * Math.exp((_energy - _maxenergy) * _multiplicativeMath));
            this._age += dt;

            this._energy -= _energyreductionWolf * _multiplicativeTime * dt;
            assert this._energy > _lowestenergy && this._energy <= _maxenergy;

            this._desire += _desirereductionWolf * dt;
            assert this._desire > _lowestdesire && this._desire <= _maxdesire;

            if (this._pos.distanceTo(_mate_target.get_position()) < 8.0) {
                this._desire = 0.0;
                this._mate_target._desire = 0.0;

                if (this._baby == null && Math.random() < 0.9) {
                    this._baby = new Wolf(this, _mate_target);

                    this._energy -= _sexEnergy;
                    assert this._energy > _lowestenergy && this._energy <= _maxenergy;

                    this._mate_target=null;
                }
            }
            if(this._energy>50.0){
                this._state = State.HUNGER;
            } else if(this._desire>_desireUpperBound){
                this._state = State.NORMAL;
            }
        }
    }

    public void set_hunting_strategy(SelectionStrategy s) {
        this._hunting_strategy = s;
    }

    public SelectionStrategy get_hunting_strategy() {
        return this._hunting_strategy;
    }

    public void set_hunt_target(Animal a) {
        this._hunt_target = a;
    }

    public Animal get_hunt_target() {
        return this._hunt_target;
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

    @Override
    public Vector2D adjust_position(Vector2D pos) {
        return null;
    }

}
