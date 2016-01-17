package com.egravitas.mcmod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by mataz on 1/15/2016.
 */
public class ConsoleUtils {

    public static void printConsoleOutput(InputStream is, List<String> messageLog) throws IOException {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(is)))
        {
            String line;
            while(true) {
                line = reader.readLine();
                messageLog.add(line);

                if (line != null)
                    System.out.println(line);
            }
        }
    }
}
