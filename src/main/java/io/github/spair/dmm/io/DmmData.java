package io.github.spair.dmm.io;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Data
@Setter(AccessLevel.NONE)
public class DmmData {

    private int maxX;
    private int maxY;

    private int keyLength;

    private Map<String, TileContent> tileContentsByKey = new TreeMap<>(new TileKeyComparator());
    private Map<TileLocation, TileContent> tileContentsByLocation = new HashMap<>();
    private Map<TileContent, String> keysByTileContent = new HashMap<>();

    public void addTileContentByKey(final String key, final TileContent tileContent) {
        tileContentsByKey.put(key, tileContent);
    }

    public void addTileContentByLocation(final TileLocation tileLocation, final TileContent tileContent) {
        tileContentsByLocation.put(tileLocation, tileContent);
    }

    public void addKeyByTileContent(final TileContent tileContent, final String key) {
        keysByTileContent.put(tileContent, key);
    }

    public TileContent getTileContentByKey(final String key) {
        return tileContentsByKey.get(key);
    }

    public TileContent getTileContentByLocation(final TileLocation location) {
        return tileContentsByLocation.get(location);
    }

    public String getKeyByTileContent(final TileContent tileContent) {
        return keysByTileContent.get(tileContent);
    }

    public String getKeyByLocation(final TileLocation location) {
        return getKeyByTileContent(getTileContentByLocation(location));
    }

    public void setMaxX(final int maxX) {
        this.maxX = maxX;
    }

    public void setMaxY(final int maxY) {
        this.maxY = maxY;
    }

    public void setKeyLength(final int keyLength) {
        this.keyLength = keyLength;
    }
}
