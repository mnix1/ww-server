package com.ww.manager.rival.campaign.state;

import com.ww.manager.rival.campaign.CampaignWarManager;
import com.ww.manager.rival.war.WarManager;
import com.ww.manager.rival.war.state.WarState;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.war.WarProfileContainer;
import com.ww.model.entity.wisie.OwnedWisie;

import java.util.ArrayList;

import static com.ww.model.constant.wisie.HeroType.isWisie;

public class CampaignWarStateChoosingTaskProps extends WarState {

    protected CampaignWarManager campaignWarManager;

    public CampaignWarStateChoosingTaskProps(CampaignWarManager manager) {
        super(manager);
        this.campaignWarManager = manager;
    }

    @Override
    protected void processVoid() {
        rivalContainer.setStatus(RivalStatus.CHOOSING_TASK_PROPS);
        rivalContainer.increaseCurrentTaskIndex();
        WarProfileContainer warProfileContainer = (WarProfileContainer) warManager.getRivalContainer().getRivalProfileContainer(warManager.getRivalContainer().getOpponentProfile().getId());
        warProfileContainer.setActiveIndex(warProfileContainer.getPresentIndexes().get(0));
        Category category = Category.random();
        DifficultyLevel difficultyLevel = DifficultyLevel.random();
        if (isWisie(warProfileContainer.getActiveTeamMember().getType())) {
            OwnedWisie wisie = (OwnedWisie) warProfileContainer.getActiveTeamMember().getContent();
            category = new ArrayList<>(wisie.getHobbies()).get(0);
            difficultyLevel = campaignWarManager.profileCampaign.getCampaign().getDifficultyLevel();
        }
        rivalManager.prepareTask((long) rivalContainer.getCurrentTaskIndex() + 1, category, difficultyLevel);
    }
}
