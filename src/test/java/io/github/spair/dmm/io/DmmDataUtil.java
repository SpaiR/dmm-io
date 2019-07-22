package io.github.spair.dmm.io;

public final class DmmDataUtil {
    public static DmmData getDmmData(boolean isTGM) {
        DmmData dmmData = new DmmData();

        dmmData.setTgm(isTGM);
        dmmData.setDmmSize(3, 3);
        dmmData.setKeyLength(1);

        TileContent a = new TileContent();
        a.addTileObject(new TileObject("/obj/structure/inflatable/survival"));
        a.addTileObject(new TileObject("/turf/simulated/floor/pod"));

        TileContent b = new TileContent();
        b.addTileObject(new TileObject("/obj/structure/sign/mining/survival") {
            {
                putVar("icon_state", "\"survival\"");
                putVar("dir", "1");
            }
        });
        b.addTileObject(new TileObject("/obj/structure/inflatable/survival"));

        TileContent c = new TileContent();
        c.addTileObject(new TileObject("/obj/structure/fans"));
        c.addTileObject(new TileObject("/turf/simulated/floor/pod"));
        c.addTileObject(new TileObject("/area/survivalpod") {
            {
                putVar("icon_state", "\"survival\"");
            }
        });

        dmmData.addKeyByTileContent(a, "a");
        dmmData.addKeyByTileContent(b, "b");
        dmmData.addKeyByTileContent(c, "c");

        dmmData.addTileContentByKey("a", a);
        dmmData.addTileContentByKey("b", b);
        dmmData.addTileContentByKey("c", c);

        dmmData.addTileContentByLocation(1, 1, a);
        dmmData.addTileContentByLocation(2, 1, a);
        dmmData.addTileContentByLocation(3, 1, c);
        dmmData.addTileContentByLocation(1, 2, b);
        dmmData.addTileContentByLocation(2, 2, c);
        dmmData.addTileContentByLocation(3, 2, b);
        dmmData.addTileContentByLocation(1, 3, a);
        dmmData.addTileContentByLocation(2, 3, a);
        dmmData.addTileContentByLocation(3, 3, a);

        return dmmData;
    }
}
