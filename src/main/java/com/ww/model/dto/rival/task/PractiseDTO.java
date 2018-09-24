package com.ww.model.dto.rival.task;

import com.ww.model.entity.outside.rival.practise.Practise;
import lombok.Getter;

@Getter
public class PractiseDTO {

    private Long id;
    private TaskDTO question;

    public PractiseDTO(Practise practise, TaskDTO question) {
        this.id = practise.getId();
        this.question = question;
    }
}
