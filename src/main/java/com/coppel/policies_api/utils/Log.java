package com.coppel.policies_api.utils;

import java.io.FileWriter;
import java.io.IOException;

public class Log {

    public void save(String message) {
        try (
                FileWriter fw = new FileWriter("logs\\policies_api_flow-log.txt", true)) {

            fw.write(message + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
