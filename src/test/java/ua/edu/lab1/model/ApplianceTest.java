package ua.edu.lab1.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApplianceTest {
    private Appliance appliance;

    @BeforeEach
    void setUp() {
        appliance = new Kettle("  Philips  ", 2000, 1700);
    }

    @Test
    void storesValidatedDataAndStartsDisconnected() {
        assertEquals("Philips", appliance.getName());
        assertEquals(2000, appliance.getRatedPowerWatts());
        assertFalse(appliance.isPluggedIn());
        assertFalse(appliance.isSwitchedOn());
        assertEquals(0, appliance.getCurrentPowerWatts());
    }

    @Test
    void pluggingInDoesNotStartTheAppliance() {
        appliance.plugIn();

        assertTrue(appliance.isPluggedIn());
        assertFalse(appliance.isSwitchedOn());
        assertEquals(0, appliance.getCurrentPowerWatts());
    }

    @Test
    void cannotSwitchOnWithoutPowerConnection() {
        assertThrows(IllegalStateException.class, appliance::switchOn);
        assertFalse(appliance.isSwitchedOn());
        assertEquals(0, appliance.getCurrentPowerWatts());
    }

    @Test
    void switchingOffStopsConsumptionButKeepsTheConnection() {
        appliance.plugIn();
        appliance.switchOn();
        assertTrue(appliance.isSwitchedOn());
        assertEquals(2000, appliance.getCurrentPowerWatts());

        appliance.switchOff();

        assertTrue(appliance.isPluggedIn());
        assertFalse(appliance.isSwitchedOn());
        assertEquals(0, appliance.getCurrentPowerWatts());
    }

    @Test
    void unpluggingStopsConsumptionAndReconnectingDoesNotRestartIt() {
        appliance.plugIn();
        appliance.switchOn();

        appliance.unplug();

        assertFalse(appliance.isPluggedIn());
        assertFalse(appliance.isSwitchedOn());
        assertEquals(0, appliance.getCurrentPowerWatts());
        assertThrows(IllegalStateException.class, appliance::switchOn);

        appliance.plugIn();
        assertFalse(appliance.isSwitchedOn());
        assertEquals(0, appliance.getCurrentPowerWatts());
    }

    @Test
    void repeatedStateCommandsAreSafe() {
        appliance.switchOff();
        appliance.unplug();
        appliance.plugIn();
        appliance.switchOn();
        appliance.plugIn();
        appliance.switchOn();
        assertEquals(2000, appliance.getCurrentPowerWatts());

        appliance.switchOff();
        appliance.switchOff();
        appliance.unplug();
        appliance.unplug();
        assertFalse(appliance.isPluggedIn());
        assertFalse(appliance.isSwitchedOn());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t\n"})
    void rejectsMissingName(String name) {
        assertThrows(IllegalArgumentException.class,
                () -> new Kettle(name, 2000, 1700));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, Integer.MIN_VALUE})
    void rejectsNonPositiveRatedPower(int powerWatts) {
        assertThrows(IllegalArgumentException.class,
                () -> new Kettle("Philips", powerWatts, 1700));
    }
}
