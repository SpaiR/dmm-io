package io.github.spair.dmm.io.writer;

import io.github.spair.dmm.io.*;
import io.github.spair.dmm.io.reader.DmmReader;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;

public class DmmWriterTest {

    private DmmData dataToSave = DmmDataUtil.getDmmData();
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
    public void testSaveAsBasic() {
        DmmWriter.saveAsBasic(tmpFile, dataToSave);
        assertEquals(dataToSave, DmmReader.readMap(tmpFile));
    }

    @Test
    public void testSaveAsTGM() {
        DmmWriter.saveAsTGM(tmpFile, dataToSave);
        assertEquals(dataToSave, DmmReader.readMap(tmpFile));
    }
}