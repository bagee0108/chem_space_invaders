package com.example.chem_project;

import java.io.*;

/**
 * Converts raw collision times ("mm:ss:hh", hundredths of a second) into decimal seconds.
 *
 * Usage: java com.example.chem_project.TimeConversion [inputFile] [outputFile]
 */
public class TimeConversion {
    public static void main(String[] args) {
        int conversionNo = 0;
        String filename = args.length > 0 ? args[0]
                : "chem project/src/com/example/chem_project/rawData/pFast1000x.txt";
        String outputFilename = args.length > 1 ? args[1]
                : "chem project/src/com/example/chem_project/convertedData/pFast1000x_conv.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filename));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                double decimalTime = convertToDecimalTime(line);
                writer.write(String.format("%.2f", decimalTime) + "\n"); // Write decimal time to output file
                conversionNo++;
            }
            System.out.println("Conversion " +conversionNo + " completed. Updated values saved to " + outputFilename);
        } catch (IOException e) {
            System.err.println("Error reading/writing files: " + e.getMessage());
        }
    }

    private static double convertToDecimalTime(String timeString) {
        String[] timeParts = timeString.split(":");
        int minutes = Integer.parseInt(timeParts[0]);
        int seconds = Integer.parseInt(timeParts[1]);
        int milliseconds = Integer.parseInt(timeParts[2]);

        return minutes * 60 + seconds + (double) milliseconds / 100;
    }
}
