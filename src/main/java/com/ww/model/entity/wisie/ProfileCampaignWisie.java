package com.ww.model.entity.wisie;


import com.ww.model.entity.rival.campaign.ProfileCampaign;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class ProfileCampaignWisie extends OwnedWisie {
    @ManyToOne
    @JoinColumn(name = "profile_campaign_id", nullable = false, updatable = false)
    private ProfileCampaign profileCampaign;
    private Boolean disabled = false;

    public ProfileCampaignWisie(ProfileCampaign profileCampaign, ProfileWisie profileWisie) {
        super(profileWisie);
        this.profileCampaign = profileCampaign;
        this.inTeam = true;
    }
}
