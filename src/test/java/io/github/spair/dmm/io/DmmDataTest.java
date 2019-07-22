package io.github.spair.dmm.io;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DmmDataTest {

    @Test
    public void testSetDmmSize() {
        DmmData dmmData = new DmmData();
        dmmData.setDmmSize(5, 5);

        for (int i = 1; i <= 5; i++) {
            dmmData.addTileContentByLocation(i, i, new TileContent());
            assertNotNull(dmmData.getTileContentByLocation(i, i));
        }

        dmmData.setDmmSize(3, 3);

        for (int i = 1; i <= 3; i++) {
            assertNull(dmmData.getTileContentByLocation(i, i));
            dmmData.addTileContentByLocation(i, i, new TileContent());
        }

        dmmData.setDmmSize(5, 5, true);

        for (int i = 1; i <= 3; i++) {
            assertNotNull(dmmData.getTileContentByLocation(i, i));
        }

        for (int i = 4; i <= 5; i++) {
            assertNull(dmmData.getTileContentByLocation(i, i));
        }
    }
}
