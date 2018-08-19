package com.ww.service.rival.task.riddle;

import com.ww.model.container.Position;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.ww.helper.FileHelper.*;
import static com.ww.helper.ImageHelper.toBase64;

@Service
public class RiddleService {

    public String generate(String stringPaths) {
        List<BufferedImage> images = prepareImages(stringPaths.split(","));
        BufferedImage bufferedImage = joinImages(images);
        return toBase64(bufferedImage);
    }

    private BufferedImage joinImages(List<BufferedImage> images) {
        int joinedHeight = 360;
        int joinedWidth = 720;
        BufferedImage joined = new BufferedImage(joinedWidth, joinedHeight,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = joined.createGraphics();
        int targetHeight = 180;
        int targetWidth = 180;
        List<Position> positions = preparePositions(images.size(), joinedHeight, joinedWidth, targetHeight, targetWidth);
        for (int i = 0; i < images.size(); i++) {
            Position position = positions.get(i);
            BufferedImage image = images.get(i);
            BufferedImage scaledImage = Scalr.resize(image, Scalr.Method.SPEED, targetWidth, targetHeight);
            int marginHeight = (targetHeight - scaledImage.getHeight()) / 2;
            int marginWidth = (targetWidth - scaledImage.getWidth()) / 2;
            graphics.drawImage(scaledImage, null, position.getX() + marginWidth, position.getY() + marginHeight);
        }
        graphics.dispose();
        return joined;
    }

    private List<Position> preparePositions(int count, int height, int width, int offsetHeight, int offsetWidth) {
        List<Position> positions = new ArrayList<>(count);
        int marginHeight = (height / 2 - offsetHeight);
        int halfCount = (int) Math.ceil(count / 2d);
        for (int i = 0; i < count; i++) {
            int x = (i % halfCount) * (offsetWidth + (width - offsetWidth * halfCount) / halfCount);
            int y = i < halfCount ? marginHeight : height / 2 + marginHeight;
            positions.add(new Position(y, x));
        }
        return positions;
    }

    private List<BufferedImage> prepareImages(String[] paths) {
        List<BufferedImage> images = new ArrayList<>();
        for (String path : paths) {
            String pngPath = path.replace(SVG_EXTENSION, PNG_EXTENSION);
            File file = getResource(pngPath);
            try {
                BufferedImage bi = ImageIO.read(file);
                images.add(bi);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return images;
    }

}
