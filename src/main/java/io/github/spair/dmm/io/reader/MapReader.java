package io.github.spair.dmm.io.reader;

import io.github.spair.dmm.io.DmmData;
import io.github.spair.dmm.io.TileContent;
import io.github.spair.dmm.io.TileObjectComparator;
import lombok.val;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@SuppressWarnings({"checkstyle:VisibilityModifier", "WeakerAccess"})
abstract class MapReader {

    final TileObjectComparator tileObjectComparator = new TileObjectComparator();

    OptimizedRandomAccessFile rFile;
    DmmData dmmData = new DmmData();

    String[][] localKeysByLocation;
    Map<String, String> localKeysDuplicates = new HashMap<>();

    MapReader(final OptimizedRandomAccessFile rFile) {
        this.rFile = rFile;
    }

    abstract DmmData read() throws Exception;

    // Some keys may contain absolutely same content.
    // This method ensures that those keys won't appear in DmmData.
    // Duplicated keys are saved to 'localKeysDuplicates'
    // to provide 'key/duplContent -> origKey/duplContent' connection.
    void addTileContentOrTraceDuplicateKey(final String key, final TileContent tileContent) {
        if (!dmmData.hasKeyByTileContent(tileContent)) {
            dmmData.addKeyAndTileContent(key, tileContent);
        } else {
            localKeysDuplicates.put(key, dmmData.getKeyByTileContent(tileContent));
        }
    }

    // 'localKeysByLocation' map initially takes first met key, so case with duplicated content isn't took into account.
    // With this method we replace keys with duplicated content by original keys.
    void replaceKeysWithDuplContentForLocations() {
        val duplicatedKeys = localKeysDuplicates.keySet();

        for (int y = 0; y < dmmData.getMaxY(); y++) {
            for (int x = 0; x < dmmData.getMaxX(); x++) {
                val key = localKeysByLocation[y][x];
                if (duplicatedKeys.contains(key)) {
                    localKeysByLocation[y][x] = localKeysDuplicates.get(key);
                }
            }
        }
    }

    void fillDmmDataContent() {
        for (int y = 0; y < dmmData.getMaxY(); y++) {
            for (int x = 0; x < dmmData.getMaxX(); x++) {
                dmmData.addTileContentByLocation(x + 1, y + 1, dmmData.getTileContentByKey(localKeysByLocation[y][x]));
            }
        }
    }

    // After cleaning locations from keys with duplicated content some keys could become redundant.
    // So here we fully remove them from result DmmData.
    void removeKeysWithoutLocation() {
        val keysWithLocation = new HashSet<String>();

        for (int y = 1; y <= dmmData.getMaxY(); y++) {
            for (int x = 1; x <= dmmData.getMaxX(); x++) {
                keysWithLocation.add(dmmData.getKeyByLocation(x, y));
            }
        }

        for (val key : new HashSet<>(dmmData.getKeys())) {
            if (!keysWithLocation.contains(key)) {
                dmmData.removeKeyAndTileContent(key);
            }
        }
    }
}
