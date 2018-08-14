package io.github.spair.dmm.io;

import org.junit.Test;

import static org.junit.Assert.*;

public class TileKeyComparatorTest {

    private final TileKeyComparator comparator = new TileKeyComparator();

    @Test
    public void testCompare() {
        assertEquals(0, comparator.compare("aaa", "aaa"));
        assertEquals(-1, comparator.compare("aaa", "aaA"));
        assertEquals(1, comparator.compare("aaA", "aaa"));
    }
}