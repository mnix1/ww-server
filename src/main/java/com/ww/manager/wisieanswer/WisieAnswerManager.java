package com.ww.manager.wisieanswer;

import com.ww.helper.AnswerHelper;
import com.ww.manager.rival.war.WarManager;
import com.ww.model.constant.wisie.WisieAnswerAction;
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
public class WisieAnswerManager {
    protected static final Logger logger = LoggerFactory.getLogger(WisieAnswerManager.class);

    private boolean running = false;
    private WisieAnswerFlow flow;

    private List<WisieAnswerAction> actions = new CopyOnWriteArrayList<>();

    private WisieTeamMember wisieMember;

    private WarManager warManager;
    private Question question;

    private int difficulty;
    private int answerCount;

    public WisieAnswerManager(WisieTeamMember wisieMember, Question question, WarManager warManager) {
        this.wisieMember = wisieMember;
        this.warManager = warManager;
        this.question = question;
        this.difficulty = AnswerHelper.difficultyCalibration(question) + 1;
        this.answerCount = question.getAnswers().size();
        this.flow = new WisieAnswerFlow(this);
        logger.trace(toString() +
                ", difficulty=" + difficulty +
                ", answerCount=" + answerCount);
        getWarWisie().setQuestion(question);
        getWarWisie().cacheAttributes();
        getWarWisie().cacheHobbies();
    }

    @Override
    public String toString() {
        return "WisieAnswerManager wisieName=" + getOwnedWisie().getWisie().getNamePolish()
                + ", profileWisieId=" + getOwnedWisie().getId()
                + ", profileId=" + getOwnedWisie().getProfile().getId()
                + ", lastAction=" + lastAction().name();
    }

    public OwnedWisie getOwnedWisie() {
        return getWarWisie().getWisie();
    }

    public WarWisie getWarWisie() {
        return wisieMember.getContent();
    }

    public WisieAnswerAction lastAction() {
        if (actions.isEmpty()) {
            return WisieAnswerAction.NONE;
        }
        return actions.get(actions.size() - 1);
    }

    public WarTeam getTeam(WisieAnswerManager manager) {
        return (WarTeam) warManager.getTeam(manager.getOwnedWisie().getProfile().getId());
    }

    public WarModelFactory getModelFactory(){
        return warManager.getModelFactory();
    }

    public void addAction(WisieAnswerAction action) {
        actions.add(action);
    }

    public void addAndSendAction(WisieAnswerAction action) {
        addAction(action);
        warManager.sendModel((m, wT) -> {
            warManager.getModelFactory().fillModelWisieActions(m, wT);
        });
    }

}
