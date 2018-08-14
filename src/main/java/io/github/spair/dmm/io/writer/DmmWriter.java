package io.github.spair.dmm.io.writer;

import io.github.spair.dmm.io.DmmData;
import io.github.spair.dmm.io.TileLocation;
import lombok.val;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;

import static io.github.spair.dmm.io.writer.TileContentStringifier.toBasicString;
import static io.github.spair.dmm.io.writer.TileContentStringifier.toTGMString;

public final class DmmWriter {

    private static final String NEW_LINE = System.lineSeparator();
    private static final String TGM_HEADER =
            "//MAP CONVERTED BY dmm2tgm.py THIS HEADER COMMENT PREVENTS RECONVERSION, DO NOT REMOVE";

    public static File saveAsBasic(final File fileToSave, final DmmData map) {
        checkFileToSave(fileToSave);

        try (val writer = new BufferedWriter(new FileWriter(fileToSave))) {
            for (String key : map.getTileContentsByKey().keySet()) {
                writer.write(toBasicString(map.getTileContentByKey(key)) + NEW_LINE);
            }

            writer.append(NEW_LINE);
            writer.write("(1,1,1) = {\"" + NEW_LINE);

            for (int y = map.getMaxY(); y >= 1; y--) {
                for (int x = 1; x <= map.getMaxX(); x++) {
                    writer.write(map.getKeyByLocation(TileLocation.of(x, y)));
                }
                writer.write(NEW_LINE);
            }

            writer.write("\"}");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return fileToSave;
    }

    public static File saveAsTGM(final File fileToSave, final DmmData map) {
        checkFileToSave(fileToSave);

        try (val writer = new BufferedWriter(new FileWriter(fileToSave))) {
            writer.write(TGM_HEADER);
            writer.write(NEW_LINE);

            for (String key : map.getTileContentsByKey().keySet()) {
                writer.write(toTGMString(map.getTileContentByKey(key)) + NEW_LINE);
            }

            for (int x = 1; x <= map.getMaxX(); x++) {
                writer.write(NEW_LINE);
                writer.write("(" + x + ",1,1) = {\"" + NEW_LINE);

                for (int y = map.getMaxY(); y >= 1; y--) {
                    writer.write(map.getKeyByLocation(TileLocation.of(x, y)));
                    writer.write(NEW_LINE);
                }

                writer.write("\"}");
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return fileToSave;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void checkFileToSave(final File file) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private DmmWriter() {
    }
}
