package com.ww.game.play.command.war;

import com.ww.game.play.command.PlayCommand;
import com.ww.game.play.container.PlayContainer;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.skill.PassiveSkillsInit;

public class PlayWarInitPassiveSkillsCommand extends PlayCommand {

    public PlayWarInitPassiveSkillsCommand(PlayContainer container) {
        super(container);
    }

    @Override
    public void execute() {
        for (RivalTeam team : container.getTeams().getTeams()) {
            WarTeam warTeam = (WarTeam) team;
            new PassiveSkillsInit(warTeam).init();
        }
    }
}
