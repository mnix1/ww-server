package com.ww.model.entity.outside.rival.challenge;

import com.ww.model.constant.rival.challenge.ChallengeProfileStatus;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class ChallengeProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private ChallengeProfileStatus status = ChallengeProfileStatus.OPEN;
    private Integer score = 0;
    @ManyToOne
    @JoinColumn(name = "challenge_id", nullable = false, updatable = false)
    private Challenge challenge;
    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false, updatable = false)
    private Profile profile;

    public ChallengeProfile(Challenge challenge, Profile profile, ChallengeProfileStatus status) {
        this.challenge = challenge;
        this.profile = profile;
        this.status = status;
    }
}
