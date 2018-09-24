package com.ww.model.dto.rival.challenge;

import com.ww.model.dto.social.ProfileDTO;
import com.ww.model.entity.outside.rival.challenge.Challenge;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

@Getter
public class ChallengeInfoDTO {

    private Long id;
    private ProfileDTO creatorProfile;
    private Instant inProgressDate;
    private Boolean canResponse = false;

    public ChallengeInfoDTO(Challenge challenge) {
        this.id = challenge.getId();
        this.creatorProfile = new ProfileDTO(challenge.getCreatorProfile());
        this.inProgressDate = challenge.getInProgressDate();
    }

    public ChallengeInfoDTO(Challenge challenge, Boolean canResponse) {
        this(challenge);
        this.canResponse = canResponse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChallengeInfoDTO that = (ChallengeInfoDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
