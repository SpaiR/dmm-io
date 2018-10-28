package io.github.spair.dmm.io;

import io.github.spair.dmm.io.reader.DmmReader;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TileObjectComparatorTest {

    private List<TileObject> expectedAObjects;
    private List<TileObject> expectedBObjects;
    private List<TileObject> expectedCObjects;

    @Before
    public void setUp() {
        expectedAObjects = Arrays.asList(new TileObject("/obj/item"), new TileObject("/turf/floor"));
        expectedBObjects = Arrays.asList(new TileObject("/turf/floor"), new TileObject("/area/zone"));
        expectedCObjects = Arrays.asList(new TileObject("/obj/item"), new TileObject("/turf/floor"), new TileObject("/area/zone"));
    }

    @Test
    public void testCompareWithBasic() {
        DmmData dmmData = DmmReader.readMap(ResourceUtil.readResourceFile("basic_with_wrong_order.dmm"));
        commonAssert(dmmData);
    }

    @Test
    public void testCompareWithTGM() {
        DmmData dmmData = DmmReader.readMap(ResourceUtil.readResourceFile("tgm_with_wrong_order.dmm"));
        commonAssert(dmmData);
    }

    private void commonAssert(final DmmData dmmData) {
        TileContent a = dmmData.getTileContentByKey("a");
        TileContent b = dmmData.getTileContentByKey("b");
        TileContent c = dmmData.getTileContentByKey("c");

        assertEquals(expectedAObjects, a.getTileObjects());
        assertEquals(expectedBObjects, b.getTileObjects());
        assertEquals(expectedCObjects, c.getTileObjects());
    }
}