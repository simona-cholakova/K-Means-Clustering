package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FileReading {

    public static List<Record> loadRecords(String filePath) {
        List<Record> records = new ArrayList<>();
        String jsonContent = readJsonFile(filePath);  //reading the file

        if (jsonContent != null) {
            try {
                //parse the JSON content into a list of Record objects
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Record>>() {}.getType();
                records = gson.fromJson(jsonContent, listType); //parse into list of Records
            } catch (Exception e) {
                System.err.println("Error parsing JSON: " + e.getMessage());
            }
        }

        return records;
    }

    //helper function to read a JSON file into a String
    private static String readJsonFile(String filePath) {
        StringBuilder jsonContent = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(filePath)) {
            int byteData;
            while ((byteData = fis.read()) != -1) {
                jsonContent.append((char) byteData);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return null;
        }
        return jsonContent.toString();
    }
}
