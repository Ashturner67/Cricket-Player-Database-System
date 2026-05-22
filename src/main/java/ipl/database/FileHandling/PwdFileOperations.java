package ipl.database.FileHandling;

import ipl.database.Players.PlayerDatabase;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class PwdFileOperations {
    private static final String INPUT_FILE_NAME = "password.txt";
    private static final String OUTPUT_FILE_NAME = "password.txt";

    public static HashMap<String, String> readPasswordFile() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_NAME));
        HashMap<String, String> userPasswordMap = new HashMap<>();
        while(true){
            String line = br.readLine();
            if(line == null) break;
            String[] credentials = line.split(",");
            if(credentials.length == 2){
                userPasswordMap.put(credentials[0], credentials[1]);
            }
            else userPasswordMap.put(credentials[0], "");
        }
        br.close();
        return userPasswordMap;
    }

    public static void writePasswordFile(HashMap<String, String> userPasswordMap) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_FILE_NAME));
        for (Map.Entry<String, String> entry : userPasswordMap.entrySet()) {
            String line = entry.getKey() + "," + entry.getValue();
            bw.write(line);
            bw.newLine();
        }

        bw.close();
    }
}
