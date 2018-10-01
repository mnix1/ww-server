package com.ww.model.container.rival.init;

import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.entity.outside.rival.campaign.ProfileCampaign;
import com.ww.model.entity.outside.rival.challenge.ChallengeProfile;
import com.ww.model.entity.outside.rival.challenge.ChallengeQuestion;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RivalChallengeInitContainer extends RivalOnePlayerInitContainer {
    private ChallengeProfile challengeProfile;
    private List<ChallengeQuestion>  challengeQuestions;

    public RivalChallengeInitContainer(RivalType type, RivalImportance importance, Profile creatorProfile, ChallengeProfile challengeProfile, List<ChallengeQuestion> challengeQuestions) {
        super(type, importance, creatorProfile);
        this.challengeProfile = challengeProfile;
        this.challengeQuestions = challengeQuestions;
    }

}
