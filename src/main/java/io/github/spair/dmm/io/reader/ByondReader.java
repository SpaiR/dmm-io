package io.github.spair.dmm.io.reader;

import io.github.spair.dmm.io.DmmData;
import io.github.spair.dmm.io.TileContent;
import io.github.spair.dmm.io.TileLocation;
import io.github.spair.dmm.io.TileObject;
import lombok.val;

import java.io.BufferedReader;
import java.util.regex.Pattern;

final class ByondReader extends MapReader {

    private final Pattern objsPattern = Pattern.compile("(,|^)(?=/)(?![^{]*[}])");
    private final Pattern objWithVarPattern = Pattern.compile("^(/.+)\\{(.*)}");
    private final Pattern varsPattern = Pattern.compile(";[ ]?(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

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
        val tileContent = readTileContent(rawValue.substring(1, rawValue.length() - 1));

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
