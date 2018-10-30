package com.ww.manager.rival.war;

import com.ww.helper.TeamHelper;
import com.ww.manager.rival.RivalManager;
import com.ww.model.container.rival.RivalModel;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.RivalTeams;
import com.ww.model.container.rival.init.RivalTwoPlayerInit;
import com.ww.model.container.rival.war.*;
import com.ww.model.container.rival.war.skill.WarTeamSkills;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.OwnedWisie;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import com.ww.service.rival.war.RivalWarService;
import com.ww.websocket.message.Message;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.function.BiConsumer;

@NoArgsConstructor
@Getter
public class WarManager extends RivalManager {
    protected WarModel model;
    protected WarModelFactory modelFactory;
    protected WarInterval interval;
    protected WarFlow flow;

    public WarManager(RivalTwoPlayerInit init, RivalWarService rivalService) {
        this.rivalService = rivalService;
        RivalTeams teams = new RivalTeams();
        this.model = new WarModel(init, teams);
        this.modelFactory = new WarModelFactory(this.model);
        this.interval = new WarInterval();
        this.flow = new WarFlow(this);

        Profile creator = init.getCreatorProfile();
        List<ProfileWisie> creatorWisies = rivalService.getProfileWisies(creator);
        List<TeamMember> teamMembers = TeamHelper.prepareTeamMembers(creator, creatorWisies);
        WarTeam creatorTeam = new WarTeam(creator, teamMembers, new WarTeamSkills(1, teamMembers));

        Profile opponent = init.getOpponentProfile();
        List<ProfileWisie> opponentWisies = rivalService.getProfileWisies(opponent);
        List<TeamMember> opponentTeamMembers = TeamHelper.prepareTeamMembers(opponent, opponentWisies);
        WarTeam opponentTeam = new WarTeam(opponent, opponentTeamMembers, new WarTeamSkills(1, opponentTeamMembers));
        teams.addTeams(creatorTeam, opponentTeam);
    }

    @Override
    public boolean isEnd() {
        for (RivalTeam rivalTeam : this.getModel().getTeams().getTeams()) {
            WarTeam warTeam = (WarTeam) rivalTeam;
            if (!warTeam.isAnyPresentMember()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Message getMessageContent() {
        return Message.WAR_CONTENT;
    }

    public void sendModel(List<BiConsumer<Map<String, Object>, ? super RivalTeam>> fillMethods) {
        getModel().getTeams().forEachTeam(rivalTeam -> {
            Map<String, Object> model = new HashMap<>();
            for (BiConsumer<Map<String, Object>, ? super RivalTeam> fillMethod : fillMethods) {
                fillMethod.accept(model, rivalTeam);
            }
            send(model, getMessageContent(), rivalTeam.getProfileId());
        });
    }

    public void sendModel(BiConsumer<Map<String, Object>, ? super RivalTeam>... fillMethods) {
        sendModel(Arrays.asList(fillMethods));
    }

    public void sendActiveMemberAndActionsModel() {
        sendModel((m, wT) -> {
            getModelFactory().fillModelActiveMemberAddOn(m, wT);
            getModelFactory().fillModelWisieActions(m, wT);
        });
    }

    public void sendNewSkillsModel(BiConsumer<Map<String, Object>, ? super RivalTeam>... fillMethods) {
        List<BiConsumer<Map<String, Object>, ? super RivalTeam>> fillMethodList = new ArrayList<>(fillMethods.length + 1);
        fillMethodList.addAll(Arrays.asList(fillMethods));
        fillMethodList.add((m, wT) -> getModelFactory().fillModelSkills(m, wT));
        sendModel(fillMethodList);
    }
}
