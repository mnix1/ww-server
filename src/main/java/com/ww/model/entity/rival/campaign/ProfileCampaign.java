package com.ww.model.entity.rival.campaign;

import com.ww.model.constant.rival.campaign.ProfileCampaignStatus;
import com.ww.model.constant.rival.challenge.ChallengeProfileStatus;
import com.ww.model.entity.rival.challenge.Challenge;
import com.ww.model.entity.social.Profile;
import com.ww.model.entity.wisie.ProfileCampaignWisie;
import com.ww.model.entity.wisie.ProfileWisie;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class ProfileCampaign {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private ProfileCampaignStatus status = ProfileCampaignStatus.IN_PROGRESS;
    private Integer phase = 0;
    private Boolean present = true;
    @ManyToOne
    @JoinColumn(name = "campaign_id", nullable = false, updatable = false)
    private Campaign campaign;
    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false, updatable = false)
    private Profile profile;
    @OneToMany(mappedBy = "profileCampaign", fetch = FetchType.LAZY)
    private Set<ProfileCampaignWisie> wisies = new HashSet<>();

    public ProfileCampaign(Profile profile, Campaign campaign) {
        this.profile = profile;
        this.campaign = campaign;
    }
}
