package com.ww.play.command.war;

import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.play.command.PlayCommand;
import com.ww.play.container.PlayContainer;

public class PlaySetDefaultActiveIndexCommand extends PlayCommand {

    public PlaySetDefaultActiveIndexCommand(PlayContainer container) {
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
