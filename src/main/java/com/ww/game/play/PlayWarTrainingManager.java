package com.ww.game.play;

import com.ww.game.training.TrainingManager;
import com.ww.helper.TeamHelper;
import com.ww.model.constant.wisie.WisieType;
import com.ww.model.container.rival.RivalInterval;
import com.ww.model.container.rival.RivalTrainingInterval;
import com.ww.model.container.rival.init.RivalTwoInit;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.skill.WarTeamSkills;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import com.ww.service.rival.war.RivalWarService;
import com.ww.service.wisie.ProfileWisieService;

import java.util.*;

import static com.ww.helper.TeamHelper.isBotProfile;
import static com.ww.websocket.message.MessageDTO.rivalContentMessage;

public class PlayWarTrainingManager extends PlayWarManager {
    private TrainingManager manager;

    public PlayWarTrainingManager(RivalTwoInit init, RivalWarService rivalService) {
        super(init, rivalService);
        this.manager = new TrainingManager(init.getOpponentProfile(), this);
    }

    @Override
    protected RivalInterval prepareInterval() {
        return new RivalTrainingInterval();
    }

    @Override
    protected WarTeam prepareTeam(Profile profile) {
        if (isBotProfile(profile)) {
            Set<WisieType> wisieTypes = new HashSet<>();
            while (wisieTypes.size() < 5) {
                wisieTypes.add(WisieType.random());
            }
            ProfileWisieService wisieService = ((RivalWarService) service).getProfileWisieService();
            List<ProfileWisie> wisies = wisieService.createWisies(profile, new ArrayList<>(wisieTypes));
            long profileWisieId = -1;
            for (ProfileWisie wisie : wisies) {
                wisie.setId(profileWisieId--);
                wisieService.initWisieAttributes(wisie, 0, 1);
            }
            List<TeamMember> teamMembers = TeamHelper.prepareTeamMembers(profile, wisies);
            return new WarTeam(profile, teamMembers, new WarTeamSkills(1, teamMembers));
        }
        return super.prepareTeam(profile);
    }

    @Override
    public void send(Long profileId, Map<String, Object> model) {
        if (isBotProfile(profileId)) {
            manager.getConnection().sendMessage(rivalContentMessage(model));
        } else {
            super.send(profileId, model);
        }
    }
}
