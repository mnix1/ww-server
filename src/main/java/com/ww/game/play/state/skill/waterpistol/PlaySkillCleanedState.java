package com.ww.game.play.state.skill.waterpistol;

import com.ww.game.member.command.MemberWisieRemoveDisguiseCommand;
import com.ww.game.play.command.skill.PlaySkillUnblockAllCommand;
import com.ww.game.play.flow.skill.PlaySkillFlowOpponent;
import com.ww.game.play.state.skill.PlaySkillOpponentState;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;

import java.util.HashMap;
import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelActiveMemberAddOns;
import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelSkills;

public class PlaySkillCleanedState extends PlaySkillOpponentState {

    public PlaySkillCleanedState(PlaySkillFlowOpponent flow) {
        super(flow);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieRemoveDisguiseCommand(opponentManager));
        commands.add(new PlaySkillUnblockAllCommand(opponentWarTeam));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = new HashMap<>();
        fillModelSkills(model, opponentWarTeam, (WarTeam) team, (WarTeam) opponentTeam);
        fillModelActiveMemberAddOns(model, opponentWarTeam, (WarTeam) team, (WarTeam) opponentTeam);
        return model;
    }

    @Override
    public void after() {
        flow.getFlowContainer().runPrevious(opponentWarTeam.getProfileId());
    }
}
