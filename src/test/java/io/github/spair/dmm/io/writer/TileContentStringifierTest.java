package io.github.spair.dmm.io.writer;

import io.github.spair.dmm.io.TileContent;
import io.github.spair.dmm.io.TileObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TileContentStringifierTest {

    private static final String NEW_LINE = System.lineSeparator();
    private TileContent tileContent;

    @Before
    public void setUp() {
        tileContent = new TileContent();

        TileObject tileObject = new TileObject();
        tileObject.setType("/obj/item");
        tileContent.addTileObject(tileObject);

        tileObject = new TileObject();
        tileObject.setType("/area/zone");
        tileObject.putVar("var1", "value1");
        tileContent.addTileObject(tileObject);

        tileObject = new TileObject();
        tileObject.setType("/turf/ground");
        tileObject.putVar("var2", "value2");
        tileObject.putVar("var3", "value3");
        tileContent.addTileObject(tileObject);
    }

    @Test
    public void testToByondString() {
        assertEquals(
                "\"aaa\" = (/obj/item,/area/zone{var1 = value1},/turf/ground{var2 = value2; var3 = value3})",
                TileContentStringifier.toByondString("aaa", tileContent)
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
                TileContentStringifier.toTGMString("aaa", tileContent)
        );
    }
}