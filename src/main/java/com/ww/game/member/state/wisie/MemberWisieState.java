package com.ww.game.member.state.wisie;

import com.ww.game.GameState;
import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.play.modelfiller.PlayModelPreparer;
import com.ww.model.constant.wisie.MemberWisieStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WarWisie;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelWisieActions;

public class MemberWisieState extends GameState implements PlayModelPreparer {
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

    @Override
    public void initCommands() {
        commands.add(new MemberWisieAddStatusCommand(manager, status));
    }

    protected double hobbyImpact(double interval) {
        return interval / wisie.getHobbyFactor();
    }

    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = new HashMap<>();
        try {
            fillModelWisieActions(model, manager.getContainer().getTeam(), (WarTeam) team, (WarTeam) opponentTeam);
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
}
