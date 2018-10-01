package com.ww.service.rival.task.riddle;

import com.ww.model.container.rival.task.Position;
import org.apache.commons.lang3.StringUtils;
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
public class RiddleClipartService {

    public String generateMulti(String imageContent) {
        String[] stringPaths = imageContent.split(";");
        List<String> images = new ArrayList<>();
        for (String stringPath : stringPaths) {
            images.add(generate(stringPath));
        }
        return StringUtils.join(images, "^_^");
    }

    public String generate(String stringPaths) {
        List<BufferedImage> images = prepareImages(stringPaths.split(","));
        BufferedImage bufferedImage = joinImages(images);
        return toBase64(bufferedImage);
    }

    private BufferedImage joinImages(List<BufferedImage> images) {
        int joinedHeight = 364;
        int joinedWidth = 728;
        BufferedImage joined = new BufferedImage(joinedWidth, joinedHeight,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = joined.createGraphics();
        int targetHeight = 180;
        int targetWidth = 180;
        boolean oneRow = false;
        if (images.size() < 3) {
            targetHeight *= 2;
            targetWidth *= 2;
            oneRow = true;
        } else if (images.size() < 4) {
            targetHeight *= 1.33;
            targetWidth *= 1.33;
            oneRow = true;
        }
        List<Position> positions = preparePositions(images.size(), joinedHeight, joinedWidth, targetHeight, targetWidth, oneRow);
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

    private List<Position> preparePositions(int count, int height, int width, int offsetHeight, int offsetWidth, boolean oneRow) {
        List<Position> positions = new ArrayList<>(count);
        int marginHeight = (height / 2 - offsetHeight);
        int halfCount = (int) Math.ceil(count / (oneRow ? 1d : 2d));
        int marginWidth = (width / halfCount / 2 - offsetHeight / 2);
        for (int i = 0; i < count; i++) {
            int x = (int) (count == 1 ? width / 2 - offsetWidth / 2 : (i % halfCount) * (offsetWidth + marginWidth + (width - offsetWidth * halfCount) / (double) halfCount));
            int y = oneRow ? (height / 2 - offsetHeight / 2) : (i < halfCount ? marginHeight : height / 2 + marginHeight);
            positions.add(new Position(y, x));
        }
        return positions;
    }

    private List<BufferedImage> prepareImages(String[] paths) {
        List<BufferedImage> images = new ArrayList<>();
        for (String path : paths) {
            File file = getResource(path);
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
