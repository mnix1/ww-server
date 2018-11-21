package com.ww.game.auto.state.rival;

import com.ww.game.auto.AutoManager;
import com.ww.game.auto.container.AutoSkillContainer;
import com.ww.model.constant.Skill;
import com.ww.model.constant.wisie.HeroType;
import com.ww.model.constant.wisie.MemberWisieStatus;
import com.ww.model.container.MapModel;
import com.ww.model.container.rival.RivalTeamSkills;
import com.ww.model.container.rival.war.WarTeam;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ww.helper.RandomHelper.randomDouble;
import static com.ww.helper.RandomHelper.randomElement;

public class AutoRivalMaybeSkillState extends AutoRivalState {
    private static List<Skill> withOpponentWisieSkills = Arrays.asList(Skill.NINJA, Skill.WATER_PISTOL, Skill.GHOST, Skill.PIZZA, Skill.HINT, Skill.CHANGE_TASK);
    private static List<Skill> withoutOpponentWisieSkills = Arrays.asList(Skill.HINT, Skill.CHANGE_TASK);

    public AutoRivalMaybeSkillState(AutoManager manager) {
        super(manager);
    }

    @Override
    public boolean stopAfter() {
        return false;
    }

    @Override
    public void execute() {
        super.execute();
        AutoSkillContainer skillContainer = container.getSkillContainer();
        if (skillContainer == null || skillContainer.getUsed().size() >= 1) {
            return;
        }
        List<MemberWisieStatus> wisieActions = container.activeWisieMemberActions();
        if (wisieActions.size() < 2 || randomDouble() < randomDouble(0.5, 1)) {
            return;
        }
        Skill skill = randomElement(skillsCanUse());
        if (skill == null) {
            return;
        }
        skillContainer.getUsed().add(skill);
        long interval = (long) (randomDouble(1, 3) * container.getManager().getInterval().intervalMultiply());
        sendAfter(interval, skill.name(), prepareSkillProps(skill));
    }

    private Map<String, Object> prepareSkillProps(Skill skill) {
        if (skill != Skill.HINT) {
            return new HashMap<>();
        }
        return new MapModel("answerId", container.question().findCorrectAnswerId()).get();
    }

    private List<Skill> possibleSkillsToUse() {
        HeroType opponentHeroType = container.opponentActiveMemberType();
        if (HeroType.isWisie(opponentHeroType)) {
            return withOpponentWisieSkills;
        }
        return withoutOpponentWisieSkills;
    }

    private List<Skill> skillsCanUse() {
        WarTeam warTeam = container.warTeam();
        RivalTeamSkills teamSkills = warTeam.getTeamSkills();
        return possibleSkillsToUse().stream().filter(teamSkills::canUse).collect(Collectors.toList());
    }
}
