package io.github.spair.dmm.io;

import lombok.val;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;

final class DmmDataWriter {

    private static final String NEW_LINE = System.lineSeparator();

    static File saveAsByond(final File fileToSave, final DmmData map) {
        checkFileToSave(fileToSave);

        try (val writer = new BufferedWriter(new FileWriter(fileToSave))) {
            for (String key : map.getKeys()) {
                writer.write(toByondString(key, map.getTileContentByKey(key)) + NEW_LINE);
            }

            writer.append(NEW_LINE);
            writer.write("(1,1,1) = {\"" + NEW_LINE);

            for (int y = map.getMaxY(); y >= 1; y--) {
                for (int x = 1; x <= map.getMaxX(); x++) {
                    writer.write(map.getKeyByLocation(x, y));
                }
                writer.write(NEW_LINE);
            }

            writer.write("\"}" + NEW_LINE);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return fileToSave;
    }

    static File saveAsTGM(final File fileToSave, final DmmData map) {
        checkFileToSave(fileToSave);

        try (val writer = new BufferedWriter(new FileWriter(fileToSave))) {
            writer.write("//MAP CONVERTED BY dmm2tgm.py THIS HEADER COMMENT PREVENTS RECONVERSION, DO NOT REMOVE");
            writer.write(NEW_LINE);

            for (String key : map.getKeys()) {
                writer.write(toTGMString(key, map.getTileContentByKey(key)) + NEW_LINE);
            }

            for (int x = 1; x <= map.getMaxX(); x++) {
                writer.write(NEW_LINE);
                writer.write("(" + x + ",1,1) = {\"" + NEW_LINE);

                for (int y = map.getMaxY(); y >= 1; y--) {
                    writer.write(map.getKeyByLocation(x, y));
                    writer.write(NEW_LINE);
                }

                writer.write("\"}");
            }
            writer.write(NEW_LINE);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return fileToSave;
    }

    private static String toByondString(final String key, final TileContent tileContent) {
        StringBuilder sb = new StringBuilder('"' + key + "\" = (");

        val objIter = tileContent.iterator();
        while (objIter.hasNext()) {
            val tileObject = objIter.next();

            sb.append(tileObject.getType());

            if (tileObject.hasVars()) {
                sb.append('{');

                val varIter = tileObject.iterator();
                while (varIter.hasNext()) {
                    val varEntry = varIter.next();
                    val varName = varEntry.getKey();
                    val varValue = varEntry.getValue();
                    sb.append(varName).append(" = ").append(varValue);

                    if (varIter.hasNext()) {
                        sb.append("; ");
                    }
                }

                sb.append('}');
            }

            if (objIter.hasNext()) {
                sb.append(',');
            }
        }

        sb.append(')');

        return sb.toString();
    }

    private static String toTGMString(final String key, final TileContent tileContent) {
        StringBuilder sb = new StringBuilder('"' + key + "\" = (").append(NEW_LINE);

        val objIter = tileContent.iterator();
        while (objIter.hasNext()) {
            val tileObject = objIter.next();

            sb.append(tileObject.getType());

            if (tileObject.hasVars()) {
                sb.append('{').append(NEW_LINE);

                val varIter = tileObject.iterator();
                while (varIter.hasNext()) {
                    val varEntry = varIter.next();
                    val varName = varEntry.getKey();
                    val varValue = varEntry.getValue();
                    sb.append('\t').append(varName).append(" = ").append(varValue);

                    if (varIter.hasNext()) {
                        sb.append(";");
                    }

                    sb.append(NEW_LINE);
                }

                sb.append('\t').append('}');
            }

            if (objIter.hasNext()) {
                sb.append(',').append(NEW_LINE);
            }
        }

        sb.append(')');

        return sb.toString();
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

    private DmmDataWriter() {
    }
}
