package com.ww.game.member.container;

import com.ww.helper.AnswerHelper;
import com.ww.model.container.rival.war.WisieTeamMember;
import com.ww.model.entity.outside.rival.task.Question;
import lombok.Getter;

@Getter
public class MemberWisieContainer extends MemberContainer {
    private WisieTeamMember member;
    private Question question;
    private int difficulty;
    private int answerCount;
    private Long correctAnswerId;

    public MemberWisieContainer(WisieTeamMember member, Question question) {
        this.member = member;
        this.question = question;
        this.difficulty = AnswerHelper.difficultyCalibration(question) + 1;
        this.answerCount = question.getAnswers().size();
        this.correctAnswerId = question.findCorrectAnswerId();
    }
}
