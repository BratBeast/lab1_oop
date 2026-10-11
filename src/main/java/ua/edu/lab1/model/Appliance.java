package ua.edu.lab1.model;

/** Базовий клас електроприладу. */
public abstract class Appliance {
    private final String name;
    private final int ratedPowerWatts;
    private boolean pluggedIn;
    private boolean switchedOn;

    protected Appliance(String name, int ratedPowerWatts) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Назва не може бути порожньою.");
        }
        if (ratedPowerWatts <= 0) {
            throw new IllegalArgumentException("Потужність має бути додатною.");
        }
        this.name = name.strip();
        this.ratedPowerWatts = ratedPowerWatts;
    }

    public String getName() {
        return name;
    }

    public int getRatedPowerWatts() {
        return ratedPowerWatts;
    }

    public boolean isPluggedIn() {
        return pluggedIn;
    }

    public boolean isSwitchedOn() {
        return switchedOn;
    }

    /** Підключає прилад до розетки, але не запускає його. */
    public void plugIn() {
        pluggedIn = true;
    }

    /** Від'єднання від розетки також вимикає прилад. */
    public void unplug() {
        switchedOn = false;
        pluggedIn = false;
    }

    public void switchOn() {
        if (!pluggedIn) {
            throw new IllegalStateException("Спочатку підключіть прилад.");
        }
        switchedOn = true;
    }

    public void switchOff() {
        switchedOn = false;
    }

    // Вимкнений прилад не споживає потужність.
    public int getCurrentPowerWatts() {
        if (switchedOn) {
            return ratedPowerWatts;
        }
        return 0;
    }

    /** Кожен тип приладу описує власні характеристики. */
    public abstract String getDescription();
}
