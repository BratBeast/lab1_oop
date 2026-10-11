package ua.edu.lab1.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WashingMachineTest {
    @Test
    void storesLoadCapacityAndDescribesItsTypePolymorphically() {
        WashingMachine washer = new WashingMachine("Bosch", 2200, 7);
        Appliance appliance = washer;

        assertEquals(7, washer.getLoadCapacityKg());
        assertEquals("Пральна машина Bosch (7 кг)", appliance.getDescription());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void rejectsNonPositiveLoad(int loadCapacityKg) {
        assertThrows(IllegalArgumentException.class,
                () -> new WashingMachine("Bosch", 2200, loadCapacityKg));
    }
}
