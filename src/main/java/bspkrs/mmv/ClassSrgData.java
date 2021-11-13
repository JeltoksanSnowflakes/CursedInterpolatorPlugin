/*
 * Copyright (C) 2013 bspkrs
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package bspkrs.mmv;

public class ClassSrgData implements Comparable<ClassSrgData> {
    public static SortType sortType = SortType.PKG;
    private final String obfName;
    private final String srgName;
    private final String cursedName;
    private final String intermediaryName;
    private final String cursedPkg;
    private final String intermediaryPkg;
    private final boolean isClientOnly;
    private String srgPkgName;

    public ClassSrgData(String obfName, String srgName, String srgPkgName, String intName, String cursedName, String intPkg, String cursedPkg, boolean isClientOnly) {
        this.obfName = obfName;
        this.srgName = srgName;
        this.srgPkgName = srgPkgName;
        this.intermediaryName = intName;
        this.cursedName = cursedName;
        this.intermediaryPkg = intPkg;
        this.cursedPkg = cursedPkg;
        this.isClientOnly = isClientOnly;
    }

    public String getObfName() {
        return this.obfName;
    }

    public String getSrgName() {
        return this.srgName;
    }

    public String getSrgPkgName() {
        return this.srgPkgName;
    }

    public ClassSrgData setSrgPkgName(String pkg) {
        this.srgPkgName = pkg;
        return this;
    }

    public String getCursedName() {
        return cursedName;
    }

    public String getIntermediaryName() {
        return intermediaryName;
    }

    public String getCursedPkg() {
        return cursedPkg;
    }

    public String getIntermediaryPkg() {
        return intermediaryPkg;
    }

    public boolean isClientOnly() {
        return isClientOnly;
    }

    public String getFullyQualifiedSrgName() {
        return srgPkgName + "/" + srgName;
    }

    @Override
    public int compareTo(ClassSrgData o) {
        if (sortType == SortType.PKG)
            if (o != null)
                return getFullyQualifiedSrgName().compareTo(o.getFullyQualifiedSrgName());
            else
                return 1;
        else if (o != null)
            if (obfName.length() != o.obfName.length())
                return obfName.length() - o.obfName.length();
            else
                return obfName.compareTo(o.obfName);
        else
            return 1;

    }

    public boolean contains(String s) {
        return srgName.contains(s) || obfName.contains(s) || this.srgPkgName.contains(s) || cursedName.contains(s) || intermediaryName.contains(s) || cursedPkg.contains(s) || intermediaryPkg.contains(s);
    }

    public enum SortType {
        PKG,
        OBF
    }
}
