package com.ww.model.dto.rival.challenge;

import com.ww.model.constant.rival.challenge.ChallengeProfileResponse;
import com.ww.model.container.Resources;
import com.ww.model.container.rival.challenge.ChallengePosition;
import com.ww.model.dto.social.BaseProfileDTO;
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
    private Resources reward;

    public ChallengePositionDTO(ChallengePosition challengePosition) {
        this.position = challengePosition.getPosition();
        this.profile = new BaseProfileDTO(challengePosition.getProfile());
        this.status = challengePosition.getStatus();
        this.score = challengePosition.getScore();
        this.interval = challengePosition.getInterval();
        this.reward = challengePosition.getChallengeProfile().getGainResources();
    }

}
