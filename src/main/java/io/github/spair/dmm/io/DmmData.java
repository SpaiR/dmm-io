package io.github.spair.dmm.io;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.val;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Data
public class DmmData {

    private boolean isTgm = false;
    private int keyLength;
    private TileContent[][] content;

    @Setter(AccessLevel.NONE) private int maxX;
    @Setter(AccessLevel.NONE) private int maxY;

    @Setter(AccessLevel.NONE) private Map<String, TileContent> tileContentsByKey = new TreeMap<>(new TileKeyComparator());
    @Setter(AccessLevel.NONE) private Map<TileContent, String> keysByTileContent = new HashMap<>();

    public void setDmmSize(final int maxX, final int maxY) {
        setDmmSize(maxX, maxY, false);
    }

    public void setDmmSize(final int maxX, final int maxY, final boolean keepContent) {
        val tmpContent = new TileContent[maxY][maxX];

        if (keepContent) {
            for (int x = 0; x < maxX; x++) {
                for (int y = 0; y < maxY; y++) {
                    if (x < this.maxX && y < this.maxY) {
                        tmpContent[y][x] = content[y][x];
                    }
                }
            }
        }

        this.maxX = maxX;
        this.maxY = maxY;
        this.content = tmpContent;
    }

    public File saveAsByond(final File fileToSave) {
        return DmmDataWriter.saveAsByond(fileToSave, this);
    }

    public File saveAsTGM(final File fileToSave) {
        return DmmDataWriter.saveAsTGM(fileToSave, this);
    }

    public void addKeyAndTileContent(final String key, final TileContent tileContent) {
        tileContentsByKey.put(key, tileContent);
        keysByTileContent.put(tileContent, key);
    }

    public void addTileContentByKey(final String key, final TileContent tileContent) {
        tileContentsByKey.put(key, tileContent);
    }

    public void addKeyByTileContent(final TileContent tileContent, final String key) {
        keysByTileContent.put(tileContent, key);
    }

    public void addTileContentByLocation(final int x, final int y, final TileContent tileContent) {
        content[y - 1][x - 1] = tileContent;
    }

    public TileContent getTileContentByKey(final String key) {
        return tileContentsByKey.get(key);
    }

    public TileContent getTileContentByLocation(final int x, final int y) {
        return content[y - 1][x - 1];
    }

    public String getKeyByTileContent(final TileContent tileContent) {
        return keysByTileContent.get(tileContent);
    }

    public String getKeyByLocation(final int x, final int y) {
        return getKeyByTileContent(getTileContentByLocation(x, y));
    }

    public boolean hasLocationsWithoutContent() {
        for (int x = 1; x <= maxX; x++) {
            for (int y = 1; y <= maxY; y++) {
                if (getTileContentByLocation(x, y) == null) {
                    return true;
                }
            }
        }
        return false;
    }

    public Set<String> getKeys() {
        return tileContentsByKey.keySet();
    }

    public void removeKeyAndTileContent(final String key) {
        keysByTileContent.remove(tileContentsByKey.remove(key));
    }

    public String removeKeyByTileContent(final TileContent tileContent) {
        return keysByTileContent.remove(tileContent);
    }

    public TileContent removeTileContentByKey(final String key) {
        return tileContentsByKey.remove(key);
    }

    public void removeTileContentByLocation(final int x, final int y) {
        content[y - 1][x - 1] = null;
    }

    public boolean hasTileContentByKey(final String key) {
        return tileContentsByKey.containsKey(key);
    }

    public boolean hasTileContentByLocation(final int x, final int y) {
        return getTileContentByLocation(x, y) != null;
    }

    public boolean hasKeyByTileContent(final TileContent tileContent) {
        return keysByTileContent.containsKey(tileContent);
    }
}
