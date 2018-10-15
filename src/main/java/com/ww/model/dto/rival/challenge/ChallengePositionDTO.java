package com.ww.model.dto.rival.challenge;

import com.ww.model.constant.rival.challenge.ChallengeProfileResponse;
import com.ww.model.dto.social.BaseProfileDTO;
import com.ww.model.entity.outside.rival.challenge.ChallengeProfile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChallengePositionDTO {

    private Long position;
    private BaseProfileDTO profile;
    private Integer score;
    private Long interval;
    private ChallengeProfileResponse status;

    public ChallengePositionDTO(ChallengeProfile challengeProfile) {
        this.profile = new BaseProfileDTO(challengeProfile.getProfile());
        this.status = challengeProfile.getResponseStatus();
        this.score = challengeProfile.getScore();
        this.interval = challengeProfile.responseInterval();
    }

}
