package com.ww.model.dto.task;

import com.ww.model.entity.rival.task.Question;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class QuestionDTO {

    private String contentPolish;
    private String contentEnglish;
    private List<AnswerDTO> answers;

    public QuestionDTO(Question question) {
        this.contentPolish = question.getContentPolish();
        this.contentEnglish = question.getContentEnglish();
        this.answers = question.getAnswers().stream().map(AnswerDTO::new).collect(Collectors.toList());
    }
}
