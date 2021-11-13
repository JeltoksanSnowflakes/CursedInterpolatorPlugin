/*
 * Copyright (C) 2013 bspkrs
 * Portions Copyright (C) 2013 Alex "immibis" Campbell
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

import immibis.bon.gui.Side;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.TreeMap;

public class CsvFile {
    public final Map<String, CsvData> srgName2CsvData;
    private final File file;
    private final Side side;

    public CsvFile(File file, Side side) throws IOException {
        this.file = file;
        this.side = side;
        this.srgName2CsvData = new TreeMap<>();
        readFromFile();
    }

    public void readFromFile() throws IOException {
        String in = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())), StandardCharsets.UTF_8);
        String[] lines = in.replace("\r", "").split("\n");
        boolean trip = false;
        for (String line : lines) {
            if (!trip) {
                trip = true;
                continue;
            }
            String[] lineParts = line.replace("\"", "").split(",");
            String srgName = lineParts[0];
            String mcpName = lineParts[1];
            String obfName = lineParts[2];
            String pkg = lineParts[7];
            String side = lineParts[8];
            String comment = "";
            if (lineParts.length > 9) {
                comment = lineParts[9];
            }
            if (sideIn(Integer.parseInt(side), this.side.intSide))
                srgName2CsvData.put(srgName, new CsvData(srgName, mcpName, Integer.parseInt(side), comment));
        }
    }

    private boolean sideIn(int i, int[] ar) {
        for (int n : ar)
            if (n == i)
                return true;
        return false;
    }

    public void writeToFile() throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter(file));

        for (CsvData data : this.srgName2CsvData.values())
            out.println(data.toCsv());

        out.close();

    }
}
