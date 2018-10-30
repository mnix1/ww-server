package com.ww.play.command.war;

import com.ww.model.container.rival.war.WarTeam;
import com.ww.play.command.PlayCommand;
import com.ww.play.container.PlayContainer;

public class PlaySetActiveIndexCommand extends PlayCommand {
    private Long profileId;
    private int activeIndex;

    public PlaySetActiveIndexCommand(PlayContainer container, Long profileId, int activeIndex) {
        super(container);
        this.profileId = profileId;
        this.activeIndex = activeIndex;
    }

    @Override
    public void execute() {
        WarTeam warTeam = (WarTeam) container.getTeams().team(profileId);
        warTeam.setChosenActiveIndex(true);
        warTeam.setActiveIndex(activeIndex);
    }
}
