package com.ww.model.entity.outside.rival.challenge;

import com.ww.model.constant.rival.challenge.ChallengeAccess;
import com.ww.model.constant.rival.challenge.ChallengeApproach;
import com.ww.model.constant.rival.challenge.ChallengeStatus;
import com.ww.model.constant.rival.challenge.ChallengeType;
import com.ww.model.container.Resources;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "creator_profile_id", updatable = false)
    private Profile creatorProfile;
    private ChallengeType type = ChallengeType.PRIVATE;
    private ChallengeStatus status = ChallengeStatus.IN_PROGRESS;
    private Long participants = 0L;
    private ChallengeAccess access;
    private ChallengeApproach approach;
    private Long goldCost;
    private Long crystalCost;
    private Long wisdomCost;
    private Long elixirCost;
    private Long goldGain;
    private Long crystalGain;
    private Long wisdomGain;
    private Long elixirGain;
    private Instant creationDate = Instant.now();
    private Instant closeDate;
    private Instant timeoutDate;

    @OneToMany(mappedBy = "challenge", fetch = FetchType.LAZY)
    private Set<ChallengeProfile> profiles = new HashSet<>();

    @OneToMany(mappedBy = "challenge", fetch = FetchType.LAZY)
    private Set<ChallengePhase> phases;

    public Challenge(Profile creatorProfile, ChallengeType type, ChallengeAccess access, ChallengeApproach approach, Resources resources, Integer duration) {
        setCostResources(resources);
        this.creatorProfile = creatorProfile;
        this.type = type;
        this.access = access;
        this.approach = approach;
        this.timeoutDate = creationDate.plus(duration, ChronoUnit.HOURS);
    }

    public Challenge(ChallengeType type, ChallengeAccess access, ChallengeApproach approach, Resources resources, Instant timeoutDate) {
        setCostResources(resources);
        this.type = type;
        this.access = access;
        this.approach = approach;
        this.timeoutDate = timeoutDate;
    }

    private void setCostResources(Resources resources) {
        this.goldCost = resources.getGold();
        this.crystalCost = resources.getCrystal();
        this.wisdomCost = resources.getWisdom();
        this.elixirCost = resources.getElixir();
    }

    public void setGainResources(Resources resources) {
        this.goldGain = resources.getGold();
        this.crystalGain = resources.getCrystal();
        this.wisdomGain = resources.getWisdom();
        this.elixirGain = resources.getElixir();
    }

    public Resources getCostResources() {
        return new Resources(goldCost, crystalCost, wisdomCost, elixirCost);
    }

    public Resources getGainResources() {
        return new Resources(goldGain, crystalGain, wisdomGain, elixirGain);
    }

    public Long getTimeoutInterval() {
        return timeoutDate.toEpochMilli() - Instant.now().toEpochMilli();
    }

    public void joined() {
        participants++;
    }
}
