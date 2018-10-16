package com.ww.model.entity.outside.rival.challenge;

import com.ww.model.constant.rival.challenge.ChallengeProfileResponse;
import com.ww.model.constant.rival.challenge.ChallengeProfileType;
import com.ww.model.container.Resources;
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
    private Boolean rewarded = false;
    private Long goldGain;
    private Long crystalGain;
    private Long wisdomGain;
    private Long elixirGain;
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

    public void setGainResources(Resources resources) {
        this.goldGain = resources.getGold();
        this.crystalGain = resources.getCrystal();
        this.wisdomGain = resources.getWisdom();
        this.elixirGain = resources.getElixir();
    }

    public Resources getGainResources() {
        return new Resources(goldGain, crystalGain, wisdomGain, elixirGain);
    }

    public Long responseInterval() {
        if (responseStart == null || responseEnd == null) {
            return null;
        }
        return responseEnd.toEpochMilli() - responseStart.toEpochMilli();
    }

    public void join() {
        joined = true;
        score = 0;
    }
}
