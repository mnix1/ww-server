package com.ww.game.play.command.skill;

import com.ww.game.play.command.PlayCommand;
import com.ww.game.play.container.PlayContainer;
import com.ww.model.constant.Skill;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WisieTeamMember;

public class PlaySkillStartHintFlowCommand extends PlayCommand {
    private WarTeam warTeam;
    private Long answerId;

    public PlaySkillStartHintFlowCommand(PlayContainer container, WarTeam warTeam, Long answerId) {
        super(container);
        this.warTeam = warTeam;
        this.answerId = answerId;
    }

    @Override
    public void execute() {
        WisieTeamMember member = (WisieTeamMember) warTeam.getActiveTeamMember();
//        member.currentManager().getFlow().
    }
}
