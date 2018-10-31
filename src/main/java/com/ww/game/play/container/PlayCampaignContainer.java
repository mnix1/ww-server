package com.ww.game.play.container;

import com.ww.model.container.rival.*;
import com.ww.model.container.rival.init.RivalTwoInit;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.entity.outside.social.Profile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PlayCampaignContainer extends PlayWarContainer {
    public PlayCampaignContainer(RivalTwoInit init, RivalTeams teams, RivalTasks tasks, RivalTimeouts timeouts, RivalDecisions decisions, RivalResult result) {
        super(init, teams, tasks, timeouts, decisions, result);
    }

    @Override
    public boolean isRandomTaskProps() {
        return true;
    }

}
