package io.github.spair.dmm.io;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor
public class TileObject {

    private String type;
    private Map<String, String> vars = new LinkedHashMap<>();

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
