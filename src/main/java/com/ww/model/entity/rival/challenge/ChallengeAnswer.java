package com.ww.model.entity.rival.challenge;

import com.ww.model.constant.rival.challenge.ChallengeAnswerResult;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.social.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class ChallengeAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private ChallengeAnswerResult result;
    @ManyToOne
    @JoinColumn(name = "challenge_id", nullable = false, updatable = false)
    private Challenge challenge;
    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false, updatable = false)
    private Profile profile;
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false, updatable = false)
    private Question question;

    public ChallengeAnswer(ChallengeAnswerResult result, Challenge challenge, Profile profile, Question question) {
        this.result = result;
        this.challenge = challenge;
        this.profile = profile;
        this.question = question;
    }
}
