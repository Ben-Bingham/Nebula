package ca.benbingham.engine.util;

import java.io.InputStream;
import java.util.Scanner;

public class FileReader {

    public static String readFile(String fileName) throws Exception {
        String result;
        try (InputStream in = FileReader.class.getResourceAsStream(fileName);
             Scanner scanner = new Scanner(in, java.nio.charset.StandardCharsets.UTF_8.name())) {
            result = scanner.useDelimiter("\\A").next();
        }
        return result;
    }
}
