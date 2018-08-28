package io.github.spair.dmm.io.reader;

import io.github.spair.dmm.io.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DmmReaderTest {

    @Test
    public void testReadMapAsBasic() {
        assertEquals(DmmDataUtil.getDmmData(false), DmmReader.readMap(ResourceUtil.readResourceFile("basic.dmm")));
    }

    @Test
    public void testReadMapAsBasicWithUnusedKeys() {
        assertEquals(DmmDataUtil.getDmmData(false), DmmReader.readMap(ResourceUtil.readResourceFile("basic_with_unused_keys.dmm")));
    }

    @Test
    public void testReadMapAsBasicWithDuplicatedKeys() {
        assertEquals(DmmDataUtil.getDmmData(false), DmmReader.readMap(ResourceUtil.readResourceFile("basic_with_duplicated_keys.dmm")));
    }

    @Test
    public void testReadMapAsTGM() {
        assertEquals(DmmDataUtil.getDmmData(true), DmmReader.readMap(ResourceUtil.readResourceFile("tgm.dmm")));
    }

    @Test
    public void testReadMapAsTGMWithUnusedKeys() {
        assertEquals(DmmDataUtil.getDmmData(true), DmmReader.readMap(ResourceUtil.readResourceFile("tgm_with_unused_keys.dmm")));
    }

    @Test
    public void testReadMapAsTGMWithDuplicatedKeys() {
        assertEquals(DmmDataUtil.getDmmData(true), DmmReader.readMap(ResourceUtil.readResourceFile("tgm_with_duplicated_keys.dmm")));
    }
}