package io.github.spair.dmm.io.reader;

import io.github.spair.dmm.io.TileContent;
import io.github.spair.dmm.io.TileObject;
import lombok.val;

import java.util.regex.Pattern;

final class ByondTileContentReader {

    private final Pattern objsPattern = Pattern.compile("(,|^)(?=/)(?![^{]*[}])");
    private final Pattern objWithVarPattern = Pattern.compile("^(/.+)\\{(.*)}");
    private final Pattern varsPattern = Pattern.compile(";[ ]?(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

    TileContent read(final String contentText) {
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
                    tileObject.addVar(varName, varValue);
                }
            } else {
                tileObject.setType(item);
            }

            tileContent.addTileObject(tileObject);
        }

        return tileContent;
    }
}
