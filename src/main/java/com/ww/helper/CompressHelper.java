package com.ww.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class CompressHelper {
    private static Logger logger = LoggerFactory.getLogger(CompressHelper.class);

    public static byte[] compress(String input) {
        try {
            byte[] data = input.getBytes("UTF-8");
            Deflater deflater = new Deflater();
            deflater.setInput(data);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
            deflater.finish();
            byte[] buffer = new byte[1024];
            while (!deflater.finished()) {
                int count = deflater.deflate(buffer); // returns the generated code... index
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
            byte[] output = outputStream.toByteArray();
            logger.debug("Original: " + data.length / 1024 + " Kb");
            logger.debug("Compressed: " + output.length / 1024 + " Kb");
            return output;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            return input.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    public static String decompress(byte[] data) {
        try {
            Inflater inflater = new Inflater();
            inflater.setInput(data);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
            byte[] buffer = new byte[1024];
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
            byte[] output = outputStream.toByteArray();
            logger.debug("Compressed: " + data.length);
            logger.debug("Original: " + output.length);
            return new String(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
