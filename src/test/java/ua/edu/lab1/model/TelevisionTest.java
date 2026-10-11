package ua.edu.lab1.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TelevisionTest {
    @Test
    void storesScreenSizeAndDescribesItsTypePolymorphically() {
        Television television = new Television("Samsung", 120, 43);
        Appliance appliance = television;

        assertEquals(43, television.getScreenSizeInches());
        assertEquals("Телевізор Samsung (43 дюймів)",
                appliance.getDescription());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void rejectsNonPositiveScreenSize(int screenSizeInches) {
        assertThrows(IllegalArgumentException.class,
                () -> new Television("Samsung", 120, screenSizeInches));
    }
}
