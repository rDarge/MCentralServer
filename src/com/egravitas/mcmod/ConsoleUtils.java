package com.egravitas.mcmod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by mataz on 1/15/2016.
 */
public class ConsoleUtils {

    public static void printConsoleOutput(InputStream is) throws IOException {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(is)))
        {
            String line;
            while(true) {
                line = reader.readLine();

                if (line != null)
                    System.out.println(line);
            }
        }
    }
}
