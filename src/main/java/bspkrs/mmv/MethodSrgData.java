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

public class MethodSrgData implements Comparable<MethodSrgData> {
    private final String obfOwner;
    private final String obfName;
    private final String obfDescriptor;
    private final String srgOwner;
    private final String srgPkg;
    private final String srgName;
    private final String srgDescriptor;
    private final String cursedName;
    private final String intermediaryName;
    private final String cursedPkg;
    private final String intermediaryPkg;
    private final String cursedOwner;
    private final String intermediaryOwner;
    private final String cursedDescriptor;
    private final String intermediaryDescriptor;
    private final boolean isClientOnly;

    public MethodSrgData(String obfOwner, String obfName, String obfDescriptor, String srgOwner, String srgPkg, String srgName, String intermediaryName, String cursedName, String intPkg, String intOwner, String intDescriptor, String cursedPkg, String cursedOwner, String cursedDescriptor, String srgDescriptor, boolean isClientOnly) {
        this.obfOwner = obfOwner;
        this.obfName = obfName;
        this.obfDescriptor = obfDescriptor;
        this.srgOwner = srgOwner;
        this.srgPkg = srgPkg;
        this.srgName = srgName;
        this.intermediaryName = intermediaryName;
        this.cursedName = cursedName;
        this.srgDescriptor = srgDescriptor;
        this.isClientOnly = isClientOnly;
        this.intermediaryPkg = intPkg;
        this.intermediaryOwner = intOwner;
        this.intermediaryDescriptor = intDescriptor;
        this.cursedPkg = cursedPkg;
        this.cursedOwner = cursedOwner;
        this.cursedDescriptor = cursedDescriptor;
    }

    public String getObfOwner() {
        return obfOwner;
    }

    public String getObfName() {
        return obfName;
    }

    public String getObfDescriptor() {
        return obfDescriptor;
    }

    public String getSrgOwner() {
        return srgOwner;
    }

    public String getSrgName() {
        return srgName;
    }

    public String getSrgDescriptor() {
        return srgDescriptor;
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

    public String getCursedOwner() {
        return cursedOwner;
    }

    public String getIntermediaryOwner() {
        return intermediaryOwner;
    }

    public String getCursedDescriptor() {
        return cursedDescriptor;
    }

    public String getIntermediaryDescriptor() {
        return intermediaryDescriptor;
    }

    public boolean isClientOnly() {
        return isClientOnly;
    }

    public String getSrgPkg() {
        return srgPkg;
    }

    @Override
    public int compareTo(MethodSrgData o) {
        if (o != null)
            return srgName.compareTo(o.srgName);
        else
            return 1;
    }

    public boolean contains(String s) {
        return srgName.contains(s) || obfName.contains(s) || cursedName.contains(s) || intermediaryName.contains(s);
    }
}
