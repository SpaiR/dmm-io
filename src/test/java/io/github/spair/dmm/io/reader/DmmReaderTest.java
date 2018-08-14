package io.github.spair.dmm.io.reader;

import io.github.spair.dmm.io.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DmmReaderTest {

    private DmmData expectedData = DmmDataUtil.getDmmData();

    @Test
    public void testReadMapAsBasic() {
        assertEquals(expectedData, DmmReader.readMap(ResourceUtil.readResourceFile("basic.dmm")));
    }

    @Test
    public void testReadMapAsTGM() {
        assertEquals(expectedData, DmmReader.readMap(ResourceUtil.readResourceFile("tgm.dmm")));
    }
}