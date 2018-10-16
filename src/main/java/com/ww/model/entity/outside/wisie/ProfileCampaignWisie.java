package com.ww.model.entity.outside.wisie;


import com.ww.model.entity.outside.rival.campaign.ProfileCampaign;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Polymorphism;
import org.hibernate.annotations.PolymorphismType;

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
        this.wisie = profileWisie.wisie;
        this.profileCampaign = profileCampaign;
        this.inTeam = true;
    }
}
