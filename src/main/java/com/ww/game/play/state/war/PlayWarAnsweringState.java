package com.ww.game.play.state.war;

import com.ww.game.play.PlayWarManager;
import com.ww.game.play.command.war.PlayWarInitMemberManagerCommand;
import com.ww.game.play.command.war.PlayWarStartMemberManagerCommand;
import com.ww.game.play.state.PlayAnsweringState;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WisieTeamMember;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelWisieActions;

public class PlayWarAnsweringState extends PlayAnsweringState {
    private PlayWarManager manager;

    public PlayWarAnsweringState(PlayWarManager manager, long interval) {
        super(manager.getContainer(), interval);
        this.manager = manager;
    }

    @Override
    public void initCommands() {
        super.initCommands();
        commands.add(new PlayWarInitMemberManagerCommand(manager));
        commands.add(new PlayWarStartMemberManagerCommand(getContainer()));
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
}
