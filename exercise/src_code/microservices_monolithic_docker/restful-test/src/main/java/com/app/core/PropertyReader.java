package com.app.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {

    public Properties readProperty() {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            String filename = "src/config.properties";
            try {
                input = new FileInputStream(filename);
                if (input == null) {
                    System.out.println("Sorry, unable to find " + filename);
                    return null;
                }
            } catch (Exception ex) {
                System.err.println("Error in loading property file: " + ex);
            }
            prop.load(input);
        } catch (IOException ex) {
            System.err.println("Error in loading property file: " + ex);
        } finally{
            if(input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    System.err.println("Error in loading property file: " + e);
                }
            }
        }
        return prop;
    }

}
