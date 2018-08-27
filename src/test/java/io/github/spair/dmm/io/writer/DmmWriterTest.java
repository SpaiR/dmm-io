package io.github.spair.dmm.io.writer;

import io.github.spair.dmm.io.*;
import io.github.spair.dmm.io.reader.DmmReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;

public class DmmWriterTest {

    private File tmpFile;

    @Before
    public void setUp() throws Exception {
        tmpFile = Files.createTempFile(null, ".dmm").toFile();
        tmpFile.deleteOnExit();
    }

    @After
    public void tearDown() {
        tmpFile.delete();
    }

    @Test
    public void testSaveAsByond() {
        DmmData dmmData = DmmDataUtil.getDmmData(false);
        DmmWriter.saveAsByond(tmpFile, dmmData);
        assertEquals(dmmData, DmmReader.readMap(tmpFile));
    }

    @Test
    public void testSaveAsTGM() {
        DmmData dmmData = DmmDataUtil.getDmmData(true);
        DmmWriter.saveAsTGM(tmpFile, dmmData);
        assertEquals(dmmData, DmmReader.readMap(tmpFile));
    }
}