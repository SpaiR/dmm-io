package io.github.spair.dmm.io.reader;

import io.github.spair.dmm.io.DmmData;
import lombok.val;

import java.io.File;

public final class DmmReader {

    private static final String TGM_MARKER = "//MAP";   // No point to check this header fully since it's for sure TGM.

    public static DmmData readMap(final File mapFile) {
        try (val rFile = new OptimizedRandomAccessFile(mapFile, "r")) {

            boolean isTgm = rFile.readLine().startsWith(TGM_MARKER);
            MapReader mapReader;

            if (isTgm) {
                mapReader = new TGMReader(rFile);
            } else {
                rFile.seek(0);
                mapReader = new ByondReader(rFile);
            }

            return mapReader.read();
        } catch (Exception e) {
            throw new RuntimeException("Unable to read map. Path: " + mapFile.getPath(), e);
        }
    }

    private DmmReader() {
    }
}
