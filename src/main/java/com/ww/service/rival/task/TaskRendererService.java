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
            String imagePath = questionDTO.getImageContent();
            try {
                File file = ResourceUtils.getFile("classpath:" + imagePath);
                String image = IOUtils.toString(new FileInputStream(file), Charset.defaultCharset());
                String encodedImage = Base64.getEncoder().encodeToString(image.getBytes());
                questionDTO.setImageContent(encodedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return questionDTO;
        }
        return questionDTO;
    }

}
