package com.ww.game.auto.container;

import com.ww.game.GameFlow;
import com.ww.game.member.MemberWisieManager;
import com.ww.game.play.PlayManager;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.constant.wisie.HeroType;
import com.ww.model.constant.wisie.MemberWisieStatus;
import com.ww.model.container.rival.RivalInterval;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WisieTeamMember;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.ww.model.constant.wisie.HeroType.isWisie;

@Getter
public class AutoPlayContainer {
    private PlayManager manager;
    private Profile profile;

    @Setter
    private GameFlow flow;
    @Setter
    private AutoSkillContainer skillContainer;

    public AutoPlayContainer(PlayManager manager, Profile profile) {
        this.manager = manager;
        this.profile = profile;
    }

    public void dispose() {
        if (flow != null) {
            flow.stop();
            setFlow(null);
        }
        if (skillContainer != null) {
            setSkillContainer(null);
        }
    }

    public boolean isBattle() {
        return manager.getContainer().getInit().getType() == RivalType.BATTLE;
    }

    public boolean isWarLike() {
        RivalType type = manager.getContainer().getInit().getType();
        return type == RivalType.WAR || type == RivalType.CAMPAIGN_WAR || type == RivalType.CHALLENGE;
    }

    public RivalTeam team() {
        return manager.getContainer().getTeams().team(profile.getId());
    }

    public WarTeam warTeam() {
        return (WarTeam) team();
    }

    public RivalTeam opponentTeam() {
        return manager.getContainer().getTeams().opponent(profile.getId());
    }

    public HeroType activeMemberType() {
        if (isBattle()) {
            return HeroType.WISOR;
        }
        return warTeam().getActiveTeamMember().getType();
    }

    public WisieTeamMember activeWisieMember() {
        return (WisieTeamMember) warTeam().getActiveTeamMember();
    }

    public List<MemberWisieStatus> activeWisieMemberActions() {
        if (!isWisie(activeMemberType())) {
            return Collections.emptyList();
        }
        Optional<MemberWisieManager> manager = activeWisieMember().currentManager();
        if (manager.isPresent()) {
            return manager.get().getContainer().getActions();
        }
        return Collections.emptyList();
    }

    public HeroType opponentActiveMemberType() {
        if (isBattle()) {
            return HeroType.WISOR;
        }
        return ((WarTeam) opponentTeam()).getActiveTeamMember().getType();
    }

    public Question question() {
        return manager.getContainer().getTasks().question();
    }

    public RivalInterval interval() {
        return manager.getInterval();
    }

    public boolean isMeChoosingTaskProps() {
        return manager.getContainer().findChoosingTaskPropsProfile().equals(profile);
    }
}
