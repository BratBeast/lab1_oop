package ua.edu.lab1.app;

import java.io.PrintStream;
import java.util.List;
import java.util.Objects;

import ua.edu.lab1.model.Appliance;
import ua.edu.lab1.model.Kettle;
import ua.edu.lab1.model.Television;
import ua.edu.lab1.model.WashingMachine;
import ua.edu.lab1.service.Apartment;

/** Мінімальна консольна демонстрація всіх операцій варіанта 3. */
public class Main {
    public static void main(String[] args) {
        new Main().run(System.out);
    }

    public void run(PrintStream output) {
        Objects.requireNonNull(output, "Потік виведення не може бути null.");

        Apartment apartment = new Apartment();
        Kettle kettle = new Kettle("Philips", 2000, 1700);
        Television television = new Television("Samsung", 120, 43);
        WashingMachine washer = new WashingMachine("Bosch", 2200, 7);

        apartment.addAppliance(kettle);
        apartment.addAppliance(television);
        apartment.addAppliance(washer);
        apartment.addAppliance(new Kettle("Tefal", 1800, 1500));
        apartment.addAppliance(new Television("LG", 90, 32));

        kettle.plugIn();
        kettle.switchOn();
        television.plugIn();
        television.switchOn();
        washer.plugIn();

        output.println("Лабораторна робота 1. Варіант 3.");
        output.println("Побутові електроприлади");
        output.println();
        output.println("Прилади у квартирі:");
        printAppliances(output, apartment.getAppliances());

        output.println();
        output.println("Сумарна споживана потужність: "
                + apartment.getTotalCurrentPowerWatts() + " Вт");

        apartment.sortByPower();
        output.println();
        output.println("Сортування за номінальною потужністю (зростання):");
        printAppliances(output, apartment.getAppliances());

        output.println();
        output.println("Пошук за номінальною потужністю: 1000-2100 Вт");
        printAppliances(output, apartment.findByPowerRange(1000, 2100));

        kettle.unplug();
        output.println();
        output.println("Після від'єднання чайника Philips: "
                + apartment.getTotalCurrentPowerWatts() + " Вт");
    }

    private void printAppliances(PrintStream output,
            List<Appliance> appliances) {
        for (Appliance appliance : appliances) {
            output.println(appliance.getDescription()
                    + " | номінальна: " + appliance.getRatedPowerWatts()
                    + " Вт | поточна: " + appliance.getCurrentPowerWatts()
                    + " Вт | " + getStateLabel(appliance));
        }
    }

    private String getStateLabel(Appliance appliance) {
        if (!appliance.isPluggedIn()) {
            return "не підключено";
        }
        if (appliance.isSwitchedOn()) {
            return "працює";
        }
        return "вимкнено";
    }
}
