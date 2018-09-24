package com.ww.model.entity.outside.rival.campaign;

import com.ww.model.constant.book.BookType;
import com.ww.model.constant.rival.campaign.ProfileCampaignStatus;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.ProfileCampaignWisie;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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
    private Long goldGain;
    private Long crystalGain;
    private Long wisdomGain;
    private Long elixirGain;
    private BookType bookGain;
    private Instant openDate = Instant.now();
    private Instant closeDate;
    @ManyToOne
    @JoinColumn(name = "campaign_id", nullable = false, updatable = false)
    private Campaign campaign;
    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false, updatable = false)
    private Profile profile;
    @OneToMany(mappedBy = "profileCampaign", fetch = FetchType.LAZY)
    @OrderBy("ID ASC")
    private List<ProfileCampaignWisie> wisies = new ArrayList<>();

    public ProfileCampaign(Profile profile, Campaign campaign) {
        this.profile = profile;
        this.campaign = campaign;
    }

    public void updateResourceGains() {
        if (phase >= campaign.getPhases()) {
            goldGain = campaign.getGoldGain();
            crystalGain = campaign.getCrystalGain();
            wisdomGain = campaign.getWisdomGain();
            elixirGain = campaign.getElixirGain();
        } else if (phase >= campaign.getPhases() / 2) {
            goldGain = campaign.getGoldGain() / 2;
            crystalGain = campaign.getCrystalGain() / 2;
            wisdomGain = campaign.getWisdomGain() / 2;
            elixirGain = campaign.getElixirGain() / 2;
        }
    }
}
