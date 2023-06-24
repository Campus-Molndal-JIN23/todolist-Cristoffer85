package org.campusmolndal;

import java.io.FileInputStream;
import java.util.Properties;
    public class KeyHandler {        //Class to read locally stored password to MongoDB, to avoid it being shared public.
        private Properties props;
        private FileInputStream input;
        public KeyHandler(String file){

            props = new Properties();

            String userHome = System.getProperty("user.home");

            try {
                    // SHALL be in this folder on personal computer = Ex: C:\Users\*user*\Documents\Pass
                input = new FileInputStream(userHome + "/Documents/Pass/" + file +".txt");
                    // SHALL be typed in file: --> Pass=<password> <--
                props.load(input);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
        public String getPasscode(){
            return props.getProperty("Pass");  //Reads the password in the file
        }





        //---------Extra method below i needed for testing, don't know why exactly, to be honest... but i tried, for a long long (too long as well...) time just to get this class to be able to be tested. Will probably remove down below method for further program use..

        Properties createProperties() {
            Properties properties = new Properties();
            return properties;
        }
    }