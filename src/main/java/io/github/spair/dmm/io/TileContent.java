package io.github.spair.dmm.io;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Data
@Setter(AccessLevel.NONE)
public class TileContent implements Iterable<TileObject> {

    private List<TileObject> tileObjects = new ArrayList<>();

    public void addTileObject(final TileObject tileObject) {
        tileObjects.add(tileObject);
    }

    @Override
    public Iterator<TileObject> iterator() {
        return tileObjects.iterator();
    }
}
