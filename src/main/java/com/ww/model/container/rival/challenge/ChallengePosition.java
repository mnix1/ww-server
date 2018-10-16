package com.ww.model.container.rival.challenge;

import com.ww.model.constant.rival.challenge.ChallengeProfileResponse;
import com.ww.model.dto.social.BaseProfileDTO;
import com.ww.model.entity.outside.rival.challenge.ChallengeProfile;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChallengePosition {

    private Long position;
    private Profile profile;
    private ChallengeProfile challengeProfile;
    private Integer score;
    private Long interval;
    private ChallengeProfileResponse status;

    public ChallengePosition(ChallengeProfile challengeProfile) {
        this.challengeProfile = challengeProfile;
        this.profile = challengeProfile.getProfile();
        this.status = challengeProfile.getResponseStatus();
        this.score = challengeProfile.getScore();
        this.interval = challengeProfile.responseInterval();
    }

}
