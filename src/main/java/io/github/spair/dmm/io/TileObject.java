package io.github.spair.dmm.io;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

@Data
@Setter(AccessLevel.NONE)
@NoArgsConstructor
public class TileObject implements Iterable<Map.Entry<String, String>> {

    private String type;
    private Map<String, String> vars = new TreeMap<>();

    public TileObject(final String type) {
        this.type = type;
    }

    public void addVar(final String name, final String value) {
        vars.put(name, value);
    }

    public String getVar(final String name) {
        return vars.get(name);
    }

    public String removeVar(final String name) {
        return vars.remove(name);
    }

    public boolean hasVar(final String name) {
        return vars.containsKey(name);
    }

    public boolean hasVars() {
        return !vars.isEmpty();
    }

    public void setType(final String type) {
        this.type = type;
    }

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        return vars.entrySet().iterator();
    }
}
