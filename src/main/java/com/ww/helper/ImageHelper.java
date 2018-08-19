package com.ww.helper;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.io.IOUtils;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;

import static com.ww.helper.FileHelper.PNG_EXTENSION;
import static com.ww.helper.FileHelper.SVG_EXTENSION;
import static com.ww.helper.FileHelper.getResource;

public class ImageHelper {
    public static void convertSvgToPng(String path, String targetPath) {
        try {
            if (getResource(targetPath) != null) {
                return;
            }
            File file = getResource(path);
            String outputPath = file.getAbsolutePath().replaceAll("\\\\","/").replace(path, targetPath);
            FileInputStream fileInputStream = new FileInputStream(file);
            TranscoderInput svgInput = new TranscoderInput(fileInputStream);
            OutputStream pngOutputStream = new FileOutputStream(outputPath);
            TranscoderOutput pngOutput = new TranscoderOutput(pngOutputStream);
            PNGTranscoder converter = new PNGTranscoder();
            converter.transcode(svgInput, pngOutput);
            pngOutputStream.flush();
            pngOutputStream.close();
            fileInputStream.close();
        } catch (TranscoderException | IOException e) {
            e.printStackTrace();
        }
    }

    public static String toBase64(BufferedImage image) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        OutputStream b64 = new Base64OutputStream(os);
        try {
            ImageIO.write(image, "png", b64);
            return os.toString("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
