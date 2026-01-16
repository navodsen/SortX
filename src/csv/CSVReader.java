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

        // Find selected column index
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

            // Row shorter than header
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

        // Convert List<Double> to double[]
        double[] numericArray = new double[values.size()];
        for (int i = 0; i < values.size(); i++) {
            numericArray[i] = values.get(i);
        }

        return numericArray;
    }
}
