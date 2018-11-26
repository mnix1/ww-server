package com.ww.game.play.state.war;

import com.ww.game.play.PlayManager;
import com.ww.game.play.command.war.PlayWarInitMemberManagerCommand;
import com.ww.game.play.command.war.PlayWarStartMemberManagerCommand;
import com.ww.game.play.container.skill.PlayWarAnsweringFlowContainer;
import com.ww.game.play.state.PlayAnsweringState;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;
import lombok.Getter;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelWisieActions;

public class PlayWarAnsweringState extends PlayAnsweringState {
    @Getter
    private PlayWarAnsweringFlowContainer flowContainer;

    public PlayWarAnsweringState(PlayManager manager) {
        super(manager);
        this.flowContainer = new PlayWarAnsweringFlowContainer();
    }

    @Override
    public void dispose() {
        flowContainer.stopAll();
    }

    @Override
    public void initCommands() {
        super.initCommands();
        commands.add(new PlayWarInitMemberManagerCommand(flowContainer, manager));
        commands.add(new PlayWarStartMemberManagerCommand(getContainer(), flowContainer.getProfileIdWisieFlowMap().values()));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        model.putAll(prepareChildModel(team, opponentTeam));
        return model;
    }

    @Override
    public Map<String, Object> prepareChildModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareChildModel(team, opponentTeam);
        fillModelWisieActions(model, (WarTeam) team, (WarTeam) opponentTeam);
        return model;
    }

    @Override
    public String toString() {
        return super.toString() +
                " flowContainer=" + flowContainer +
                '}';
    }
}
