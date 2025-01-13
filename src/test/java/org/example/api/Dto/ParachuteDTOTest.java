package org.example.api.Dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParachuteDTOTest {

    @Test
    public void testParachuteDTO() {
        ParachuteDTO parachute = new ParachuteDTO(100, "Test Parachute", "Test Description");
        assertEquals(100, parachute.getCost());
        assertEquals("Test Parachute", parachute.getName());
        assertEquals("Test Description", parachute.getDescription());
    }

    @Test
    public void testToString() {
        ParachuteDTO parachute = new ParachuteDTO(100, "Test Parachute", "Test Description");
        String expected = "ParachuteDTO{cost=100, name='Test Parachute', description='Test Description'}";
        assertEquals(expected, parachute.toString());
    }
}