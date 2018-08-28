package io.github.spair.dmm.io;

import lombok.val;

import java.util.Comparator;

/**
 * BYOND expects that objects in tile content will be sorted in the next way:
 * <ol>
 *     <li>movables</li>
 *     <li>turfs</li>
 *     <li>areas</li>
 * </ol>
 * This comparator provides that order.
 */
public final class TileObjectComparator implements Comparator<TileObject> {

    private static final String TURF_TYPE = "/turf";
    private static final String AREA_TYPE = "/area";

    @Override
    public int compare(final TileObject obj1, final TileObject obj2) {
        val type1 = obj1.getType();
        val type2 = obj2.getType();

        if (type1.startsWith(AREA_TYPE)) {
            if (!type2.startsWith(AREA_TYPE)) {
                return 1;
            }
        } else if (type1.startsWith(TURF_TYPE)) {
            if (type2.startsWith(AREA_TYPE)) {
                return -1;
            } else if (!type2.startsWith(TURF_TYPE)) {
                return 1;
            }
        } else if (type2.startsWith(TURF_TYPE) || type2.startsWith(AREA_TYPE)) {
            return -1;
        }

        return 0;
    }
}
