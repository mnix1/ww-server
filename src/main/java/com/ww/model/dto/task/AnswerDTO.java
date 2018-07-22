package com.ww.model.dto.task;

import com.ww.model.entity.rival.task.Answer;
import lombok.Getter;

@Getter
public class AnswerDTO {

    private Long id;
    private String textContentPolish;
    private String textContentEnglish;

    public AnswerDTO(Answer answer) {
        this.id = answer.getId();
        this.textContentPolish = answer.getTextContentPolish();
        this.textContentEnglish = answer.getTextContentEnglish();
    }
}
