package com.ww.model.dto.task;

import com.ww.model.entity.rival.task.Answer;
import com.ww.model.entity.rival.task.Question;
import lombok.Getter;

@Getter
public class AnswerDTO {

    private Long id;
    private String contentPolish;
    private String contentEnglish;

    public AnswerDTO(Answer answer) {
        this.id = answer.getId();
        this.contentPolish = answer.getContentPolish();
        this.contentEnglish = answer.getContentEnglish();
    }
}
