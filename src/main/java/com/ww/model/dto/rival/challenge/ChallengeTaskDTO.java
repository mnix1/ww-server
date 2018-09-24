package com.ww.model.dto.rival.challenge;

import com.ww.model.dto.rival.task.TaskDTO;
import com.ww.model.entity.outside.rival.challenge.Challenge;
import lombok.Getter;

@Getter
public class ChallengeTaskDTO {

    private Long id;
    private TaskDTO question;
    private Integer taskIndex;
    private Long score;
    private Integer taskCount;

    public ChallengeTaskDTO(Challenge challenge, TaskDTO question, Integer taskIndex, Long score, Integer taskCount) {
        this.id = challenge.getId();
        this.question = question;
        this.taskIndex = taskIndex;
        this.score = score;
        this.taskCount = taskCount;
    }
}
