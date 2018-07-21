package com.ww.service.rival.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JsonLoader;
import com.ww.model.constant.rival.task.TaskRenderer;
import com.ww.model.dto.task.QuestionDTO;
import com.ww.model.entity.rival.task.Question;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;

@Service
public class TaskRendererService {

    public QuestionDTO prepareQuestionDTO(Question question) {
        QuestionDTO questionDTO = new QuestionDTO(question);
        if (question.getTaskRenderer() == TaskRenderer.TEXT_IMAGE) {
            String imagePath = retrieveImagePathFromContent(questionDTO.getContentPolish());
            try {
                File file = ResourceUtils.getFile("classpath:" + imagePath);
                String image = IOUtils.toString(new FileInputStream(file), Charset.defaultCharset());
                String encodedImage = Base64.getEncoder().encodeToString(image.getBytes());
                questionDTO.setContentPolish(questionDTO.getContentPolish().replace("imagePath\":\"" + imagePath, "image\":\"" + encodedImage));
                questionDTO.setContentEnglish(questionDTO.getContentEnglish().replace("imagePath\":\"" + imagePath, "image\":\"" + encodedImage));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return questionDTO;
        }
        return questionDTO;
    }

    private String retrieveImagePathFromContent(String content) {
        try {
            JsonNode json = JsonLoader.fromString(content);
            return json.get("imagePath").asText();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String prepareQuestionContentTextImage(String text, String imagePath) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("text", text);
        objectNode.put("imagePath", imagePath);
        try {
            return mapper.writeValueAsString(objectNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
