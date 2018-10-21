package com.ww.manager.rival.challenge.state;

import com.ww.helper.TeamHelper;
import com.ww.manager.rival.campaign.state.CampaignWarStateChoosingWhoAnswer;
import com.ww.manager.rival.challenge.ChallengeManager;
import com.ww.manager.rival.war.WarManager;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.entity.outside.rival.challenge.ChallengePhase;
import io.reactivex.Flowable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.ww.helper.TeamHelper.BOT_PROFILE_ID;


public class ChallengeStateChoosingWhoAnswer extends CampaignWarStateChoosingWhoAnswer {

    public ChallengeStateChoosingWhoAnswer(WarManager manager) {
        super(manager);
    }

    @Override
    protected Flowable<Long> processFlowable() {
        WarTeam warTeam = (WarTeam) manager.getTeam(BOT_PROFILE_ID);
        if (warTeam.getPresentIndexes().size() == 0) {
            ChallengePhase challengePhase = ((ChallengeManager) manager).currentPhase();
            warTeam.setTeamMembers(TeamHelper.prepareTeamMembers(Arrays.asList(challengePhase.getPhaseWisie())));
        }
        return super.processFlowable();
    }

    protected void send() {
        manager.getModel().getTeams().forEachTeam(team -> {
            Map<String, Object> model = new HashMap<>();
            manager.getModelFactory().fillModelTeam(model, team);
            manager.getModelFactory().fillModelChoosingWhoAnswer(model, team);
            manager.send(model, manager.getMessageContent(), team.getProfileId());
        });
    }
}
