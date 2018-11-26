package com.ww.game.play.state.skill.lifebuoy;

import com.ww.game.GameState;
import com.ww.game.play.PlayManager;
import com.ww.game.play.command.skill.PlaySkillEnableTeamMemberCommand;
import com.ww.game.play.command.skill.PlaySkillUseCommand;
import com.ww.game.play.modelfiller.PlayModelPreparer;
import com.ww.model.constant.Skill;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;

import java.util.HashMap;
import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelPresentIndexes;
import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelSkills;

public class PlaySkillLifebuoyActionState extends GameState implements PlayModelPreparer {
    protected PlayManager manager;
    protected WarTeam warTeam;
    protected Integer index;

    public PlaySkillLifebuoyActionState(PlayManager manager, WarTeam warTeam, Integer index) {
        this.manager = manager;
        this.warTeam = warTeam;
        this.index = index;
    }

    @Override
    public void initCommands() {
        commands.add(new PlaySkillUseCommand(manager.getContainer(), warTeam, Skill.LIFEBUOY));
        commands.add(new PlaySkillEnableTeamMemberCommand(warTeam, index));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = new HashMap<>();
        fillModelSkills(model, warTeam, (WarTeam) team, (WarTeam) opponentTeam);
        fillModelPresentIndexes(model, (WarTeam) team, (WarTeam) opponentTeam);
        return model;
    }

    @Override
    public void updateNotify() {
        manager.getCommunication().prepareModel(this);
        manager.getCommunication().sendPreparedModel();
    }
}
