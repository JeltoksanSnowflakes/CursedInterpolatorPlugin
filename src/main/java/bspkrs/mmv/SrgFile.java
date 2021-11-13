/*
 * Copyright (C) 2013 Alex "immibis" Campbell, bspkrs
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
 *
 * Modified version of SrgFile.java from BON
 */
package bspkrs.mmv;

import net.fabricmc.mappings.ClassEntry;
import net.fabricmc.mappings.EntryTriple;
import net.fabricmc.mappings.FieldEntry;
import net.fabricmc.mappings.Mappings;
import net.fabricmc.mappings.MethodEntry;
import net.glasslauncher.cursedinterpolator.objects.ClassMappingEntry;
import net.glasslauncher.cursedinterpolator.objects.MappingEntry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class SrgFile {
    public final Map<String, ClassSrgData> srgName2ClassData = new TreeMap<>();            // full/pkg/ClassSrgName -> ClassSrgData
    public final Map<String, Set<ClassSrgData>> srgPkg2ClassDataSet = new TreeMap<>();       // full/pkg -> Set<ClassSrgData>
    public final Map<String, FieldSrgData> srgName2FieldData = new TreeMap<>();            // field_12345_a -> FieldSrgData
    public final Map<String, MethodSrgData> srgName2MethodData = new TreeMap<>();           // func_12345_a -> MethodSrgData
    public final Map<ClassSrgData, Set<MethodSrgData>> class2MethodDataSet = new TreeMap<>();
    public final Map<ClassSrgData, Set<FieldSrgData>> class2FieldDataSet = new TreeMap<>();
    public final Map<String, ClassSrgData> srgMethod2ClassData = new TreeMap<>();            // func_12345_a -> ClassSrgData
    public final Map<String, ClassSrgData> srgField2ClassData = new TreeMap<>();            // field_12345_a -> ClassSrgData

    public SrgFile(File f, Mappings mappings, CsvFile csvFieldData, CsvFile csvMethodData, String side) throws IOException {
        Scanner in = new Scanner(new BufferedReader(new FileReader(f)));

        HashMap<String, MappingEntry> methods = new HashMap<>();
        for (MethodEntry method : mappings.getMethodEntries()) {
            if (method != null) {
                EntryTriple entryObf = method.get(side);
                if (entryObf != null) {
                    EntryTriple entryInt = method.get("intermediary");
                    EntryTriple entryNamed = method.get("named");
                    methods.put(entryObf.getOwner() + "/" + entryObf.getName() + "|" + entryObf.getDesc(), new MappingEntry(entryObf, entryInt, entryNamed));
                }
            }
        }

        HashMap<String, MappingEntry> fields = new HashMap<>();
        for (FieldEntry field : mappings.getFieldEntries()) {
            if (field != null) {
                EntryTriple entryObf = field.get(side);
                if (entryObf != null) {
                    EntryTriple entryInt = field.get("intermediary");
                    EntryTriple entryNamed = field.get("named");
                    fields.put(entryObf.getOwner() + "/" + entryObf.getName(), new MappingEntry(entryObf, entryInt, entryNamed));
                }
            }
        }

        HashMap<String, ClassMappingEntry> classes = new HashMap<>();
        for (ClassEntry clas : mappings.getClassEntries()) {
            if (clas != null) {
                String entryObf = clas.get(side);
                if (entryObf != null) {
                    String entryInt = clas.get("intermediary");
                    String entryNamed = clas.get("named");
                    classes.put(entryObf, new ClassMappingEntry(entryObf, entryInt, entryNamed));
                }
            }
        }

        try {
            while (in.hasNextLine()) {
                if (in.hasNext("CL:")) {
                    // CL: a net/minecraft/util/EnumChatFormatting
                    in.next(); // skip CL:
                    String obf = in.next();
                    String deobf = in.next();
                    String srgName = getLastComponent(deobf);
                    String pkgName = deobf.substring(0, deobf.lastIndexOf('/'));

                    ClassMappingEntry mappingEntry = classes.get(obf);
                    if (mappingEntry == null) {
                        mappingEntry = new ClassMappingEntry("", "", "");
                    }

                    String[] intParts = mappingEntry.getIntermediaryName().split("/");
                    String intPkg = String.join("/", Arrays.copyOf(intParts, intParts.length-1));


                    String[] cursedParts = mappingEntry.getCursedName().split("/");
                    String cursedPkg = String.join("/", Arrays.copyOf(cursedParts, cursedParts.length-1));

                    ClassSrgData classData = new ClassSrgData(obf, srgName, pkgName, intParts[intParts.length-1], cursedParts[cursedParts.length-1], intPkg, cursedPkg, in.hasNext("#C"));

                    if (!srgPkg2ClassDataSet.containsKey(pkgName))
                        srgPkg2ClassDataSet.put(pkgName, new TreeSet<>());
                    srgPkg2ClassDataSet.get(pkgName).add(classData);

                    srgName2ClassData.put(pkgName + "/" + srgName, classData);

                    if (!class2MethodDataSet.containsKey(classData))
                        class2MethodDataSet.put(classData, new TreeSet<>());

                    if (!class2FieldDataSet.containsKey(classData))
                        class2FieldDataSet.put(classData, new TreeSet<>());
                } else if (in.hasNext("FD:")) {
                    // FD: aql/c net/minecraft/block/BlockStoneBrick/field_94408_c #C
                    in.next(); // skip FD:
                    String[] obf = in.next().split("/");
                    String obfOwner = String.join("/", Arrays.copyOf(obf, obf.length - 1));
                    String obfName = obf[obf.length - 1];
                    String deobf = in.next();
                    String srgName = getLastComponent(deobf);
                    String srgPkg = deobf.substring(0, deobf.lastIndexOf('/'));
                    String srgOwner = getLastComponent(srgPkg);
                    srgPkg = srgPkg.substring(0, srgPkg.lastIndexOf('/'));

                    MappingEntry field = fields.get(obfOwner + "/" + obfName);
                    String intName;
                    String cursedName;
                    if (field != null) {
                        intName = field.getIntermediary().getName();
                        cursedName = field.getCursed().getName();
                    }
                    else {
                        intName = "";
                        cursedName = "";
                    }

                    FieldSrgData fieldData = new FieldSrgData(obfOwner, obfName, srgOwner, srgPkg, srgName, intName, cursedName, in.hasNext("#C"));

                    srgName2FieldData.put(srgName, fieldData);
                    class2FieldDataSet.get(srgName2ClassData.get(srgPkg + "/" + srgOwner)).add(fieldData);
                    srgField2ClassData.put(srgName, srgName2ClassData.get(srgPkg + "/" + srgOwner));
                } else if (in.hasNext("MD:")) {
                    // MD: aor/a (Lmt;)V net/minecraft/block/BlockHay/func_94332_a (Lnet/minecraft/client/renderer/texture/IconRegister;)V #C
                    in.next(); // skip MD:
                    String[] obf = in.next().split("/");
                    String obfOwner = String.join("/", Arrays.copyOf(obf, obf.length - 1));
                    String obfName = obf[obf.length - 1];
                    String obfDescriptor = in.next();
                    String deobf = in.next();
                    String srgName = getLastComponent(deobf);
                    String srgPkg = deobf.substring(0, deobf.lastIndexOf('/'));
                    String srgOwner = getLastComponent(srgPkg);
                    srgPkg = srgPkg.substring(0, srgPkg.lastIndexOf('/'));
                    String srgDescriptor = in.next();

                    MappingEntry method = methods.get(obfOwner + "/" + obfName + "|" + obfDescriptor);
                    String intName;
                    String cursedName;
                    String intPkg;
                    String cursedPkg;
                    String intOwner;
                    String cursedOwner;
                    String intDescriptor;
                    String cursedDescriptor;
                    if (method != null) {
                        EntryTriple intermediary = method.getIntermediary();
                        EntryTriple cursed = method.getCursed();

                        intName = intermediary.getName();
                        cursedName = cursed.getName();
                        intOwner = intermediary.getOwner();
                        cursedOwner = cursed.getOwner();
                        intDescriptor = intermediary.getDesc();
                        cursedDescriptor = cursed.getDesc();

                        String[] intParts = intName.split("/");
                        intPkg = String.join("/", Arrays.copyOf(intParts, intParts.length-1));
                        intName = intParts[intParts.length - 1];

                        String[] cursedParts = cursedName.split("/");
                        cursedPkg = String.join("/", Arrays.copyOf(cursedParts, cursedParts.length-1));
                        cursedName = cursedParts[cursedParts.length - 1];
                    }
                    else {
                        intName = "";
                        cursedName = "";
                        intPkg = "";
                        cursedPkg = "";
                        intOwner = "";
                        cursedOwner = "";
                        intDescriptor = "";
                        cursedDescriptor = "";
                    }

                    MethodSrgData methodData = new MethodSrgData(obfOwner, obfName, obfDescriptor, srgOwner, srgPkg, srgName, intName, cursedName, intPkg, intOwner, intDescriptor, cursedPkg, cursedOwner, cursedDescriptor, srgDescriptor, in.hasNext("#C"));

                    srgName2MethodData.put(srgName, methodData);
                    class2MethodDataSet.get(srgName2ClassData.get(srgPkg + "/" + srgOwner)).add(methodData);
                    srgMethod2ClassData.put(srgName, srgName2ClassData.get(srgPkg + "/" + srgOwner));
                } else
                    in.nextLine();
            }
        } finally {
            in.close();
        }
    }

    public static String getLastComponent(String s) {
        String[] parts = s.split("/");
        return parts[parts.length - 1];
    }
}
