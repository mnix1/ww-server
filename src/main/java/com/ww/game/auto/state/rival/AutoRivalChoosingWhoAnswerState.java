package com.ww.game.auto.state.rival;

import com.ww.game.auto.AutoManager;
import com.ww.helper.RandomHelper;
import com.ww.model.constant.Skill;
import com.ww.model.container.MapModel;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.container.rival.war.WarInterval;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WisieTeamMember;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ww.helper.RandomHelper.randomElement;
import static com.ww.helper.TeamHelper.findIndexOfWisieWithHobby;
import static com.ww.service.rival.global.RivalMessageService.CHOOSE_WHO_ANSWER;
import static com.ww.service.rival.global.RivalMessageService.LIFEBUOY;

public class AutoRivalChoosingWhoAnswerState extends AutoRivalState {

    public AutoRivalChoosingWhoAnswerState(AutoManager manager) {
        super(manager);
    }

    @Override
    public void execute() {
        super.execute();
        long interval = RandomHelper.randomLong(1, (long) (((WarInterval) container.interval()).getChoosingWhoAnswerInterval() * 0.75));
        WarTeam team = (WarTeam) container.team();
        maybeUseLifebuoy(interval, team);
        int index = findIndexOfWisieWithHobby(team, container.question().getType().getCategory());
        sendAfter(interval, CHOOSE_WHO_ANSWER, new MapModel("activeIndex", index).get());
    }

    protected void maybeUseLifebuoy(long interval, WarTeam team) {
        if (team.getPresentIndexes().size() > 2 || !team.getTeamSkills().canUse(Skill.LIFEBUOY)) {
            return;
        }
        List<TeamMember> notPresentWisieTeamMembers = team.getTeamMembers().stream().filter(teamMember -> teamMember.isWisie() && !teamMember.isPresent()).collect(Collectors.toList());
        Optional<TeamMember> optionalTeamMember = notPresentWisieTeamMembers.stream()
                .max(Comparator.comparing(teamMember -> ((WisieTeamMember) teamMember).getContent().getValue()));
        TeamMember teamMember = optionalTeamMember.orElse(randomElement(notPresentWisieTeamMembers));
        sendAfter(interval, LIFEBUOY, new MapModel("index", teamMember.getIndex()).get());
    }
}
