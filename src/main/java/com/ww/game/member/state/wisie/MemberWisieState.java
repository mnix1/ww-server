package com.ww.game.member.state.wisie;

import com.ww.game.GameState;
import com.ww.game.member.container.MemberWisieContainer;
import com.ww.model.constant.wisie.MemberWisieStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WarWisie;
import lombok.Getter;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelWisieActions;

public class MemberWisieState extends GameState {
    protected MemberWisieContainer container;
    @Getter
    protected MemberWisieStatus status;
    @Getter
    private WarWisie wisie;

    public MemberWisieState(MemberWisieContainer container, MemberWisieStatus status) {
        this.container = container;
        this.status = status;
        this.wisie = container.getMember().getContent();
    }

    protected double hobbyImpact(double interval) {
        return interval / wisie.getHobbyFactor();
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelWisieActions(model, (WarTeam) team, (WarTeam) opponentTeam);
        return model;
    }

}
