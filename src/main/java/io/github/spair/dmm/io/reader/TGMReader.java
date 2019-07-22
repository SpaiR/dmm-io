package io.github.spair.dmm.io.reader;

import io.github.spair.dmm.io.DmmData;
import io.github.spair.dmm.io.TileContent;
import io.github.spair.dmm.io.TileObject;
import lombok.val;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

final class TGMReader extends MapReader {

    private final Map<String, TileContent> localTileContentsByKey = new HashMap<>();

    private TileContent currentTileContent;
    private TileObject currentTileObject;

    private int currentX;
    private int currentY;
    private int currentKeyLength;
    private int maxX;
    private int maxY;

    private boolean isTilesRead = false;
    private boolean isMetaInfoRead = false;

    TGMReader(final OptimizedRandomAccessFile rFile) {
        super(rFile);
        this.dmmData.setTgm(true);
    }

    @Override
    public DmmData read() throws IOException {
        String currentLine;

        while ((currentLine = rFile.readLine()) != null) {
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
                        addNewTileContent(currentLine);
                        break;
                    case '/':
                        addNewTileObject(currentLine);
                        break;
                    case '\t':
                        if (currentLine.charAt(1) != '}') {
                            addVarToCurrentObject(currentLine);
                            break;
                        }
                }
            } else {
                if (!isMetaInfoRead) {
                    long currentPtr = rFile.getFilePointer();
                    readMetaInfo();
                    rFile.seek(currentPtr);
                    isMetaInfoRead = true;
                }

                switch (firstLineChar) {
                    case '(':
                        currentX = Integer.parseInt(currentLine.substring(1, currentLine.indexOf(',')));
                        currentY = maxY - 1;
                        break;
                    case '"':
                        break;
                    default:
                        localKeysByLocation[currentY--][currentX - 1] = currentLine;
                }
            }
        }

        dmmData.setDmmSize(maxX, maxY);
        dmmData.setKeyLength(currentKeyLength);

        for (val entry : localTileContentsByKey.entrySet()) {
            val tileContent = entry.getValue();
            tileContent.getTileObjects().sort(tileObjectComparator);
            addTileContentOrTraceDuplicateKey(entry.getKey(), tileContent);
        }

        replaceKeysWithDuplContentForLocations();
        fillDmmDataContent();
        removeKeysWithoutLocation();

        return dmmData;
    }

    private void readMetaInfo() throws IOException {
        String currentLine;

        int xCounter = 0;
        int yCounter = 0;

        while ((currentLine = rFile.readLine()) != null) {
            val firstLineChar = currentLine.charAt(0);

            switch (firstLineChar) {
                case '(':
                    xCounter = Integer.parseInt(currentLine.substring(1, currentLine.indexOf(',')));
                    yCounter = 1;
                    break;
                case '"':
                    break;
                default:
                    yCounter++;
            }
        }

        maxX = xCounter;
        maxY = yCounter - 1;
        localKeysByLocation = new String[maxY][maxX];
    }

    private void addNewTileContent(final String currentLine) {
        if (currentKeyLength == 0) {
            currentKeyLength = currentLine.indexOf('"', 1) - 1;
        }

        val key = currentLine.substring(1, 1 + currentKeyLength);
        currentTileContent = new TileContent();
        localTileContentsByKey.put(key, currentTileContent);
    }

    private void addNewTileObject(final String currentLine) {
        val type = currentLine.substring(0, currentLine.length() - 1);
        currentTileObject = new TileObject(type);
        currentTileContent.addTileObject(currentTileObject);
    }

    private void addVarToCurrentObject(final String currentLine) {
        val varName = currentLine.substring(1, currentLine.indexOf(' '));
        String varValue;

        if (currentLine.charAt(currentLine.length() - 1) == ';') {
            varValue = currentLine.substring(currentLine.indexOf(' ') + 3, currentLine.length() - 1);
        } else {
            varValue  = currentLine.substring(currentLine.indexOf(' ') + 3);
        }

        currentTileObject.putVar(varName, varValue);
    }
}
