package ua.edu.lab1.model;

/** Електрочайник із заданим об'ємом у мілілітрах. */
public final class Kettle extends Appliance {
    private final int capacityMilliliters;

    public Kettle(String name, int ratedPowerWatts, int capacityMilliliters) {
        super(name, ratedPowerWatts);
        if (capacityMilliliters <= 0) {
            throw new IllegalArgumentException("Об'єм має бути додатним.");
        }
        this.capacityMilliliters = capacityMilliliters;
    }

    public int getCapacityMilliliters() {
        return capacityMilliliters;
    }

    @Override
    public String getDescription() {
        return "Чайник " + getName() + " (" + capacityMilliliters + " мл)";
    }
}
