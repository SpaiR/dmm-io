package io.github.spair.dmm.io.writer;

import io.github.spair.dmm.io.TileContent;
import lombok.val;

final class TileContentStringifier {

    private static final String NEW_LINE = System.lineSeparator();

    static String toByondString(final TileContent tileContent) {
        StringBuilder sb = new StringBuilder('"' + tileContent.getKey() + "\" = (");

        val objIter = tileContent.getTileObjects().iterator();
        while (objIter.hasNext()) {
            val tileObject = objIter.next();

            sb.append(tileObject.getType());

            if (!tileObject.getVars().isEmpty()) {
                sb.append('{');

                val varIter = tileObject.getVars().entrySet().iterator();
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

    static String toTGMString(final TileContent tileContent) {
        StringBuilder sb = new StringBuilder('"' + tileContent.getKey() + "\" = (").append(NEW_LINE);

        val objIter = tileContent.getTileObjects().iterator();
        while (objIter.hasNext()) {
            val tileObject = objIter.next();

            sb.append(tileObject.getType());

            if (!tileObject.getVars().isEmpty()) {
                sb.append('{').append(NEW_LINE);

                val varIter = tileObject.getVars().entrySet().iterator();
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

    private TileContentStringifier() {
    }
}
