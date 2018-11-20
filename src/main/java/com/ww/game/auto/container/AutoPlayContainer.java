package com.ww.game.auto.container;

import com.ww.game.play.PlayManager;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.container.rival.RivalInterval;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.social.Profile;

public class AutoPlayContainer {
    private PlayManager manager;
    private Profile profile;

    public AutoPlayContainer(PlayManager manager, Profile profile) {
        this.manager = manager;
        this.profile = profile;
    }

    public boolean isBattle() {
        return manager.getContainer().getInit().getType() == RivalType.BATTLE;
    }

    public boolean isWarLike() {
        RivalType type = manager.getContainer().getInit().getType();
        return type == RivalType.WAR || type == RivalType.CAMPAIGN_WAR || type == RivalType.CHALLENGE;
    }

    public RivalTeam myTeam() {
        return manager.getContainer().getTeams().team(profile.getId());
    }

    public Question question() {
        return manager.getContainer().getTasks().question();
    }

    public RivalInterval interval(){
        return manager.getInterval();
    }

    public boolean isMeChoosingTaskProps() {
        return manager.getContainer().findChoosingTaskPropsProfile().equals(profile);
    }
}
