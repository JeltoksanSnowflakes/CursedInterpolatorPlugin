package net.glasslauncher.cursedinterpolator.objects;

public class ClassMappingEntry {

    private String obfName;
    private String intermediaryName;
    private String cursedName;

    public ClassMappingEntry(String obfName, String intermediaryName, String cursedName) {
        this.obfName = obfName;
        this.intermediaryName = intermediaryName;
        this.cursedName = cursedName;
    }

    public String getObfName() {
        return obfName;
    }

    public String getIntermediaryName() {
        return intermediaryName;
    }

    public String getCursedName() {
        return cursedName;
    }
}
