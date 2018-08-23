package io.github.spair.dmm.io.reader;

import io.github.spair.dmm.io.DmmData;
import lombok.val;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.List;

public final class DmmReader {

    private static final String TGM_MARKER = "//MAP";   // No point to check this header fully since it's for sure TGM.

    public static DmmData readMap(final File mapFile) {
        val fileLines = readAllLines(mapFile);

        if (fileLines.isEmpty()) {
            throw new IllegalArgumentException("Map file is empty");
        }

        return getReader(fileLines).read();
    }

    private static List<String> readAllLines(final File mapFile) {
        try {
            return Files.readAllLines(mapFile.toPath());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static MapReader getReader(final List<String> lines) {
        return lines.get(0).startsWith(TGM_MARKER) ? new TGMReader(lines) : new ByondReader(lines);
    }

    private DmmReader() {
    }
}
