package com.ww.manager.rival.campaign;

import com.ww.manager.rival.war.WarManager;
import com.ww.model.container.rival.RivalInitContainer;
import com.ww.model.container.rival.campaign.CampaignContainer;
import com.ww.model.container.rival.war.WarContainer;
import com.ww.model.container.rival.war.WarProfileContainer;
import com.ww.service.rival.campaign.CampaignService;
import com.ww.service.social.ProfileConnectionService;

public class CampaignManager extends WarManager {

    public CampaignContainer campaignContainer;

    public CampaignManager(RivalInitContainer container, CampaignService campaignService, ProfileConnectionService profileConnectionService) {
        super(container, campaignService, profileConnectionService);
//        this.rivalService = campaignService;
//        this.profileConnectionService = profileConnectionService;
//        Long creatorId = container.getCreatorProfile().getId();
//        this.rivalContainer = new CampaignContainer();
//        this.rivalContainer.storeInformationFromInitContainer(container);
//        this.rivalContainer.addProfile(creatorId, new WarProfileContainer(container.getCreatorProfile(), campaignService.getProfileWisies(creatorId), null));
//        this.warContainer = (WarContainer) this.rivalContainer;
//        this.campaignContainer = (CampaignContainer) this.rivalContainer;
    }

}
