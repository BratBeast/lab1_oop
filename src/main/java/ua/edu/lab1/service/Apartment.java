package ua.edu.lab1.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import ua.edu.lab1.model.Appliance;

/** Квартира зі списком електроприладів. */
public class Apartment {
    private final List<Appliance> appliances = new ArrayList<>();

    public void addAppliance(Appliance appliance) {
        Objects.requireNonNull(appliance, "Прилад не може бути null.");
        if (appliances.contains(appliance)) {
            throw new IllegalArgumentException("Цей прилад уже є у квартирі.");
        }
        appliances.add(appliance);
    }

    // Копія захищає список квартири від змін ззовні.
    public List<Appliance> getAppliances() {
        return List.copyOf(appliances);
    }

    public long getTotalCurrentPowerWatts() {
        long totalPowerWatts = 0;
        for (Appliance appliance : appliances) {
            totalPowerWatts += appliance.getCurrentPowerWatts();
        }
        return totalPowerWatts;
    }

    /** Сортує список квартири за зростанням номінальної потужності. */
    public void sortByPower() {
        appliances.sort(Comparator.comparingInt(Appliance::getRatedPowerWatts));
    }

    /** Шукає всі прилади за номінальною потужністю; обидві межі включені. */
    public List<Appliance> findByPowerRange(int minWatts, int maxWatts) {
        if (minWatts < 0 || maxWatts < minWatts) {
            throw new IllegalArgumentException("Некоректний діапазон.");
        }
        List<Appliance> result = new ArrayList<>();
        for (Appliance appliance : appliances) {
            int powerWatts = appliance.getRatedPowerWatts();
            if (powerWatts >= minWatts && powerWatts <= maxWatts) {
                result.add(appliance);
            }
        }
        return List.copyOf(result);
    }
}
