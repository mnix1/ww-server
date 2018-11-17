package com.ww.game.play.state.skill.lifebuoy;

import com.ww.game.play.PlayManager;
import com.ww.game.play.command.skill.PlaySkillEnableTeamMemberCommand;
import com.ww.game.play.command.skill.PlaySkillUseCommand;
import com.ww.game.play.state.skill.PlaySkillActionState;
import com.ww.model.constant.Skill;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.RivalTeams;
import com.ww.model.container.rival.war.WarTeam;

import java.util.HashMap;
import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelPresentIndexes;
import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelSkills;

public class PlaySkillLifebuoyActionState extends PlaySkillActionState {
    protected WarTeam warTeam;
    protected Integer index;

    public PlaySkillLifebuoyActionState(PlayManager manager, WarTeam warTeam, Integer index) {
        super(manager);
        this.warTeam = warTeam;
        this.index = index;
    }

    @Override
    public void initCommands() {
        commands.add(new PlaySkillUseCommand(manager.getContainer(), warTeam, Skill.LIFEBUOY));
        commands.add(new PlaySkillEnableTeamMemberCommand(manager, warTeam, index));
    }

    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = new HashMap<>();
        fillModelSkills(model, (WarTeam) team, (WarTeam) opponentTeam);
        fillModelPresentIndexes(model, (WarTeam) team, (WarTeam) opponentTeam);
        return model;
    }

    @Override
    public void updateNotify() {
        RivalTeams teams = manager.getContainer().getTeams();
        teams.forEachTeam(team -> {
            manager.getCommunication().sendAndUpdateModel(team.getProfileId(), this.prepareModel(team, teams.opponent(team)));
        });
    }
}
