package ua.edu.lab1.model;

/** Телевізор із заданою діагоналлю екрана в дюймах. */
public final class Television extends Appliance {
    private final int screenSizeInches;

    public Television(String name, int ratedPowerWatts, int screenSizeInches) {
        super(name, ratedPowerWatts);
        if (screenSizeInches <= 0) {
            throw new IllegalArgumentException("Діагональ має бути додатною.");
        }
        this.screenSizeInches = screenSizeInches;
    }

    public int getScreenSizeInches() {
        return screenSizeInches;
    }

    @Override
    public String getDescription() {
        return "Телевізор " + getName() + " (" + screenSizeInches + " дюймів)";
    }
}
