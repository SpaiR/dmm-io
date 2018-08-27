package io.github.spair.dmm.io.reader;

import io.github.spair.dmm.io.DmmData;
import io.github.spair.dmm.io.TileContent;
import io.github.spair.dmm.io.TileLocation;
import io.github.spair.dmm.io.TileObject;
import lombok.val;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class TGMReader implements MapReader {

    private final DmmData dmmData = new DmmData();

    private final List<String> mapLines;
    private final List<TileContent> localTileContents = new ArrayList<>();
    private final Map<TileLocation, String> localKeysByLocation = new HashMap<>();

    private TileContent currentTileContent;
    private TileObject currentTileObject;

    private String currentLine;
    private int currentX;
    private int currentY;
    private int currentKeyLength;

    private boolean isTilesRead = false;

    TGMReader(final List<String> mapLines) {
        this.mapLines = mapLines;
        this.dmmData.setTgm(true);
    }

    @Override
    public DmmData read() {
        for (int i = 1; i < mapLines.size(); i++) { // First line will be skipped to avoid reading of header.
            currentLine = mapLines.get(i);

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

        localTileContents.forEach(tileContent -> {
            dmmData.addTileContentByKey(tileContent.getKey(), tileContent);
            dmmData.addKeyByTileContent(tileContent, tileContent.getKey());
        });

        // For consistency reasons Y axis for locations is mirrored, since we read file from top to bottom,
        // while map in BYOND is represented from bottom to top.
        localKeysByLocation.forEach((location, key) -> {
            location.setY(currentY - location.getY());
            dmmData.addTileContentByLocation(location, dmmData.getTileContentByKey(key));
        });

        return dmmData;
    }

    private void addNewTileContent() {
        if (currentKeyLength == 0) {
            val keyLength = currentLine.indexOf('"', 1) - 1;
            currentKeyLength = keyLength;
            dmmData.setKeyLength(keyLength);
        }

        val key = currentLine.substring(1, 1 + currentKeyLength);
        currentTileContent = new TileContent(key);
        localTileContents.add(currentTileContent);
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
