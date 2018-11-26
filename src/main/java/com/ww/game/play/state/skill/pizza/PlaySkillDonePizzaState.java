package com.ww.game.play.state.skill.pizza;

import com.ww.game.play.command.skill.PlaySkillUnblockAllCommand;
import com.ww.game.play.flow.skill.PlaySkillFlowOpponent;
import com.ww.game.play.state.skill.PlaySkillOpponentState;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;

import java.util.HashMap;
import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelSkills;

public class PlaySkillDonePizzaState extends PlaySkillOpponentState {
    private Long profileId;
    private WarTeam targetTeam;

    public PlaySkillDonePizzaState(PlaySkillFlowOpponent flow) {
        super(flow);
    }

    @Override
    public void initCommands() {
        commands.add(new PlaySkillUnblockAllCommand(targetTeam));
    }

    @Override
    public void initProps() {
        super.initProps();
        this.profileId = (Long) params.get("profileId");
        this.targetTeam = (WarTeam) manager.getPlayManager().getContainer().getTeams().team(profileId);
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = new HashMap<>();
        fillModelSkills(model, targetTeam, (WarTeam) team, (WarTeam) opponentTeam);
        return model;
    }

    @Override
    public void updateNotify() {
        manager.getPlayManager().getCommunication().prepareModel(this);
    }

    @Override
    public void after(){
        flow.getFlowContainer().runPrevious(profileId);
    }
}
