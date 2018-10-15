package com.ww.model.entity.outside.rival.challenge;

import com.ww.model.constant.rival.challenge.ChallengeProfileType;
import com.ww.model.constant.rival.challenge.ChallengeProfileResponse;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class ChallengeProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private ChallengeProfileResponse responseStatus = ChallengeProfileResponse.OPEN;
    private ChallengeProfileType type;
    private Boolean joined = false;
    private Instant responseStart;
    private Instant responseEnd;
    private Integer score;
    @ManyToOne
    @JoinColumn(name = "challenge_id", nullable = false, updatable = false)
    private Challenge challenge;
    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false, updatable = false)
    private Profile profile;

    public ChallengeProfile(Challenge challenge, Profile profile, ChallengeProfileType type) {
        this.challenge = challenge;
        this.profile = profile;
        this.type = type;
    }
}
