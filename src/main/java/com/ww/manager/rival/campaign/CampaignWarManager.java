package com.ww.manager.rival.campaign;

import com.ww.manager.rival.war.WarManager;
import com.ww.model.container.rival.RivalInitContainer;
import com.ww.model.container.rival.campaign.CampaignWarContainer;
import com.ww.service.rival.campaign.CampaignWarService;
import com.ww.service.social.ProfileConnectionService;

public class CampaignWarManager extends WarManager {

    public CampaignWarContainer campaignWarContainer;

    public CampaignWarManager(RivalInitContainer container, CampaignWarService campaignWarService, ProfileConnectionService profileConnectionService) {
        super(container, campaignWarService, profileConnectionService);
//        this.rivalService = campaignService;
//        this.profileConnectionService = profileConnectionService;
//        Long creatorId = container.getCreatorProfile().getId();
//        this.rivalContainer = new CampaignWarContainer();
//        this.rivalContainer.storeInformationFromInitContainer(container);
//        this.rivalContainer.addProfile(creatorId, new WarProfileContainer(container.getCreatorProfile(), campaignService.getProfileWisies(creatorId), null));
//        this.warContainer = (WarContainer) this.rivalContainer;
//        this.campaignWarContainer = (CampaignWarContainer) this.rivalContainer;
    }

}
