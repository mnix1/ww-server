package com.ww.helper;

import org.apache.commons.io.IOUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.charset.Charset;

public class FileHelper {

    public static String IMAGE_RESOURCE_DIRECTORY = "task/image/";
    public static String SVG_EXTENSION = ".svg";
    public static String PNG_EXTENSION = ".png";

    //must exist
    public static File getResource(String path) {
        try {
            return ResourceUtils.getFile("classpath:" + path);
        } catch (FileNotFoundException e) {
            System.out.println("resource doesn't exists: " + path);
        }
        return null;
    }

    //don't need to exist
    public static File getResourceFile(String path) {
        try {
            File file = ResourceUtils.getFile("classpath:application.properties");
            return new File(file.getAbsolutePath().replace("application.properties", path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveToFile(String content, String path) {
        try {
            File file = getResourceFile(path);
            new File(file.getAbsolutePath().replace(file.getName(), "")).mkdirs();
            file.createNewFile();
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file));
            osw.write(content);
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(String path) {
        File file = getResource(path);
        if (file == null) {
            return null;
        }
        return readFile(file);
    }

    public static String readFile(File file) {
        try {
            return IOUtils.toString(new FileInputStream(file), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
