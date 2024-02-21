package simulator.model;

import java.util.List;

public interface SelectionStrategy {
    Animal select(Animal a, List<Animal> as);

    public static SelectionStrategy SelectFirst = (a, as) -> {
        return as.get(0);
    };
    public static SelectionStrategy SelectClosest = (a, as) -> {
        Animal closest = as.get(0);
        for (Animal animal : as) {
            if (a.get_position().distanceTo(animal.get_position()) < a.get_position().distanceTo(closest.get_position())) {
                closest = animal;
            }
        }
        return closest;
    };
    public static SelectionStrategy SelectYoungest = (a, as) -> {
        Animal youngest = as.get(0);
        for (Animal animal : as) {
            if (animal.get_age() < youngest.get_age()) {
                youngest = animal;
            }
        }
        return youngest;
    };

}
