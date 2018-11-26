package com.ww.game.play.state.skill;

import com.ww.game.GameState;
import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.container.MemberWisieContainer;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.modelfiller.PlayModelPreparer;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WarWisie;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelWisieActions;
import static com.ww.helper.RandomHelper.randomDouble;

public class PlaySkillState extends GameState implements PlayModelPreparer {
    protected PlaySkillFlow flow;
    protected MemberWisieManager manager;
    @Getter
    private WarWisie wisie;
    protected WarTeam warTeam;
    protected long interval;

    public PlaySkillState(PlaySkillFlow flow) {
        this.flow = flow;
        this.manager = flow.getManager();
        this.wisie = manager.getContainer().getMember().getContent();
        this.warTeam = manager.getContainer().getTeam();
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
    public void execute() {
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

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        return prepareModel(null, team, opponentTeam);
    }

    public Map<String, Object> prepareModel(WarTeam changedTeam, RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = new HashMap<>();
        try {
            fillModelWisieActions(model, changedTeam, (WarTeam) team, (WarTeam) opponentTeam);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(toString() + ", " + manager.getPlayManager().toString());
        }
        return model;
    }

    @Override
    public void updateNotify() {
        manager.getPlayManager().getCommunication().prepareModel(this);
        manager.getPlayManager().getCommunication().sendPreparedModel();
    }

    @Override
    public String toString() {
        return super.toString() + ", " + manager.getContainer().getMember();
    }
}
