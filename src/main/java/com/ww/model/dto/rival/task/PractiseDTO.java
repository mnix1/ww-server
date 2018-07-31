package com.ww.model.dto.rival.task;

import com.ww.model.entity.rival.practise.Practise;
import lombok.Getter;

@Getter
public class PractiseDTO {

    private Long id;
    private QuestionDTO question;

    public PractiseDTO(Practise practise, QuestionDTO question) {
        this.id = practise.getId();
        this.question = question;
    }
}
