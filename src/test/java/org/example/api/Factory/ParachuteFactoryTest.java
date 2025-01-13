package org.example.api.Factory;

import org.example.api.Dto.ParachuteDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParachuteFactoryTest {

    private ParachuteFactory factory;

    @BeforeEach
    public void setUp() {
        factory = ParachuteFactory.getInstance();
        factory.getList().clear(); // очищаем список перед каждым тестом
    }

    @Test
    public void testReadFromFileWithValidData() throws IOException {
        // Создаем тестовый файл
        File testFile = new File("test_parachute.txt");
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("100,Test Parachute 1,Description 1\n");
            writer.write("200,Test Parachute 2,Description 2\n");
        }

        factory.readFromFile(testFile.getPath());

        List<ParachuteDTO> parachutes = factory.getList();
        assertEquals(2, parachutes.size());
        assertEquals("Test Parachute 1", parachutes.get(0).getName());
        assertEquals("Description 2", parachutes.get(1).getDescription());

        // Удаляем тестовый файл
        testFile.delete();
    }

    @Test
    public void testReadFromFileWithInvalidData() throws IOException {
        // Создаем файл с некорректными данными
        File testFile = new File("invalid_parachute.txt");
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("Invalid Data\n");
        }

        factory.readFromFile(testFile.getPath());

        List<ParachuteDTO> parachutes = factory.getList();
        assertTrue(parachutes.isEmpty());

        // Удаляем тестовый файл
        testFile.delete();
    }

    @Test
    public void testFindByName() {
        factory.addToListStorage(new ParachuteDTO(100, "Unique Parachute", "Desc"));
        ParachuteDTO found = factory.findByName("Unique Parachute");
        assertNotNull(found);
        assertEquals(100, found.getCost());

        ParachuteDTO notFound = factory.findByName("Nonexistent Parachute");
        assertEquals(-1, notFound.getCost()); // Проверяем, что возвращается пустой объект
    }
}