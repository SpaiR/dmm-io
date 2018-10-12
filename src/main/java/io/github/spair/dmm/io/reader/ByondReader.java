package io.github.spair.dmm.io.reader;

import io.github.spair.dmm.io.DmmData;
import io.github.spair.dmm.io.TileLocation;
import lombok.val;

import java.io.BufferedReader;
import java.util.regex.Pattern;

final class ByondReader extends MapReader {

    private final ByondTileContentReader tileContentReader = new ByondTileContentReader();

    private int currentY = 1;
    private int maxX;

    private boolean isTilesRead = false;

    private int keyLength;
    private Pattern keySplit;

    ByondReader(final BufferedReader bufferedReader) {
        super(bufferedReader);
    }

    @Override
    public DmmData read() {
        while ((currentLine = readLine()) != null) {
            if (currentLine.isEmpty()) {
                isTilesRead = true;
                continue;
            }

            val firstLineChar = currentLine.charAt(0);

            if (firstLineChar == '"' && !isTilesRead) {
                readTileDeclaration();
            } else if (firstLineChar != '(') {
                if (firstLineChar != '"') {
                    readMapTiles();
                } else {
                    break;
                }
            }
        }

        dmmData.setMaxX(maxX);
        dmmData.setMaxY(currentY - 1);

        replaceKeysWithDuplContentForLocations();
        mirrorLocationYAxisAndAddToDmmData();
        removeKeysWithoutLocation();

        return dmmData;
    }

    private void readTileDeclaration() {
        if (keyLength == 0) {
            keyLength = currentLine.indexOf('"', 1) - 1;
            keySplit = Pattern.compile(String.format("(?<=\\G.{%d})", keyLength));
            dmmData.setKeyLength(keyLength);
        }

        val key = currentLine.substring(1, 1 + keyLength);
        val rawValue = currentLine.substring(currentLine.indexOf('('));
        val tileContent = tileContentReader.read(rawValue.substring(1, rawValue.length() - 1));

        addTileContentOrTraceDuplicateKey(key, tileContent);
    }

    private void readMapTiles() {
        val keys = keySplit.split(currentLine);
        maxX = keys.length;
        int x = 1;
        for (String key : keys) {
            localKeysByLocation.put(TileLocation.of(x++, currentY), key);
        }
        currentY++;
    }
}
