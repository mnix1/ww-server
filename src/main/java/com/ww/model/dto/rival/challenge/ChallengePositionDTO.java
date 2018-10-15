package com.ww.model.dto.rival.challenge;

import com.ww.model.constant.rival.challenge.ChallengeProfileResponse;
import com.ww.model.dto.social.ProfileDTO;
import com.ww.model.entity.outside.rival.challenge.ChallengeProfile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChallengePositionDTO {

    private Long position;
    private ProfileDTO profile;
    private Integer score;
    private ChallengeProfileResponse status;

    public ChallengePositionDTO(ChallengeProfile challengeProfile) {
        this.profile = new ProfileDTO(challengeProfile.getProfile());
        this.score = challengeProfile.getScore();
        this.status = challengeProfile.getResponseStatus();
    }

}
