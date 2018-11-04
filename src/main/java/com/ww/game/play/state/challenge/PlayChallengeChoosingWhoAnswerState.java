package com.ww.game.play.state.challenge;

import com.ww.game.play.PlayManager;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.battle.BattleTeam;
import com.ww.model.container.rival.challenge.ChallengeTeam;
import com.ww.game.play.command.challenge.PlayChallengeSwapBotTeamCommand;
import com.ww.game.play.container.PlayContainer;
import com.ww.game.play.state.campaign.PlayCampaignChoosingWhoAnswerState;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayBattleModelFiller.fillModelScores;
import static com.ww.game.play.modelfiller.PlayBattleModelFiller.fillModelTaskCount;
import static com.ww.game.play.modelfiller.PlayChallengeModelFiller.fillModelScore;
import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelPresentIndexes;
import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelTeams;

public class PlayChallengeChoosingWhoAnswerState extends PlayCampaignChoosingWhoAnswerState {
    public PlayChallengeChoosingWhoAnswerState(PlayManager manager) {
        super(manager);
    }

    @Override
    public void initCommands() {
        commands.add(new PlayChallengeSwapBotTeamCommand(getContainer()));
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
