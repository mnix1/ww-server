package com.ww.service.rival.task;

import com.ww.model.constant.rival.task.TaskRenderer;
import com.ww.model.dto.task.QuestionDTO;
import com.ww.model.entity.rival.task.Question;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;

@Service
public class TaskRendererService {

    public QuestionDTO prepareQuestionDTO(Question question) {
        QuestionDTO questionDTO = new QuestionDTO(question);
        if (question.getTaskRenderer() == TaskRenderer.TEXT_IMAGE) {
            swapImagePathToImageData(questionDTO);
        }
        if (question.getTaskRenderer() == TaskRenderer.TEXT_ANIMATION) {
            swapShapeKeyToShapeData(questionDTO);
            questionDTO.setAnimationContent(encodeData(questionDTO.getAnimationContent()));
        }
        return questionDTO;
    }

    private void swapImagePathToImageData(QuestionDTO questionDTO) {
        String imagePath = questionDTO.getImageContent();
        questionDTO.setImageContent(encodeData(loadImage(imagePath)));
    }

    private String loadImage(String path) {
        try {
            File file = ResourceUtils.getFile("classpath:" + path);
            return IOUtils.toString(new FileInputStream(file), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void swapShapeKeyToShapeData(QuestionDTO questionDTO) {
        String content = questionDTO.getAnimationContent();
        String[] shapes = content.split("shape\":\"");
        for (String shape : shapes) {
            if (shape.indexOf("[") == 0) {
                continue;
            }
            String shapePath = shape.substring(0, shape.indexOf("\""));
            content = content.replace(shapePath, encodeData(loadImage(shapePath)));
        }
        questionDTO.setAnimationContent(content);
    }

    private String encodeData(String s) {
        return Base64.getEncoder().encodeToString(s.getBytes());
    }
}
