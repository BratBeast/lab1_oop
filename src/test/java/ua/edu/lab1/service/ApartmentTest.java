package ua.edu.lab1.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import ua.edu.lab1.model.Appliance;
import ua.edu.lab1.model.Kettle;
import ua.edu.lab1.model.Television;
import ua.edu.lab1.model.WashingMachine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ApartmentTest {
    @Test
    void emptyApartmentHasZeroConsumptionAndNoMatches() {
        Apartment apartment = new Apartment();

        apartment.sortByPower();

        assertTrue(apartment.getAppliances().isEmpty());
        assertEquals(0, apartment.getTotalCurrentPowerWatts());
        assertTrue(apartment.findByPowerRange(0, 3000).isEmpty());
    }

    @Test
    void applianceListIsAnUnmodifiableSnapshot() {
        Apartment apartment = new Apartment();
        Appliance first = new Kettle("Philips", 2000, 1700);
        Appliance second = new Television("LG", 90, 32);
        apartment.addAppliance(first);
        List<Appliance> snapshot = apartment.getAppliances();

        apartment.addAppliance(second);

        assertEquals(List.of(first), snapshot);
        assertEquals(List.of(first, second), apartment.getAppliances());
        assertThrows(UnsupportedOperationException.class, snapshot::clear);
    }

    @Test
    void rejectsNullAndTheSameObjectAddedTwice() {
        Apartment apartment = new Apartment();
        Appliance appliance = new Kettle("Philips", 2000, 1700);
        apartment.addAppliance(appliance);

        assertThrows(NullPointerException.class,
                () -> apartment.addAppliance(null));
        assertThrows(IllegalArgumentException.class,
                () -> apartment.addAppliance(appliance));
        assertEquals(List.of(appliance), apartment.getAppliances());
    }

    @Test
    void allowsTwoSeparateAppliancesOfTheSameModel() {
        Apartment apartment = new Apartment();
        Appliance first = new Kettle("Philips", 2000, 1700);
        Appliance second = new Kettle("Philips", 2000, 1700);
        apartment.addAppliance(first);
        apartment.addAppliance(second);

        assertEquals(List.of(first, second), apartment.getAppliances());
    }

    @Test
    void countsOnlyRunningAppliancesAndRecalculatesAfterStateChanges() {
        Apartment apartment = new Apartment();
        Appliance kettle = new Kettle("Philips", 2000, 1700);
        Appliance television = new Television("Samsung", 120, 43);
        Appliance washer = new WashingMachine("Bosch", 2200, 7);
        apartment.addAppliance(kettle);
        apartment.addAppliance(television);
        apartment.addAppliance(washer);

        kettle.plugIn();
        kettle.switchOn();
        washer.plugIn();
        assertEquals(2000, apartment.getTotalCurrentPowerWatts());

        television.plugIn();
        television.switchOn();
        assertEquals(2120, apartment.getTotalCurrentPowerWatts());

        kettle.unplug();
        assertEquals(120, apartment.getTotalCurrentPowerWatts());

        television.switchOff();
        assertEquals(0, apartment.getTotalCurrentPowerWatts());
    }

    @Test
    void sumsPowerThroughTheApplianceContractUsingMockito() {
        Appliance first = mock(Appliance.class);
        Appliance second = mock(Appliance.class);
        when(first.getCurrentPowerWatts()).thenReturn(750);
        when(second.getCurrentPowerWatts()).thenReturn(125);
        Apartment apartment = new Apartment();
        apartment.addAppliance(first);
        apartment.addAppliance(second);

        assertEquals(875, apartment.getTotalCurrentPowerWatts());

        verify(first).getCurrentPowerWatts();
        verify(second).getCurrentPowerWatts();
        verifyNoMoreInteractions(first, second);
    }

    @Test
    void totalPowerDoesNotOverflowTheIntRange() {
        Apartment apartment = new Apartment();
        Appliance first = new Kettle("A", Integer.MAX_VALUE, 1);
        Appliance second = new Kettle("B", Integer.MAX_VALUE, 1);
        first.plugIn();
        first.switchOn();
        second.plugIn();
        second.switchOn();
        apartment.addAppliance(first);
        apartment.addAppliance(second);

        assertEquals(4294967294L, apartment.getTotalCurrentPowerWatts());
    }

    @Test
    void sortsByRatedPowerAndPreservesOrderForEqualPower() {
        Apartment apartment = new Apartment();
        Appliance high = new WashingMachine("Bosch", 2200, 7);
        Appliance equalFirst = new Kettle("A", 2000, 1700);
        Appliance low = new Television("LG", 90, 32);
        Appliance equalSecond = new Kettle("B", 2000, 1500);
        low.plugIn();
        low.switchOn();
        apartment.addAppliance(high);
        apartment.addAppliance(equalFirst);
        apartment.addAppliance(low);
        apartment.addAppliance(equalSecond);

        apartment.sortByPower();

        assertEquals(List.of(low, equalFirst, equalSecond, high),
                apartment.getAppliances());
        assertEquals(90, apartment.getTotalCurrentPowerWatts());
    }

    @Test
    void findsAllMatchesIncludingBothBoundariesRegardlessOfState() {
        Apartment apartment = new Apartment();
        Appliance below = new Television("LG", 90, 32);
        Appliance lower = new Television("Samsung", 120, 43);
        Appliance inside = new Kettle("Tefal", 1800, 1500);
        Appliance upper = new Kettle("Philips", 2000, 1700);
        Appliance above = new WashingMachine("Bosch", 2200, 7);
        List<Appliance> all = List.of(below, lower, inside, upper, above);
        for (Appliance appliance : all) {
            apartment.addAppliance(appliance);
        }
        lower.plugIn();
        lower.switchOn();

        List<Appliance> matches = apartment.findByPowerRange(120, 2000);

        assertEquals(List.of(lower, inside, upper), matches);
        assertEquals(List.of(below, lower, inside, upper, above),
                apartment.getAppliances());
        assertThrows(UnsupportedOperationException.class, matches::clear);
    }

    @Test
    void acceptsAnExactPowerAndReturnsEmptyListWhenNothingMatches() {
        Apartment apartment = new Apartment();
        Appliance appliance = new Kettle("Philips", 2000, 1700);
        apartment.addAppliance(appliance);

        assertEquals(List.of(appliance),
                apartment.findByPowerRange(2000, 2000));
        assertTrue(apartment.findByPowerRange(0, 0).isEmpty());
        assertTrue(apartment.findByPowerRange(3000, 4000).isEmpty());
    }

    @ParameterizedTest
    @CsvSource({"-1, 100", "200, 100", "0, -1"})
    void rejectsAnInvalidRange(int minWatts, int maxWatts) {
        Apartment apartment = new Apartment();

        assertThrows(IllegalArgumentException.class,
                () -> apartment.findByPowerRange(minWatts, maxWatts));
    }
}
