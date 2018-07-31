package com.ww.model.dto.rival.challenge;

import com.ww.model.dto.rival.task.QuestionDTO;
import com.ww.model.entity.rival.challenge.Challenge;
import lombok.Getter;

import java.util.List;

@Getter
public class ChallengeTaskDTO {

    private Long id;
    private List<QuestionDTO> questions;

    public ChallengeTaskDTO(Challenge challenge, List<QuestionDTO> questions) {
        this.id = challenge.getId();
        this.questions = questions;
    }
}
