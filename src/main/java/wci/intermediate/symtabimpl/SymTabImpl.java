package wci.intermediate.symtabimpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

import wci.intermediate.*;

/**
 * <h1>SymTabImpl</h1>
 *
 * <p>An implementation of the symbol table.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 */
public class SymTabImpl
    extends TreeMap<String, SymTabEntry>
    implements SymTab
{
    private int nestingLevel;       // scope nesting level of this entry
    private int slotNumber;         // local variables array slot number
    private int maxSlotNumber;      // max slot number value

    /**
     * Constructor.
     * @param nestingLevel the nesting level of this entry.
     */
    public SymTabImpl(int nestingLevel)
    {
        this.nestingLevel = nestingLevel;
        this.slotNumber = -1;
        this.maxSlotNumber = 0;
    }

    /**
     * Getter.
     * @return the scope nesting level of this entry.
     */
    @Override
    public int getNestingLevel()
    {
        return nestingLevel;
    }

    /**
     * Create and enter a new entry into the symbol table.
     * @param name the name of the entry.
     * @return the new entry.
     */
    @Override
    public SymTabEntry enter(String name)
    {
        SymTabEntry entry = SymTabFactory.createSymTabEntry(name, this);
        put(name, entry);

        return entry;
    }

    /**
     * Look up an existing symbol table entry.
     * @param name the name of the entry.
     * @return the entry, or null if it does not exist.
     */
    @Override
    public SymTabEntry lookup(String name)
    {
        return get(name);
    }

    /**
     * @return a list of symbol table entries sorted by name.
     */
    @Override
    public ArrayList<SymTabEntry> sortedEntries()
    {
        Collection<SymTabEntry> entries = values();
        Iterator<SymTabEntry> iter = entries.iterator();
        ArrayList<SymTabEntry> list = new ArrayList<SymTabEntry>(size());

        // Iterate over the sorted entries and append them to the list.
        while (iter.hasNext()) {
            list.add(iter.next());
        }

        return list;  // sorted list of entries
    }

    /**
     * @return the next local variables array slot number.
     */
    @Override
    public int nextSlotNumber()
    {
        maxSlotNumber = ++slotNumber;
        return slotNumber;
    }

    /**
     * @return the maximum local variables array slot number.
     */
    @Override
    public int maxSlotNumber()
    {
        return maxSlotNumber;
    }
}
