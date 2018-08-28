package io.github.spair.dmm.io.reader;

import io.github.spair.dmm.io.DmmData;
import io.github.spair.dmm.io.TileContent;
import io.github.spair.dmm.io.TileLocation;
import lombok.val;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@SuppressWarnings("checkstyle:VisibilityModifier")
abstract class AbstractReader implements MapReader {

    final DmmData dmmData = new DmmData();
    final List<String> mapLines;
    String currentLine;

    final Map<TileLocation, String> localKeysByLocation = new HashMap<>();
    private final Map<String, String> localKeysDuplicates = new HashMap<>();

    AbstractReader(final List<String> mapLines) {
        this.mapLines = mapLines;
    }

    // Some keys may contain absolutely same content.
    // This method ensures that those keys won't appear in DmmData.
    // Duplicated keys are saved to 'localKeysDuplicates'
    // to provide 'key/duplContent -> origKey/duplContent' connection.
    void addTileContentOrTraceDuplicateKey(final String key, final TileContent tileContent) {
        if (!dmmData.getKeysByTileContent().containsKey(tileContent)) {
            dmmData.addTileContentByKey(key, tileContent);
            dmmData.addKeyByTileContent(tileContent, key);
        } else {
            localKeysDuplicates.put(key, dmmData.getKeyByTileContent(tileContent));
        }
    }

    // 'localKeysByLocation' map initially takes first met key, so case with duplicated content isn't took into account.
    // With this method we replace keys with duplicated content by original keys.
    void replaceKeysWithDuplContentForLocations() {
        val duplicatedKeys = localKeysDuplicates.keySet();
        localKeysByLocation.forEach((location, key) -> {
            if (duplicatedKeys.contains(key)) {
                localKeysByLocation.put(location, localKeysDuplicates.get(key));
            }
        });
    }

    // For consistency reasons Y axis for locations is mirrored, since we read file from top to bottom,
    // while map in BYOND is represented from bottom to top.
    void mirrorLocationYAxisAndAddToDmmData() {
        localKeysByLocation.forEach((location, key) -> {
            location.setY(dmmData.getMaxY() - location.getY() + 1);
            dmmData.addTileContentByLocation(location, dmmData.getTileContentByKey(key));
        });
    }

    // After cleaning locations from keys with duplicated content some keys could become redundant.
    // So here we fully remove them from result DmmData.
    void removeKeysWithoutLocation() {
        val keysWithLocation = new HashSet<String>();

        dmmData.getTileContentsByLocation().forEach((location, tileContent) ->
                keysWithLocation.add(dmmData.getKeyByLocation(location))
        );

        for (String key : new HashSet<>(dmmData.getTileContentsByKey().keySet())) {
            if (!keysWithLocation.contains(key)) {
                val tileContent = dmmData.getTileContentsByKey().remove(key);
                dmmData.getKeysByTileContent().remove(tileContent);
            }
        }
    }
}
