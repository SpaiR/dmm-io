package io.github.spair.dmm.io;

public final class DmmDataUtil {
    public static DmmData getDmmData(boolean isTGM) {
        DmmData dmmData = new DmmData();

        dmmData.setTgm(isTGM);

        dmmData.setMaxX(3);
        dmmData.setMaxY(3);
        dmmData.setKeyLength(1);

        TileContent a = new TileContent();
        a.addTileObject(new TileObject("/obj/structure/inflatable/survival"));
        a.addTileObject(new TileObject("/turf/simulated/floor/pod"));

        TileContent b = new TileContent();
        b.addTileObject(new TileObject("/obj/structure/sign/mining/survival") {
            {
                addVar("icon_state", "\"survival\"");
                addVar("dir", "1");
            }
        });
        b.addTileObject(new TileObject("/obj/structure/inflatable/survival"));

        TileContent c = new TileContent();
        c.addTileObject(new TileObject("/obj/structure/fans"));
        c.addTileObject(new TileObject("/turf/simulated/floor/pod"));
        c.addTileObject(new TileObject("/area/survivalpod") {
            {
                addVar("icon_state", "\"survival\"");
            }
        });

        dmmData.addKeyByTileContent(a, "a");
        dmmData.addKeyByTileContent(b, "b");
        dmmData.addKeyByTileContent(c, "c");

        dmmData.addTileContentByKey("a", a);
        dmmData.addTileContentByKey("b", b);
        dmmData.addTileContentByKey("c", c);

        dmmData.addTileContentByLocation(TileLocation.of(1, 1), a);
        dmmData.addTileContentByLocation(TileLocation.of(2, 1), a);
        dmmData.addTileContentByLocation(TileLocation.of(3, 1), a);

        dmmData.addTileContentByLocation(TileLocation.of(1, 2), b);
        dmmData.addTileContentByLocation(TileLocation.of(2, 2), c);
        dmmData.addTileContentByLocation(TileLocation.of(3, 2), b);

        dmmData.addTileContentByLocation(TileLocation.of(1, 3), a);
        dmmData.addTileContentByLocation(TileLocation.of(2, 3), a);
        dmmData.addTileContentByLocation(TileLocation.of(3, 3), a);

        return dmmData;
    }
}
