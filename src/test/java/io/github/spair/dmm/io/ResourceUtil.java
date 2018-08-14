package io.github.spair.dmm.io;

import java.io.File;
import java.util.Objects;

public class ResourceUtil {
    public static File readResourceFile(final String path) {
        return new File(Objects.requireNonNull(ResourceUtil.class.getClassLoader().getResource(path)).getFile());
    }
}
