package com.fang;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        Map<String, Ad> map = new HashMap<>();
        Crower crower = new Crower();

        try {

            File f = new File("C:\\Users\\Administrator\\IdeaProjects\\SearchAdPlatform\\rawQuery3.txt");

            BufferedReader b = new BufferedReader(new FileReader(f));

            String readLine = "";

            System.out.println("Reading file using Buffered Reader");

            while ((readLine = b.readLine()) != null) {
                if(readLine.length() < 2) {
                    continue;
                }
                String[] quaries = readLine.split(",");
                for(int i=0; i<4; i++) {
                    quaries[i] = quaries[i].trim();
                }
                try{
                    crower.getAmazonProds(quaries[0], Double.parseDouble(quaries[1]), Integer.parseInt(quaries[2]), Integer.parseInt(quaries[3]), map);
                } catch (Exception e) {
                    System.out.print(readLine + "reading file ");
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Object to JSON in file
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("C:\\Users\\Administrator\\IdeaProjects\\SearchAdPlatform\\Ads.json"), map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
