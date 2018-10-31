package com.ww.play.state.challenge;

import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.battle.BattleTeam;
import com.ww.model.container.rival.challenge.ChallengeTeam;
import com.ww.play.command.challenge.PlayChallengeSwapBotTeamCommand;
import com.ww.play.container.PlayContainer;
import com.ww.play.state.campaign.PlayCampaignChoosingWhoAnswerState;

import java.util.Map;

import static com.ww.play.modelfiller.PlayBattleModelFiller.fillModelScores;
import static com.ww.play.modelfiller.PlayBattleModelFiller.fillModelTaskCount;
import static com.ww.play.modelfiller.PlayChallengeModelFiller.fillModelScore;
import static com.ww.play.modelfiller.PlayWarModelFiller.fillModelPresentIndexes;
import static com.ww.play.modelfiller.PlayWarModelFiller.fillModelTeams;

public class PlayChallengeChoosingWhoAnswerState extends PlayCampaignChoosingWhoAnswerState {
    public PlayChallengeChoosingWhoAnswerState(PlayContainer container, long interval) {
        super(container, interval);
    }

    @Override
    public void initCommands() {
        commands.add(new PlayChallengeSwapBotTeamCommand(container));
        super.initCommands();
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        ChallengeTeam challengeTeam = (ChallengeTeam) team;
        fillModelScore(model, challengeTeam);
        fillModelTeams(model, challengeTeam, (ChallengeTeam) opponentTeam);
        fillModelPresentIndexes(model, challengeTeam, (ChallengeTeam) opponentTeam);
        return model;
    }

}
