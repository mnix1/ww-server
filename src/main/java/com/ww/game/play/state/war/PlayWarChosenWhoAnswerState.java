package com.ww.game.play.state.war;

import com.ww.game.play.PlayManager;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.game.play.command.war.PlayWarSetActiveIndexCommand;
import com.ww.game.play.container.PlayContainer;
import com.ww.game.play.state.PlayState;

public class PlayWarChosenWhoAnswerState extends PlayState {
    private Long profileId;
    private int activeIndex;

    public PlayWarChosenWhoAnswerState(PlayManager manager, Long profileId, int activeIndex) {
        super(manager, RivalStatus.CHOSEN_WHO_ANSWER);
        this.profileId = profileId;
        this.activeIndex = activeIndex;
    }

    @Override
    public void initCommands() {
        commands.add(new PlayWarSetActiveIndexCommand(getContainer(), profileId, activeIndex));
    }

    @Override
    public boolean afterReady() {
        for (RivalTeam team : getContainer().getTeams().getTeams()) {
            WarTeam warTeam = (WarTeam) team;
            if (!warTeam.isChosenActiveIndex()) {
                return false;
            }
        }
        return true;
    }
}
