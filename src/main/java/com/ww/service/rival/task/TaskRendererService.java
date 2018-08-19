package com.ww.service.rival.task;

import com.ww.model.constant.rival.task.TaskRenderer;
import com.ww.model.dto.rival.task.TaskDTO;
import com.ww.model.entity.rival.task.Question;
import com.ww.service.rival.task.riddle.RiddleService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;

import static com.ww.model.entity.rival.task.Country.MAP_DIRECTORY;

@Service
public class TaskRendererService {

    @Autowired
    RiddleService riddleService;

    public TaskDTO prepareTaskDTO(Question question) {
        TaskDTO taskDTO = new TaskDTO(question);
        if (question.getType().getRenderer() == TaskRenderer.TEXT_IMAGE_SVG) {
            swapImagePathToImageData(taskDTO);
        }
        if (question.getType().getRenderer() == TaskRenderer.TEXT_IMAGE_PNG) {
            taskDTO.setImageContent(riddleService.generate(taskDTO.getImageContent()));
        }
        if (question.getType().getRenderer() == TaskRenderer.TEXT_ANIMATION) {
            swapShapeKeyToShapeData(taskDTO);
            taskDTO.setAnimationContent(encodeData(taskDTO.getAnimationContent()));
        }
        return taskDTO;
    }

    private void swapImagePathToImageData(TaskDTO taskDTO) {
        String imagePath = taskDTO.getImageContent();
        taskDTO.setImageContent(encodeData(loadImage(imagePath)));
    }

    private String loadImage(String path) {
        try {
            File file = ResourceUtils.getFile("classpath:" + path);
            String image = IOUtils.toString(new FileInputStream(file), Charset.defaultCharset());
            if (path.contains(MAP_DIRECTORY)) {
                return image.replace("<svg", "<svg fill=\"#dadada\"");
            }
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void swapShapeKeyToShapeData(TaskDTO taskDTO) {
        String content = taskDTO.getAnimationContent();
        String[] shapes = content.split("shape\":\"");
        for (String shape : shapes) {
            if (shape.indexOf("[") == 0) {
                continue;
            }
            String shapePath = shape.substring(0, shape.indexOf("\""));
            content = content.replace(shapePath, encodeData(loadImage(shapePath)));
        }
        taskDTO.setAnimationContent(content);
    }

    private String encodeData(String s) {
        return Base64.getEncoder().encodeToString(s.getBytes());
    }
}
