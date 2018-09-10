package com.ww.manager.rival.challenge;

import com.ww.manager.rival.war.WarManager;
import com.ww.model.container.rival.RivalInitContainer;
import com.ww.model.container.rival.war.WarContainer;
import com.ww.model.container.rival.war.WarProfileContainer;
import com.ww.model.entity.social.Profile;
import com.ww.model.entity.wisie.ProfileWisie;
import com.ww.service.rival.ChallengeService;
import com.ww.service.social.ProfileConnectionService;

import java.util.List;

public class ChallengeManager extends WarManager {

    public ChallengeManager(RivalInitContainer container, ChallengeService challengeService, ProfileConnectionService profileConnectionService) {
        this.rivalService = challengeService;
        this.profileConnectionService = profileConnectionService;
        Profile creator = container.getCreatorProfile();
        Long creatorId = creator.getId();
        List<ProfileWisie> creatorWisies = challengeService.getProfileWisies(creator);
        this.rivalContainer = new WarContainer();
        this.rivalContainer.storeInformationFromInitContainer(container);
        this.rivalContainer.addProfile(creatorId, new WarProfileContainer(creator, null, prepareTeamMembers(creator, creatorWisies)));
        this.warContainer = (WarContainer) this.rivalContainer;
    }

}
