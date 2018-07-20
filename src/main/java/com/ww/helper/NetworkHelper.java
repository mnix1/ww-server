package com.ww.helper;

import org.apache.commons.io.IOUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

public class NetworkHelper {
    public static String downloadContent(String url) {
        try {
            if (url.contains("https")) {
                HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection();
                InputStream is = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                StringBuilder content = new StringBuilder();
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    content.append(inputLine);
                }
                return content.toString();
            } else {
                return IOUtils.toString(new URL(url), Charset.defaultCharset());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
