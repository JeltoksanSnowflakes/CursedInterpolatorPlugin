package net.glasslauncher.cursedinterpolator.objects;

import lombok.Getter;
import net.fabricmc.mappings.EntryTriple;
import org.jetbrains.annotations.Nullable;

public class MappingEntry {

    @Getter
    private final EntryTriple obf;

    @Getter
    private final EntryTriple intermediary;

    @Getter
    private final EntryTriple cursed;

    public MappingEntry(@Nullable EntryTriple obf, @Nullable EntryTriple intermediary, @Nullable EntryTriple cursed) {
        this.obf = obf;
        this.intermediary = intermediary;
        this.cursed = cursed;
    }
}
