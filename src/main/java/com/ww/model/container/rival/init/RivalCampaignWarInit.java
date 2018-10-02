package com.ww.model.container.rival.init;

import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.entity.outside.rival.campaign.ProfileCampaign;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RivalCampaignWarInit extends RivalTwoPlayerInit {
    private ProfileCampaign profileCampaign;

    public RivalCampaignWarInit(RivalType type, RivalImportance importance, Profile creatorProfile, Profile opponentProfile, ProfileCampaign profileCampaign) {
        super(type, importance, creatorProfile, opponentProfile);
        this.profileCampaign = profileCampaign;
    }

}
