package org.example.api.Misc;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ArchiverTest {

    @Test
    public void testCreateZipArchive() {
        Archiver archiver = new Archiver();

        try {
            FileWriter writer1 = new FileWriter("file1.txt");
            writer1.write("Test file 1");
            writer1.close();

            FileWriter writer2 = new FileWriter("file2.txt");
            writer2.write("Test file 2");
            writer2.close();

            String[] files = {"file1.txt", "file2.txt"};
            archiver.createZipArchive("test_archive.zip", files);

            File zipFile = new File("test_archive.zip");
            assertTrue(zipFile.exists());

            zipFile.delete();
            new File("file1.txt").delete();
            new File("file2.txt").delete();
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }

    @Test
    public void testCreateJarArchive() {
        Archiver archiver = new Archiver();

        try {
            FileWriter writer1 = new FileWriter("file1.txt");
            writer1.write("Test file 1");
            writer1.close();

            FileWriter writer2 = new FileWriter("file2.txt");
            writer2.write("Test file 2");
            writer2.close();

            String[] files = {"file1.txt", "file2.txt"};
            archiver.createJarArchive("test_archive.jar", files);

            File jarFile = new File("test_archive.jar");
            assertTrue(jarFile.exists());

            jarFile.delete();
            new File("file1.txt").delete();
            new File("file2.txt").delete();
        } catch (IOException e) {
            fail("IOException should not have been thrown");
        }
    }
}