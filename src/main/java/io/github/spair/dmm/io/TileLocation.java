package io.github.spair.dmm.io;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TileLocation {

    private int x;
    private int y;

    public static TileLocation of(final int x, final int y) {
        return new TileLocation(x, y);
    }
}
