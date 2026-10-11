package ua.edu.lab1.app;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {
    @Test
    void demoShowsCorrectTotalsSortingSearchAndAllStates() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (PrintStream output = new PrintStream(buffer, true,
                StandardCharsets.UTF_8)) {
            new Main().run(output);
        }
        String text = buffer.toString(StandardCharsets.UTF_8);

        assertTrue(text.contains("Варіант 3."));
        assertTrue(text.contains("Сумарна споживана потужність: 2120 Вт"));
        assertTrue(text.contains("Після від'єднання чайника Philips: 120 Вт"));
        assertTrue(text.contains("Вт | працює"));
        assertTrue(text.contains("Вт | вимкнено"));
        assertTrue(text.contains("Вт | не підключено"));

        int sortStart = text.indexOf("Сортування за номінальною потужністю");
        int searchStart = text.indexOf("Пошук за номінальною потужністю");
        String sorted = text.substring(sortStart, searchStart);
        assertTrue(sorted.indexOf("Телевізор LG")
                < sorted.indexOf("Телевізор Samsung"));
        assertTrue(sorted.indexOf("Телевізор Samsung")
                < sorted.indexOf("Чайник Tefal"));
        assertTrue(sorted.indexOf("Чайник Tefal")
                < sorted.indexOf("Чайник Philips"));
        assertTrue(sorted.indexOf("Чайник Philips")
                < sorted.indexOf("Пральна машина Bosch"));

        String search = text.substring(searchStart,
                text.indexOf("Після від'єднання"));
        assertEquals(2, search.lines().filter(line -> line.contains(" | "))
                .count());
        assertTrue(search.contains("Чайник Tefal"));
        assertTrue(search.contains("Чайник Philips"));
    }

    @Test
    void mainWritesTheDemonstrationToStandardOutput() {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (PrintStream output = new PrintStream(buffer, true,
                StandardCharsets.UTF_8)) {
            System.setOut(output);
            Main.main(new String[0]);
        } finally {
            System.setOut(original);
        }

        assertTrue(buffer.toString(StandardCharsets.UTF_8)
                .contains("Сумарна споживана потужність: 2120 Вт"));
    }

    @Test
    void rejectsMissingOutput() {
        assertThrows(NullPointerException.class, () -> new Main().run(null));
    }
}
