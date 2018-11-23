package com.ww.game.member.container;

import com.ww.helper.AnswerHelper;
import com.ww.model.constant.wisie.MemberWisieStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WisieTeamMember;
import com.ww.model.entity.outside.rival.task.Question;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class MemberWisieContainer {
    protected List<MemberWisieStatus> actions = new CopyOnWriteArrayList<>();
    protected RivalTeam team;
    protected TeamMember member;
    protected Question question;
    protected int difficulty;
    protected int answerCount;
    protected Long correctAnswerId;

    public MemberWisieContainer(WarTeam team, WisieTeamMember member, Question question) {
        this.team = team;
        this.member = member;
        this.question = question;
        this.difficulty = AnswerHelper.difficultyCalibration(question) + 1;
        this.answerCount = question.getAnswers().size();
        this.correctAnswerId = question.findCorrectAnswerId();
    }

    public WarTeam getTeam() {
        return (WarTeam) team;
    }

    public double difficultyPart(double factor) {
        return (4 - difficulty) * factor;
    }

    public WisieTeamMember getMember() {
        return (WisieTeamMember) member;
    }

    public void addAction(MemberWisieStatus action) {
        actions.add(action);
    }

    @Override
    public String toString() {
        return "MemberWisieContainer{" +
                "actions=" + StringUtils.join(actions, ",^_^") +
                '}';
    }
}
