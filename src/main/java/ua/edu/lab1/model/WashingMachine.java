package ua.edu.lab1.model;

/** Пральна машина з максимальною масою завантаження в кілограмах. */
public final class WashingMachine extends Appliance {
    private final int loadCapacityKg;

    public WashingMachine(String name, int ratedPowerWatts,
            int loadCapacityKg) {
        super(name, ratedPowerWatts);
        if (loadCapacityKg <= 0) {
            throw new IllegalArgumentException(
                    "Завантаження має бути додатним.");
        }
        this.loadCapacityKg = loadCapacityKg;
    }

    public int getLoadCapacityKg() {
        return loadCapacityKg;
    }

    @Override
    public String getDescription() {
        return "Пральна машина " + getName() + " (" + loadCapacityKg + " кг)";
    }
}
