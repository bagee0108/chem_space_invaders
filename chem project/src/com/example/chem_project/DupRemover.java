package com.example.chem_project;
import java.io.*;
import java.util.*;


/**
 * Writes a copy of the second file with every line that also appears in the first file removed.
 *
 * Usage: java com.example.chem_project.DupRemover [fileToSubtract] [sourceFile] [outputFile]
 */
public class DupRemover {

    public static void main(String[] args) {
        String rawData = "chem project/src/com/example/chem_project/rawData/";
        String filePath1 = args.length > 0 ? args[0] : rawData + "leftHMid.txt";
        String filePath2 = args.length > 1 ? args[1] : rawData + "p_mid o_mid.txt";
        String outputPath = args.length > 2 ? args[2] : rawData + "p_mid o_mid no dupe.txt";

        Set<String> valuesInDoc1 = new HashSet<>();
        List<String> valuesInDoc2 = new ArrayList<>();

        // Read values from the first document
        try (BufferedReader br = new BufferedReader(new FileReader(filePath1))) {
            String line;
            while ((line = br.readLine()) != null) {
                valuesInDoc1.add(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + filePath1 + ": " + e.getMessage());
        }

        // Read values from the second document
        try (BufferedReader br = new BufferedReader(new FileReader(filePath2))) {
            String line;
            while ((line = br.readLine()) != null) {
                valuesInDoc2.add(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + filePath2 + ": " + e.getMessage());
        }

        // Remove values from the second document that are present in the first document
        valuesInDoc2.removeIf(valuesInDoc1::contains);

        // Write the result to the output file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath))) {
            for (String value : valuesInDoc2) {
                bw.write(value);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file " + outputPath + ": " + e.getMessage());
        }

        System.out.println("Values from " + filePath1 + " have been removed from " + filePath2 + ". Output written to " + outputPath);
    }
}
