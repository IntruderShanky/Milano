package com.intrusoft.milano;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Convert {

    /**
     * To convert the {@link InputStream} into {@link String}
     * @param stream
     * @return
     */
    static public String toString(InputStream stream) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(stream));
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * To convert {@link String} array into {@link ArrayList}
     * @param list
     * @return
     */
    static public String[] toString(ArrayList<String> list) {
        String r[] = new String[list.size()];
        for (int i = 0; i < list.size(); i++)
            r[i] = list.get(i);
        return r;
    }
}
