package io.github.spair.dmm.io;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.TreeMap;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor
public class TileObject {

    private String type;
    private Map<String, String> vars = new TreeMap<>();

    public TileObject(final String type) {
        this.type = type;
    }

    public void addVar(final String name, final String value) {
        vars.put(name, value);
    }

    public void setType(final String type) {
        this.type = type;
    }
}
