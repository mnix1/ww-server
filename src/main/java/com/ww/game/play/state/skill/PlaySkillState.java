package com.ww.game.play.state.skill;

import com.ww.game.GameState;
import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.container.MemberWisieContainer;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.RivalTeams;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WarWisie;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelWisieActions;
import static com.ww.helper.RandomHelper.randomDouble;

public class PlaySkillState extends GameState {
    protected PlaySkillFlow flow;
    protected MemberWisieManager manager;
    @Getter
    private WarWisie wisie;
    protected long interval;

    public PlaySkillState(PlaySkillFlow flow, MemberWisieManager manager) {
        this.flow = flow;
        this.manager = manager;
        this.wisie = manager.getContainer().getMember().getContent();
    }

    protected MemberWisieContainer getContainer() {
        return manager.getContainer();
    }

    protected double hobbyImpact(double interval) {
        return interval / wisie.getHobbyFactor();
    }

    protected long intervalMultiply() {
        return 1000;
    }

    protected double minInterval() {
        return 0;
    }

    protected double maxInterval() {
        return 0;
    }

    protected double prepareInterval() {
        return randomDouble(minInterval(), maxInterval());
    }

    @Override
    public void execute(){
        interval = (long) (prepareInterval() * intervalMultiply());
        super.execute();
    }

    @Override
    public boolean stopAfter() {
        return false;
    }

    @Override
    public long afterInterval() {
        return interval;
    }

    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = new HashMap<>();
        fillModelWisieActions(model, (WarTeam) team, (WarTeam) opponentTeam);
        return model;
    }

    @Override
    public void updateNotify() {
        RivalTeams teams = manager.getPlayManager().getContainer().getTeams();
        teams.forEachTeam(team -> {
            manager.getPlayManager().getCommunication().sendAndUpdateModel(team.getProfileId(), this.prepareModel(team, teams.opponent(team)));
        });
    }

    @Override
    public String toString() {
        return super.toString() + ", " + manager.getContainer().getMember();
    }
}
