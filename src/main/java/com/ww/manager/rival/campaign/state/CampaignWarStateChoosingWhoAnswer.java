package com.ww.manager.rival.campaign.state;

import com.ww.manager.rival.war.WarManager;
import com.ww.manager.rival.war.state.WarStateChoosingWhoAnswer;
import com.ww.model.constant.Category;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WisieTeamMember;

import static com.ww.manager.rival.campaign.CampaignWarManager.isBotProfile;

public class CampaignWarStateChoosingWhoAnswer extends WarStateChoosingWhoAnswer {

    public CampaignWarStateChoosingWhoAnswer(WarManager manager) {
        super(manager);
    }

    protected void setTeamsDefaultActive() {
        for (RivalTeam team : manager.getModel().getTeams().getTeams()) {
            WarTeam warTeam = (WarTeam) team;
            if (isBotProfile(warTeam.getProfile())) {
                Category category = manager.getModel().findCurrentQuestion().getType().getCategory();
                warTeam.setActiveIndex(findActiveIndex(warTeam, category));
                warTeam.setChosenActiveIndex(true);
            } else {
                warTeam.setActiveIndex(warTeam.getPresentIndexes().get(0));
                warTeam.setChosenActiveIndex(false);
            }
        }
    }

    protected int findActiveIndex(WarTeam warTeam, Category category) {
        for (TeamMember teamMember : warTeam.getTeamMembers()) {
            if (!teamMember.isWisie() || !teamMember.isPresent()) {
                continue;
            }
            WisieTeamMember wisieTeamMember = (WisieTeamMember) teamMember;
            if (wisieTeamMember.getContent().getWisie().getHobbies().contains(category)) {
                return wisieTeamMember.getIndex();
            }
        }
        return warTeam.getPresentIndexes().get(0);
    }

}
