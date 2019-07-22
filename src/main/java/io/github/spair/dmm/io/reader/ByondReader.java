package io.github.spair.dmm.io.reader;

import io.github.spair.dmm.io.DmmData;
import io.github.spair.dmm.io.TileContent;
import io.github.spair.dmm.io.TileObject;
import lombok.val;
import lombok.var;

import java.io.IOException;
import java.util.regex.Pattern;

final class ByondReader extends MapReader {

    private final Pattern objsPattern = Pattern.compile("(,|^)(?=/)(?![^{]*[}])");
    private final Pattern objWithVarPattern = Pattern.compile("^(/.+)\\{(.*)}");
    private final Pattern varsPattern = Pattern.compile(";[ ]?(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

    private int currentY;
    private int maxX;
    private int maxY;

    private boolean isTilesRead = false;
    private boolean isMetaInfoRead = false;

    private int keyLength;
    private Pattern keySplit;

    ByondReader(final OptimizedRandomAccessFile rFile) {
        super(rFile);
    }

    @Override
    public DmmData read() throws IOException {
        String currentLine;

        while ((currentLine = rFile.readLine()) != null) {
            if (currentLine.isEmpty()) {
                isTilesRead = true;
                continue;
            }

            val firstLineChar = currentLine.charAt(0);

            if (firstLineChar == '"' && !isTilesRead) {
                readTileDeclaration(currentLine);
            } else {
                if (!isMetaInfoRead) {
                    long currentPtr = rFile.getFilePointer();
                    readMetaInfo();
                    rFile.seek(currentPtr);
                    isMetaInfoRead = true;
                    continue;
                }

                if (firstLineChar != '(') {
                    if (firstLineChar != '"') {
                        readMapTiles(currentLine);
                    } else {
                        break;
                    }
                }
            }
        }

        dmmData.setDmmSize(maxX, maxY);
        dmmData.setKeyLength(keyLength);

        replaceKeysWithDuplContentForLocations();
        fillDmmDataContent();
        removeKeysWithoutLocation();

        return dmmData;
    }

    private void readMetaInfo() throws IOException {
        var currentLine = rFile.readLine();
        maxX = keySplit.split(currentLine).length;

        var lineCounter = 1;

        while ((currentLine = rFile.readLine()) != null) {
            val firstLineChar = currentLine.charAt(0);

            if (firstLineChar != '(') {
                if (firstLineChar != '"') {
                    lineCounter++;
                } else {
                    break;
                }
            }
        }

        maxY = lineCounter;
        localKeysByLocation = new String[maxY][maxX];
        currentY = maxY - 1;
    }

    private void readTileDeclaration(final String currentLine) {
        if (keyLength == 0) {
            keyLength = currentLine.indexOf('"', 1) - 1;
            keySplit = Pattern.compile(String.format("(?<=\\G.{%d})", keyLength));
        }

        val key = currentLine.substring(1, 1 + keyLength);
        val rawValue = currentLine.substring(currentLine.indexOf('('));
        val tileContent = readTileContent(rawValue.substring(1, rawValue.length() - 1));

        addTileContentOrTraceDuplicateKey(key, tileContent);
    }

    private void readMapTiles(final String currentLine) {
        int x = 0;

        for (String key : keySplit.split(currentLine)) {
            localKeysByLocation[currentY][x++] = key;
        }

        currentY--;
    }

    private TileContent readTileContent(final String contentText) {
        val allObjects = objsPattern.split(contentText);
        val tileContent = new TileContent();

        for (String item : allObjects) {
            val tileObject = new TileObject();
            val itemWithVar = objWithVarPattern.matcher(item);

            if (itemWithVar.find()) {
                tileObject.setType(itemWithVar.group(1));
                val vars = varsPattern.split(itemWithVar.group(2));

                for (String varDef : vars) {
                    varDef = varDef.trim();
                    val varName = varDef.substring(0, varDef.indexOf(' '));
                    val varValue = varDef.substring(varDef.indexOf(' ') + 3);
                    tileObject.putVar(varName, varValue);
                }
            } else {
                tileObject.setType(item);
            }

            tileContent.addTileObject(tileObject);
        }

        tileContent.getTileObjects().sort(tileObjectComparator);

        return tileContent;
    }
}
