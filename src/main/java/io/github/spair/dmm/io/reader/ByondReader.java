package io.github.spair.dmm.io.reader;

import io.github.spair.dmm.io.DmmData;
import io.github.spair.dmm.io.TileLocation;
import lombok.val;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

final class ByondReader implements MapReader {

    private final DmmData dmmData = new DmmData();
    private final ByondTileContentReader tileContentReader = new ByondTileContentReader();

    private final List<String> mapLines;
    private final Map<TileLocation, String> localKeysByLocation = new HashMap<>();

    private String currentLine;
    private int currentY = 1;
    private int maxX;

    private boolean isTilesRead = false;

    private int keyLength;
    private Pattern keySplit;

    ByondReader(final List<String> mapLines) {
        this.mapLines = mapLines;
    }

    @Override
    public DmmData read() {
        for (String line : mapLines) {
            if (line.isEmpty()) {
                isTilesRead = true;
                continue;
            }

            currentLine = line;
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

        // For consistency reasons Y axis for locations is mirrored, since we read file from top to bottom,
        // while map in BYOND is represented from bottom to top.
        localKeysByLocation.forEach((location, key) -> {
            location.setY(currentY - location.getY());
            dmmData.addTileContentByLocation(location, dmmData.getTileContentByKey(key));
        });

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

        tileContent.setKey(key);

        dmmData.addTileContentByKey(key, tileContent);
        dmmData.addKeyByTileContent(tileContent, key);
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
