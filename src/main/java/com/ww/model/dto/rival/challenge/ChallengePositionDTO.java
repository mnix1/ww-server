package com.ww.model.dto.rival.challenge;

import com.ww.model.constant.rival.challenge.ChallengeProfileStatus;
import com.ww.model.dto.social.ProfileDTO;
import com.ww.model.entity.rival.challenge.ChallengeProfile;
import lombok.Getter;

@Getter
public class ChallengePositionDTO {

    private Long position;
    private ProfileDTO profile;
    private Integer score = 0;
    private Long answerInterval;
    private ChallengeProfileStatus status;

    public ChallengePositionDTO(ChallengeProfile challengeProfile) {
        this.profile = new ProfileDTO(challengeProfile.getProfile());
        this.status = challengeProfile.getStatus();
        if (this.status == ChallengeProfileStatus.CLOSED) {
            this.answerInterval = challengeProfile.inProgressInterval();
        }
    }

    public void increaseScore() {
        score++;
    }

}
