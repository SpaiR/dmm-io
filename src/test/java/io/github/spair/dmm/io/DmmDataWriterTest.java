package io.github.spair.dmm.io;

import io.github.spair.dmm.io.reader.DmmReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;

import static org.junit.Assert.*;

public class DmmDataWriterTest {

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
    public void saveAsByond() throws Exception {
        DmmData dmmData = DmmDataUtil.getDmmData(false);
        DmmDataWriter.saveAsByond(tmpFile, dmmData);
        assertEquals(dmmData, DmmReader.readMap(tmpFile));

        File basicFile = ResourceUtil.readResourceFile("basic.dmm");
        dmmData = DmmReader.readMap(basicFile);
        DmmDataWriter.saveAsByond(tmpFile, dmmData);

        assertFilesAreIdentical(basicFile, tmpFile);
    }

    @Test
    public void saveAsTGM() throws Exception {
        DmmData dmmData = DmmDataUtil.getDmmData(true);
        DmmDataWriter.saveAsTGM(tmpFile, dmmData);
        assertEquals(dmmData, DmmReader.readMap(tmpFile));

        File tgmFile = ResourceUtil.readResourceFile("tgm.dmm");
        dmmData = DmmReader.readMap(tgmFile);
        DmmDataWriter.saveAsTGM(tmpFile, dmmData);

        assertFilesAreIdentical(tgmFile, tmpFile);
    }

    private void assertFilesAreIdentical(final File f1, final File f2) throws Exception {
        // Fast and easy way to check if last line contains line ending symbol.
        assertArrayEquals("files should be absolutely identical", Files.readAllBytes(f1.toPath()), Files.readAllBytes(f2.toPath()));
    }
}