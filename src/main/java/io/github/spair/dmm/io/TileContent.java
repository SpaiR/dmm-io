package io.github.spair.dmm.io;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor
public class TileContent implements Iterable<TileObject> {

    private String key;
    private List<TileObject> tileObjects = new ArrayList<>();

    public TileContent(final String key) {
        this.key = key;
    }

    public void addTileObject(final TileObject tileObject) {
        tileObjects.add(tileObject);
    }

    public void setKey(final String key) {
        this.key = key;
    }

    @Override
    public Iterator<TileObject> iterator() {
        return tileObjects.iterator();
    }
}
