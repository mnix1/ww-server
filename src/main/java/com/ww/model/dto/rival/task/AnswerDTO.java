package com.ww.model.dto.rival.task;

import com.ww.model.entity.rival.task.Answer;
import lombok.Getter;

@Getter
public class AnswerDTO {

    private Long id;
    private String imageContent;
    private String htmlContent;
    private String textContentPolish;
    private String textContentEnglish;

    public AnswerDTO(Answer answer) {
        this.id = answer.getId();
        this.imageContent = answer.getImageContent();
        this.htmlContent = answer.getHtmlContent();
        this.textContentPolish = answer.getTextContentPolish();
        this.textContentEnglish = answer.getTextContentEnglish();
    }
}
