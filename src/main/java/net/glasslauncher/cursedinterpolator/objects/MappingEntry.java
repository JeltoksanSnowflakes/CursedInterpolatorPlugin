package net.glasslauncher.cursedinterpolator.objects;

import net.fabricmc.mappings.EntryTriple;

public class MappingEntry {

    private EntryTriple obf;
    private EntryTriple intermediary;
    private EntryTriple cursed;

    public MappingEntry(EntryTriple obf, EntryTriple intermediary, EntryTriple cursed) {
        this.obf = obf;
        this.intermediary = intermediary;
        this.cursed = cursed;
    }

    public EntryTriple getObf() {
        return obf;
    }

    public EntryTriple getIntermediary() {
        return intermediary;
    }

    public EntryTriple getCursed() {
        return cursed;
    }
}
