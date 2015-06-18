package Compiler;

import java.util.HashMap;

/**
 *
 * @author Davy
 */
public class SymbolTable {

    private final HashMap localTable;
    private final HashMap globalTable;

    /**
     * Constructor of SymbolTable class. Initialize both localTable and
     * globalTable, allocating HashMaps.
     */
    public SymbolTable() {
        localTable = new HashMap();
        globalTable = new HashMap();
    }

    public Object putInLocal(String key, Object value) {
        return localTable.put(key, value);
    }

    public Object putInGlobal(String key, Object value) {
        return globalTable.put(key, value);
    }

    public Object getInLocal(Object key) {
        return localTable.get(key);
    }

    public Object getInGlobal(Object key) {
        return globalTable.get(key);
    }

    /**
     * Returns the object corresponding to the key. First search in localTable,
     * and if not found, search in globalTable.
     *
     * @param key
     * @return Object
     */
    public Object get(String key) {
        // returns the object corresponding to the key.
        Object result = localTable.get(key);
        if (result != null) {
            // found local identifier
            return result;
        } else {
            // global identifier, if it is in globalTable
            return globalTable.get(key);
        }
    }

    /**
     * Clean localTable.
     */
    public void removeLocalIdent() {
        // remove all local identifiers from the table
        localTable.clear();
    }
}
