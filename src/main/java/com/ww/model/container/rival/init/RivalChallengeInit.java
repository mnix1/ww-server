package com.ww.model.container.rival.init;

import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.entity.outside.rival.challenge.ChallengeProfile;
import com.ww.model.entity.outside.rival.challenge.ChallengePhase;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RivalChallengeInit extends RivalOnePlayerInit {
    private ChallengeProfile challengeProfile;
    private List<ChallengePhase> challengePhases;

    public RivalChallengeInit(RivalType type, RivalImportance importance, Profile creatorProfile, ChallengeProfile challengeProfile, List<ChallengePhase> challengePhases) {
        super(type, importance, creatorProfile);
        this.challengeProfile = challengeProfile;
        this.challengePhases = challengePhases;
    }

}
