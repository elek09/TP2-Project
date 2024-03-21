package simulator.model;

import simulator.misc.Vector2D;

public class Wolf extends Animal {
    private final SelectionStrategy _hunting_strategy;
    private Animal _hunt_target;

    /**
     * Constructs a Wolf with the specified mate strategy, hunting strategy, and position.
     *
     * @param mate_strategy   The mate selection strategy.
     * @param danger_strategy The hunting selection strategy.
     * @param pos             The position of the wolf.
     */
    public Wolf(SelectionStrategy mate_strategy, SelectionStrategy danger_strategy, Vector2D pos) {
        super("Wolf", Diet.CARNIVORE, _sightrangeConst, _speedConst, mate_strategy, pos);
        this._mate_strategy = mate_strategy;
        this._hunting_strategy = danger_strategy;
        this._hunt_target = null;
    }

    protected Wolf(Wolf p1, Animal p2) {
        super(p1, p2);
        this._hunting_strategy = p1.get_hunting_strategy();
        this._hunt_target = null;
    }

    /**
     * Retrieves the hunting strategy of the wolf.
     *
     * @return The hunting selection strategy of the wolf.
     */
    private SelectionStrategy get_hunting_strategy() {
        return this._hunting_strategy;
    }

    /**
     * Updates the wolf's state and position.
     *
     * @param dt The time step.
     */
    @Override
    public void update(double dt) {
        switch (_state) {
            case NORMAL:
                updateAsNormal(dt);
                break;
            case DEAD:
                break;
            case HUNGER:
                updateAsHunger(dt);
                break;
            case MATE:
                updateAsMate(dt);
                break;
        }
        if (IsOutOfMap()) {
            this._state = State.NORMAL;
            updateAsNormal(dt);
        } else if (this._energy <= _lowestenergy || this._age > _wolfAge) {
            this._state = State.DEAD;
        }
        _energy += this._region_mngr.get_food(this, dt);
        checkEnergy();
    }

    /**
     * Updates the wolf's state and behavior when it is in the NORMAL state.
     *
     * @param dt The time increment for the update.
     */
    private void updateAsNormal(double dt) {
        if (_pos.distanceTo(_dest) < distanceDest) {
            _dest = new Vector2D(Math.random() * _region_mngr.get_width(), Math.random() * _region_mngr.get_height());
        }
        move(this._speed * dt * Math.exp((this._energy - _maxenergy) * _movefactor));
        this._age += dt;

        _energy -= _energyreductionWolf * dt;
        checkEnergy();

        _desire += _desirereductionWolf * dt;
        checkDesire();

        if (this._energy < _energyBound) {
            _state = State.HUNGER;
        } else if (this._desire > _desireUpperBound) {
            this._state = State.MATE;
        }
    }

    /**
     * Updates the wolf's state and behavior when it is in the HUNGER state.
     *
     * @param dt The time increment for the update.
     */
    private void updateAsHunger(double dt) {
        if ((this._hunt_target == null) || (this._hunt_target != null && (this._hunt_target._state == State.DEAD || this._hunt_target.get_position().distanceTo(_pos) > get_sight_range()))) {
            this._hunt_target = searchForHuntTarget(_region_mngr, this._hunting_strategy);
            if (this._hunt_target == null) {
                updateAsNormal(dt);
            }
        }
        if (this._hunt_target != null) {
            _dest = _hunt_target.get_position();
            move(_speedFactorWolf * dt * Math.exp((_energy - _maxenergy) * _multiplicativeMath));

            this._age += dt;

            _energy -= _energyreductionWolf * dt;
            checkEnergy();

            _desire += _desirereductionWolf * dt;
            checkDesire();

            if (this._pos.distanceTo(_hunt_target.get_position()) < distanceDest) {
                this._hunt_target._state = State.DEAD;
                this._hunt_target = null;
                _energy += _energyBound;
                checkEnergy();

                if (this._energy > _energyBound) {
                    if (this._desire < _desireUpperBound) {
                        this._state = State.NORMAL;
                    } else {
                        if (this._desire >= 50)
                            this._state = State.MATE;
                    }
                }
            }
        }
    }

    /**
     * Updates the wolf's state and behavior when it is in the MATE state.
     *
     * @param dt The time increment for the update.
     */
    private void updateAsMate(double dt) {
        if (_mate_target != null && (_mate_target.get_state() == State.DEAD || _mate_target.get_position().distanceTo(_pos) > _sight_range)) {
            _mate_target = null;

        }
        if (_mate_target == null) {
            _mate_target = searchForMate(_region_mngr, _mate_strategy);
            if (_mate_target == null) {
                updateAsNormal(dt);
            }
        }
        if (_mate_target != null) {
            _dest = _mate_target.get_position();
            move(_speedFactorWolf * dt * Math.exp((_energy - _maxenergy) * _multiplicativeMath));
            _age += dt;

            _energy -= _energyreductionWolf * _multiplicativeTime * dt;
            checkEnergy();

            _desire += _desirereductionWolf * dt;
            checkDesire();

            if (_pos.distanceTo(_mate_target.get_position()) < distanceDest) {
                setDesire(0);
                _mate_target.setDesire(0);

                if (!is_pregnant() && Math.random() < _createBaby) {
                    _baby = new Wolf(this, _mate_target);
                    _energy -= _sexEnergy;
                    checkEnergy();
                    _mate_target = null;
                }
            }
        }
        if (_energy < _energyBound) {
            _state = State.HUNGER;
        }
        if (_energy >= _energyBound && _desire < _desireUpperBound) {
            _state = State.NORMAL;
        }
    }
}