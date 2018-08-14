package io.github.spair.dmm.io;

import java.util.Comparator;

public final class TileKeyComparator implements Comparator<String> {

    @Override
    public int compare(final String key1, final String key2) {
        if (key1.equals(key2)) {
            return 0;
        }

        for (int i = 0; i < (Math.min(key1.length(), key2.length())); i++) {
            char c1 = key1.charAt(i);
            char c2 = key2.charAt(i);

            if (Character.isLowerCase(c1) && Character.isUpperCase(c2)) {
                return -1;
            } else if (Character.isUpperCase(c1) && Character.isLowerCase(c2)) {
                return 1;
            } else {
                int charComparison = Character.compare(c1, c2);
                if (charComparison != 0) {
                    return charComparison;
                }
            }
        }

        return 0;
    }
}
