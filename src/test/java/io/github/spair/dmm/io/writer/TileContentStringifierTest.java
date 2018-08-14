package io.github.spair.dmm.io.writer;

import io.github.spair.dmm.io.TileContent;
import io.github.spair.dmm.io.TileObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TileContentStringifierTest {

    private static final String NEW_LINE = System.lineSeparator();
    private TileContent tileContent;

    @Before
    public void setUp() {
        tileContent = new TileContent();
        tileContent.setKey("aaa");

        TileObject tileObject = new TileObject();
        tileObject.setType("/obj/item");
        tileContent.addTileObject(tileObject);

        tileObject = new TileObject();
        tileObject.setType("/area/zone");
        tileObject.addVar("var1", "value1");
        tileContent.addTileObject(tileObject);

        tileObject = new TileObject();
        tileObject.setType("/turf/ground");
        tileObject.addVar("var2", "value2");
        tileObject.addVar("var3", "value3");
        tileContent.addTileObject(tileObject);
    }

    @Test
    public void testToBasicString() {
        assertEquals(
                "\"aaa\" = (/obj/item,/area/zone{var1 = value1},/turf/ground{var2 = value2; var3 = value3})",
                TileContentStringifier.toBasicString(tileContent)
        );
    }

    @Test
    public void testToTGMString() {
        assertEquals(
                "\"aaa\" = (" + NEW_LINE
                        + "/obj/item," + NEW_LINE
                        + "/area/zone{" + NEW_LINE
                        + "\tvar1 = value1" + NEW_LINE
                        + "\t}," + NEW_LINE
                        + "/turf/ground{" + NEW_LINE
                        + "\tvar2 = value2;" + NEW_LINE
                        + "\tvar3 = value3" + NEW_LINE
                        + "\t})",
                TileContentStringifier.toTGMString(tileContent)
        );
    }
}