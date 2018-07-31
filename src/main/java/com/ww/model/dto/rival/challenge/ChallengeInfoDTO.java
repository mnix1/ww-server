package com.ww.model.dto.rival.challenge;

import com.ww.model.dto.social.ProfileDTO;
import com.ww.model.entity.rival.challenge.Challenge;
import lombok.Getter;

import java.util.Date;

@Getter
public class ChallengeInfoDTO {

    private Long id;
    private ProfileDTO creatorProfile;
    private Date inProgressDate;

    public ChallengeInfoDTO(Challenge challenge) {
        this.id = challenge.getId();
        this.creatorProfile = new ProfileDTO(challenge.getCreatorProfile());
        this.inProgressDate = challenge.getInProgressDate();
    }
}
