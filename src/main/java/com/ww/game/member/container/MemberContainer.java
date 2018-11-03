package com.ww.game.member.container;

import com.ww.game.GameContainer;
import com.ww.helper.AnswerHelper;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WisieTeamMember;
import com.ww.model.entity.outside.rival.task.Question;
import lombok.Getter;

@Getter
public abstract class MemberContainer extends GameContainer {
    protected RivalTeam team;
    protected TeamMember member;
    protected Question question;
    protected int difficulty;
    protected int answerCount;
    protected Long correctAnswerId;

    protected MemberContainer(RivalTeam team, TeamMember member, Question question) {
        this.team = team;
        this.member = member;
        this.question = question;
        this.difficulty = AnswerHelper.difficultyCalibration(question) + 1;
        this.answerCount = question.getAnswers().size();
        this.correctAnswerId = question.findCorrectAnswerId();
    }
}
