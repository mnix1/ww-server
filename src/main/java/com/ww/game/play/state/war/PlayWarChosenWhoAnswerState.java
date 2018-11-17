package com.ww.game.play.state.war;

import com.ww.game.play.PlayManager;
import com.ww.game.play.command.war.PlayWarSetActiveIndexCommand;
import com.ww.game.play.state.PlayState;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;

import java.util.Collections;
import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelActiveIndex;
import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelIsChosenActiveIndex;

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
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        if (!profileId.equals(team.getProfileId())) {
            return Collections.emptyMap();
        }
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelActiveIndex(model, (WarTeam) team);
        fillModelIsChosenActiveIndex(model, (WarTeam) team);
        return model;
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

    @Override
    public void after() {
        manager.getFlow().run("PREPARING_NEXT_TASK");
    }
}
