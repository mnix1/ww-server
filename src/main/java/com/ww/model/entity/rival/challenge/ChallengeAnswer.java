package com.ww.model.entity.rival.challenge;

import com.ww.model.constant.rival.challenge.ChallengeAnswerResult;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.social.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class ChallengeAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private ChallengeAnswerResult result = ChallengeAnswerResult.IN_PROGRESS;
    private Instant inProgressDate = Instant.now();
    private Instant closeDate;
    private Integer taskIndex;
    @ManyToOne
    @JoinColumn(name = "challenge_id", nullable = false, updatable = false)
    private Challenge challenge;
    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false, updatable = false)
    private Profile profile;
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false, updatable = false)
    private Question question;

    public Long inProgressInterval() {
        return closeDate.toEpochMilli() - inProgressDate.toEpochMilli();
    }

    public ChallengeAnswer(Challenge challenge, Profile profile, Question question, Integer taskIndex) {
        this.challenge = challenge;
        this.profile = profile;
        this.question = question;
        this.taskIndex = taskIndex;
    }
}
