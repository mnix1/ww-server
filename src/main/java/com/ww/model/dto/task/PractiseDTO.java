package com.ww.model.dto.task;

import com.ww.model.entity.rival.practise.Practise;
import com.ww.model.entity.rival.task.Question;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PractiseDTO {

    private Long id;
    private QuestionDTO question;

    public PractiseDTO(Practise practise, QuestionDTO question) {
        this.id = practise.getId();
        this.question = question;
    }
}
