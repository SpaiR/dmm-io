package io.github.spair.dmm.io.reader;

import io.github.spair.dmm.io.DmmData;
import lombok.val;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public final class DmmReader {

    private static final String TGM_MARKER = "//MAP";   // No point to check this header fully since it's for sure TGM.

    public static DmmData readMap(final File mapFile) {
        try (val bufferedReader = new BufferedReader(new FileReader(mapFile))) {
            bufferedReader.mark(1);

            boolean isTgm = bufferedReader.readLine().startsWith(TGM_MARKER);
            MapReader mapReader;

            if (isTgm) {
                mapReader = new TGMReader(bufferedReader);
            } else {
                bufferedReader.reset();
                mapReader = new ByondReader(bufferedReader);
            }

            return mapReader.read();
        } catch (Exception e) {
            throw new RuntimeException("Unable to read map. Path: " + mapFile.getPath(), e);
        }
    }

    private DmmReader() {
    }
}
