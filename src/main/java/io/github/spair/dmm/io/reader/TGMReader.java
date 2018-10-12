package io.github.spair.dmm.io.reader;

import io.github.spair.dmm.io.DmmData;
import io.github.spair.dmm.io.TileContent;
import io.github.spair.dmm.io.TileLocation;
import io.github.spair.dmm.io.TileObject;
import io.github.spair.dmm.io.TileObjectComparator;
import lombok.val;

import java.io.BufferedReader;
import java.util.Map;
import java.util.HashMap;

final class TGMReader extends MapReader {

    private final TileObjectComparator objectComparator = new TileObjectComparator();
    private final Map<String, TileContent> localTileContentsByKey = new HashMap<>();

    private TileContent currentTileContent;
    private TileObject currentTileObject;

    private int currentX;
    private int currentY;
    private int currentKeyLength;

    private boolean isTilesRead = false;

    TGMReader(final BufferedReader bufferedReader) {
        super(bufferedReader);
        this.dmmData.setTgm(true);
    }

    @Override
    public DmmData read() {
        while ((currentLine = readLine()) != null) {
            if (currentLine.isEmpty()) {
                currentTileContent = null;
                currentTileObject = null;
                isTilesRead = true;
                continue;
            }

            val firstLineChar = currentLine.charAt(0);

            if (!isTilesRead) {
                switch (firstLineChar) {
                    case '"':
                        addNewTileContent();
                        break;
                    case '/':
                        addNewTileObject();
                        break;
                    case '\t':
                        if (currentLine.charAt(1) != '}') {
                            addVarToCurrentObject();
                            break;
                        }
                }
            } else {
                switch (firstLineChar) {
                    case '(':
                        currentX = Integer.parseInt(currentLine.substring(1, currentLine.indexOf(',')));
                        currentY = 1;
                        break;
                    case '"':
                        break;
                    default:
                        localKeysByLocation.put(TileLocation.of(currentX, currentY++), currentLine);
                }
            }
        }

        dmmData.setMaxX(currentX);
        dmmData.setMaxY(currentY - 1);

        for (val entry : localTileContentsByKey.entrySet()) {
            val tileContent = entry.getValue();
            tileContent.getTileObjects().sort(objectComparator);
            addTileContentOrTraceDuplicateKey(entry.getKey(), tileContent);
        }

        replaceKeysWithDuplContentForLocations();
        mirrorLocationYAxisAndAddToDmmData();
        removeKeysWithoutLocation();

        return dmmData;
    }

    private void addNewTileContent() {
        if (currentKeyLength == 0) {
            val keyLength = currentLine.indexOf('"', 1) - 1;
            currentKeyLength = keyLength;
            dmmData.setKeyLength(keyLength);
        }

        val key = currentLine.substring(1, 1 + currentKeyLength);
        currentTileContent = new TileContent();
        localTileContentsByKey.put(key, currentTileContent);
    }

    private void addNewTileObject() {
        val type = currentLine.substring(0, currentLine.length() - 1);
        currentTileObject = new TileObject(type);
        currentTileContent.addTileObject(currentTileObject);
    }

    private void addVarToCurrentObject() {
        val varName = currentLine.substring(1, currentLine.indexOf(' '));
        String varValue;

        if (currentLine.charAt(currentLine.length() - 1) == ';') {
            varValue = currentLine.substring(currentLine.indexOf(' ') + 3, currentLine.length() - 1);
        } else {
            varValue  = currentLine.substring(currentLine.indexOf(' ') + 3);
        }

        currentTileObject.addVar(varName, varValue);
    }
}
