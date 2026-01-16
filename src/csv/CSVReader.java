package csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CSVReader {

    public static String[] readHeaders(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String headerLine = reader.readLine();
        reader.close();

        if (headerLine != null) {
            return headerLine.split(",");
        }
        return new String[0];
    }
}
