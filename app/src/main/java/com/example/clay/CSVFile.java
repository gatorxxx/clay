package com.example.clay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class CSVFile {

    private InputStream inputStream;
    public static Map<String, Verse> result;

    public CSVFile(InputStream inputStream){
        this.inputStream = inputStream;
    }

    public Map<String, Verse> read(){
        result = new HashMap<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {

                // Split a comma-separated string but ignoring commas in double quotes
                String[] row = csvLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                String key = row[0].replaceAll("^\"|\"$", "");
                int book = Integer.parseInt(row[1].replaceAll("^\"|\"$", ""));
                int chapter = Integer.parseInt(row[2].replaceAll("^\"|\"$", ""));
                int verse = Integer.parseInt(row[3].replaceAll("^\"|\"$", ""));
                String content = row[4].replaceAll("^\"|\"$", "");
                Verse v = new Verse(key, book, chapter, verse, content);
                result.put(key, v);
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Error in reading CSV file: " + e);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: " + e);
            }
        }
        return result;
    }
}
