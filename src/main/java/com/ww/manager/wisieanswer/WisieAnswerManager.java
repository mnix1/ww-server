package com.ww.manager.wisieanswer;

import com.ww.helper.AnswerHelper;
import com.ww.helper.Describe;
import com.ww.manager.rival.war.WarManager;
import com.ww.model.constant.wisie.MemberWisieStatus;
import com.ww.model.container.rival.war.WarModelFactory;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WarWisie;
import com.ww.model.container.rival.war.WisieTeamMember;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.wisie.OwnedWisie;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
public class WisieAnswerManager implements Describe {
    protected static final Logger logger = LoggerFactory.getLogger(WisieAnswerManager.class);

    private boolean running = false;
    private WisieAnswerFlow flow;

    private List<MemberWisieStatus> actions = new CopyOnWriteArrayList<>();

    private WisieTeamMember wisieMember;

    private WarTeam warTeam;
    private WarManager warManager;
    private Question question;

    private int difficulty;
    private int answerCount;

    public WisieAnswerManager(WisieTeamMember wisieMember, WarTeam warTeam, Question question, WarManager warManager) {
        this.wisieMember = wisieMember;
        this.warTeam = warTeam;
        this.warManager = warManager;
        this.question = question;
        this.difficulty = AnswerHelper.difficultyCalibration(question) + 1;
        this.answerCount = question.getAnswers().size();
        this.flow = new WisieAnswerFlow(this);
        logger.trace("new " + describe() +
                ", difficulty=" + difficulty +
                ", questionCategory=" + question.getType().getCategory().name() +
                ", questionType=" + question.getType().getValue() +
                ", answerCount=" + answerCount);
//        getWarWisie().setQuestion(question);
//        getWarWisie().cacheAttributes();
//        getWarWisie().cacheHobbies();
    }

    public OwnedWisie getOwnedWisie() {
        return getWarWisie().getWisie();
    }

    public Long getProfileId() {
        return getWarTeam().getProfileId();
    }

    public WarWisie getWarWisie() {
        return wisieMember.getContent();
    }

    public MemberWisieStatus lastAction() {
        if (actions.isEmpty()) {
            return MemberWisieStatus.NONE;
        }
        return actions.get(actions.size() - 1);
    }

    public WarTeam getTeam(WisieAnswerManager manager) {
        return (WarTeam) warManager.getTeam(manager.getProfileId());
    }

    public WarModelFactory getModelFactory() {
        return warManager.getModelFactory();
    }

    public void addAction(MemberWisieStatus action) {
        actions.add(action);
    }

    public void addAndSendAction(MemberWisieStatus action) {
        addAction(action);
        warManager.sendModel((m, wT) -> {
            warManager.getModelFactory().fillModelWisieActions(m, wT);
        });
    }

    @Override
    public String describe() {
        return "WisieAnswerManager wisieType=" + getOwnedWisie().getType()
                + ", profileWisieId=" + getOwnedWisie().getId()
                + ", profileId=" + getWarTeam().getProfile().getId()
                + ", lastAction=" + lastAction().name();
    }
}
