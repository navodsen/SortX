package csv;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    // Read header row
    public static String[] readHeaders(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String headerLine = reader.readLine();
        reader.close();

        if (headerLine == null || headerLine.isEmpty()) {
            throw new IOException("CSV file is empty");
        }

        return headerLine.split(",");
    }

    // Convert selected column to numeric array
    public static double[] readNumericColumn(File file, String columnName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String headerLine = reader.readLine();
        if (headerLine == null) {
            reader.close();
            throw new IOException("CSV file is empty");
        }

        String[] headers = headerLine.split(",");
        int columnIndex = -1;

        for (int i = 0; i < headers.length; i++) {
            if (headers[i].trim().equals(columnName)) {
                columnIndex = i;
                break;
            }
        }

        if (columnIndex == -1) {
            reader.close();
            throw new IOException("Selected column not found");
        }

        List<Double> values = new ArrayList<>();
        String line;

        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;

            String[] tokens = line.split(",");

            if (tokens.length <= columnIndex) {
                continue;
            }

            String value = tokens[columnIndex].trim();

            if (value.isEmpty()) {
                continue;
            }

            try {
                values.add(Double.parseDouble(value));
            } catch (NumberFormatException e) {
                reader.close();
                throw new IOException("Non-numeric value found: " + value);
            }
        }

        reader.close();

        if (values.isEmpty()) {
            throw new IOException("Selected column contains no numeric data");
        }

        double[] numericArray = new double[values.size()];
        for (int i = 0; i < values.size(); i++) {
            numericArray[i] = values.get(i);
        }

        return numericArray;
    }

    public static List<String> getNumericColumns(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String headerLine = reader.readLine();
        if (headerLine == null) {
            reader.close();
            throw new IOException("CSV file is empty");
        }

        String[] headers = headerLine.split(",");
        int columnCount = headers.length;

        boolean[] isNumeric = new boolean[columnCount];
        for (int i = 0; i < columnCount; i++) {
            isNumeric[i] = true;
        }

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;

            String[] tokens = line.split(",");

            for (int i = 0; i < columnCount; i++) {
                if (!isNumeric[i]) continue;

                if (tokens.length <= i || tokens[i].trim().isEmpty()) {
                    isNumeric[i] = false;
                    continue;
                }

                try {
                    Double.parseDouble(tokens[i].trim());
                } catch (NumberFormatException e) {
                    isNumeric[i] = false;
                }
            }
        }

        reader.close();

        List<String> numericColumns = new ArrayList<>();
        for (int i = 0; i < columnCount; i++) {
            if (isNumeric[i]) {
                numericColumns.add(headers[i].trim());
            }
        }

        if (numericColumns.isEmpty()) {
            throw new IOException("No numeric columns found in CSV file");
        }

        return numericColumns;
    }
}
