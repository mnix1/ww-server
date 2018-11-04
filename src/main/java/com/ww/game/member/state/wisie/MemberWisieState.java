package com.ww.game.member.state.wisie;

import com.ww.game.GameState;
import com.ww.game.member.MemberWisieManager;
import com.ww.model.constant.wisie.MemberWisieStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WarWisie;
import lombok.Getter;

import java.util.Map;
import java.util.Optional;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelWisieActions;

public class MemberWisieState extends GameState {
    protected MemberWisieManager manager;
    @Getter
    protected MemberWisieStatus status;
    @Getter
    private WarWisie wisie;

    public MemberWisieState(MemberWisieManager manager, MemberWisieStatus status) {
        this.manager = manager;
        this.status = status;
        this.wisie = manager.getContainer().getMember().getContent();
    }

    protected double hobbyImpact(double interval) {
        return interval / wisie.getHobbyFactor();
    }

    protected Optional<String> afterStateName(){
        return Optional.empty();
    }

}
