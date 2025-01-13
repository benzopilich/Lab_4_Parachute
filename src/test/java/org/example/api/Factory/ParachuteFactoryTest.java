package org.example.api.Factory;

import org.example.api.Dto.ParachuteDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class ParachuteFactoryTest {

    @Test
    public void testReadFromFile() {
        ParachuteFactory factory = ParachuteFactory.getInstance();
        factory.readFromFile("test_parachute.txt");
        List<ParachuteDTO> parachutes = factory.getList();
        assertFalse(parachutes.isEmpty());
    }

    @Test
    public void testWriteToFile() {
        ParachuteFactory factory = ParachuteFactory.getInstance();
        factory.addToListStorage(new ParachuteDTO(100, "Test Parachute", "Test Description"));
        factory.writeToFile("output_parachute.txt");
    }
}