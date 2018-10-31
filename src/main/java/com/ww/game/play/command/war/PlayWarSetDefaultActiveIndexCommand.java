package com.ww.game.play.command.war;

import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.game.play.command.PlayCommand;
import com.ww.game.play.container.PlayContainer;

public class PlayWarSetDefaultActiveIndexCommand extends PlayCommand {

    public PlayWarSetDefaultActiveIndexCommand(PlayContainer container) {
        super(container);
    }

    @Override
    public void execute() {
        for (RivalTeam team : container.getTeams().getTeams()) {
            WarTeam warTeam = (WarTeam) team;
            warTeam.setActiveIndex(warTeam.getPresentIndexes().get(0));
            warTeam.setChosenActiveIndex(false);
        }
    }
}
