package com.ww.model.entity.outside.rival.challenge;

import com.ww.model.constant.Language;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.entity.outside.rival.task.TaskType;
import com.ww.model.entity.outside.wisie.ChallengePhaseWisie;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class ChallengePhase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "challenge_id", nullable = false, updatable = false)
    private Challenge challenge;
    @ManyToOne
    @JoinColumn(name = "task_type_id", nullable = false, updatable = false)
    private TaskType taskType;
    @ManyToOne
    @JoinColumn(name = "phase_wisie_id", nullable = false, updatable = false)
    private ChallengePhaseWisie phaseWisie;
    private DifficultyLevel difficultyLevel;
    private Language language;

    public ChallengePhase(Challenge challenge, TaskType taskType, DifficultyLevel difficultyLevel, Language language, ChallengePhaseWisie phaseWisie) {
        this.challenge = challenge;
        this.taskType = taskType;
        this.difficultyLevel = difficultyLevel;
        this.language = language;
        this.phaseWisie = phaseWisie;
    }
}
