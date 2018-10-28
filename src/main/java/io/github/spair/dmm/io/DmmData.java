package io.github.spair.dmm.io;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Set;
import java.util.Collection;

@Data
@Setter(AccessLevel.NONE)
public class DmmData {

    private boolean isTgm = false;

    private int maxX;
    private int maxY;

    private int keyLength;

    private Map<String, TileContent> tileContentsByKey = new TreeMap<>(new TileKeyComparator());
    private Map<TileContent, String> keysByTileContent = new HashMap<>();
    private Map<TileLocation, TileContent> tileContentsByLocation = new HashMap<>();

    public void addKeyAndTileContent(final String key, final TileContent tileContent) {
        tileContentsByKey.put(key, tileContent);
        keysByTileContent.put(tileContent, key);
    }

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

    public TileContent getTileContentByLocation(final int x, final int y) {
        return getTileContentByLocation(TileLocation.of(x, y));
    }

    public TileContent getTileContentByLocation(final TileLocation location) {
        return tileContentsByLocation.get(location);
    }

    public String getKeyByTileContent(final TileContent tileContent) {
        return keysByTileContent.get(tileContent);
    }

    public String getKeyByLocation(final int x, final int y) {
        return getKeyByLocation(TileLocation.of(x, y));
    }

    public String getKeyByLocation(final TileLocation location) {
        return getKeyByTileContent(getTileContentByLocation(location));
    }

    public Collection<TileContent> getTileContentsWithKeys() {
        return tileContentsByKey.values();
    }

    public Collection<TileContent> getTileContentsWithLocations() {
        return tileContentsByLocation.values();
    }

    public Set<String> getKeys() {
        return tileContentsByKey.keySet();
    }

    public Set<TileLocation> getLocations() {
        return tileContentsByLocation.keySet();
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

    public TileContent removeTileContentByLocation(final int x, final int y) {
        return tileContentsByLocation.remove(TileLocation.of(x, y));
    }

    public TileContent removeTileContentByLocation(final TileLocation tileLocation) {
        return tileContentsByLocation.remove(tileLocation);
    }

    public boolean hasTileContentByKey(final String key) {
        return tileContentsByKey.containsKey(key);
    }

    public boolean hasTileContentByLocation(final int x, final int y) {
        return hasTileContentByLocation(TileLocation.of(x, y));
    }

    public boolean hasTileContentByLocation(final TileLocation tileLocation) {
        return tileContentsByLocation.containsKey(tileLocation);
    }

    public boolean hasKeyByTileContent(final TileContent tileContent) {
        return keysByTileContent.containsKey(tileContent);
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

    public void setTgm(final boolean isTgm) {
        this.isTgm = isTgm;
    }
}
