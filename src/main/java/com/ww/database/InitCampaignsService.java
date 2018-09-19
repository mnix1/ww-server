package com.ww.database;

import com.ww.model.constant.rival.campaign.CampaignDestination;
import com.ww.model.constant.rival.campaign.CampaignType;
import com.ww.model.entity.rival.campaign.Campaign;
import com.ww.repository.rival.campaign.CampaignRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Service
public class InitCampaignsService {

    @Autowired
    private CampaignRepository campaignRepository;

    public void initCampaigns() {
        List<Campaign> campaigns = new ArrayList<>();
        campaigns.add(new Campaign(CampaignType.SPACE_EXPEDITION, CampaignDestination.EASY, 1, 0L, 0L, 0L, 0L, 0L, 5L, 10L, 1L));
        campaigns.add(new Campaign(CampaignType.SPACE_EXPEDITION, CampaignDestination.NORMAL, 6, 0L, 0L, 10L, 0L, 0L, 10L, 20L, 2L));
        campaigns.add(new Campaign(CampaignType.SPACE_EXPEDITION, CampaignDestination.HARD, 6, 0L, 0L, 20L, 0L, 0L, 20L, 40L, 4L));
        campaigns.add(new Campaign(CampaignType.UNDERWATER_WORLD, CampaignDestination.EASY, 6, 0L, 0L, 0L, 0L, 10L, 0L, 5L, 1L));
        campaigns.add(new Campaign(CampaignType.UNDERWATER_WORLD, CampaignDestination.NORMAL, 6, 10L, 0L, 0L, 0L, 20L, 0L, 10L, 2L));
        campaigns.add(new Campaign(CampaignType.UNDERWATER_WORLD, CampaignDestination.HARD, 6, 20L, 0L, 0L, 0L, 40L, 0L, 20L, 4L));
        campaigns.add(new Campaign(CampaignType.CELEBRITY_LIFE, CampaignDestination.EASY, 6, 0L, 0L, 0L, 0L, 5L, 10L, 0L, 1L));
        campaigns.add(new Campaign(CampaignType.CELEBRITY_LIFE, CampaignDestination.NORMAL, 6, 0L, 10L, 0L, 0L, 10L, 20L, 0L, 2L));
        campaigns.add(new Campaign(CampaignType.CELEBRITY_LIFE, CampaignDestination.HARD, 6, 0L, 20L, 0L, 0L, 20L, 40L, 0L, 4L));
        campaignRepository.saveAll(campaigns);
    }
}
