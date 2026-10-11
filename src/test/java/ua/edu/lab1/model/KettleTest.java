package ua.edu.lab1.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class KettleTest {
    @Test
    void storesCapacityAndDescribesItsTypePolymorphically() {
        Kettle kettle = new Kettle("Philips", 2000, 1700);
        Appliance appliance = kettle;

        assertEquals(1700, kettle.getCapacityMilliliters());
        assertEquals("Чайник Philips (1700 мл)", appliance.getDescription());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void rejectsNonPositiveCapacity(int capacityMilliliters) {
        assertThrows(IllegalArgumentException.class,
                () -> new Kettle("Philips", 2000, capacityMilliliters));
    }
}
