package org.example.GUI;

import org.example.api.Dto.ParachuteDTO;
import org.example.api.Factory.ParachuteFactory;
import org.example.persistence.Repositories.AbstractStorage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileOperationsTest {

    @Test
    public void testWriteToFile() throws IOException {
        AbstractStorage<ParachuteDTO> storage = new ParachuteFactory();
        storage.addToListStorage(new ParachuteDTO(150, "Parachute A", "Desc A"));

        FileOperations.writeToFile(storage, "write_test.txt");

        File testFile = new File("write_test.txt");
        assertTrue(testFile.exists());

        testFile.delete();
    }

    @Test
    public void testReadFromFileWithNonExistentFile() {
        AbstractStorage<ParachuteDTO> storage = new ParachuteFactory();
        assertThrows(IOException.class, () -> {
            FileOperations.readFromFile(storage, "non_existent_file.txt");
        });
    }
}