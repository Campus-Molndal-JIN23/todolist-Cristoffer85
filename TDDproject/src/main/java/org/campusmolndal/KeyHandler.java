package org.campusmolndal;

import java.io.FileInputStream;
import java.util.Properties;
    public class KeyHandler {        //Class to read locally stored password to MongoDB, to avoid it being shared public.
        Properties props;
        KeyHandler(String file){

            props = new Properties();

            String userHome = System.getProperty("user.home");

            try {
                    // SHALL be in this folder on personal computer = Ex: C:\Users\*user*\Documents\Pass
                FileInputStream input = new FileInputStream(userHome + "/Documents/Pass/" + file +".txt");
                    // SHALL be typed in file: --> Pass=<password> <--
                props.load(input);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
        public String getPasscode(){
            return props.getProperty("Pass");  //Reads the password in the file
        }
    }
