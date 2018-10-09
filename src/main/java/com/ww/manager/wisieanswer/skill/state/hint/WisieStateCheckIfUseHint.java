package com.ww.manager.wisieanswer.skill.state.hint;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.WisieSkillState;
import com.ww.model.constant.wisie.WisieAnswerAction;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateCheckIfUseHint extends WisieSkillState {
    private boolean hintCorrect;

    private WisieAnswerAction action;
    private Double chance;
    private Boolean useHint;

    public WisieStateCheckIfUseHint(WisieAnswerManager manager, boolean hintCorrect) {
        super(manager, STATE_TYPE_DECISION);
        this.hintCorrect = hintCorrect;
    }

    @Override
    public String describe() {
        return super.describe() + ", chance=" + chance + ", hintCorrect=" + hintCorrect + ", action=" + action + ", useHint=" + useHint;
    }

    @Override
    protected WisieAnswerAction processWisieAnswerAction() {
        if (hintCorrect) {
            action = WisieAnswerAction.WILL_USE_HINT;
            return action;
        }
        double diffPart = (4 - manager.getDifficulty()) * 0.05;
        double attrPart = ((manager.getWarWisie().getWisdomSum() + manager.getWarWisie().getIntuitionF1() + manager.getWarWisie().getConfidenceF1()) / 3 - 0.5) * 4 / 5;
        double hobbyPart = manager.getWarWisie().isHobby() ? 0.1 : 0;
        chance = 0.5 + diffPart + attrPart + hobbyPart;
        useHint = chance <= randomDouble();
        if (useHint) {
            action = WisieAnswerAction.WILL_USE_HINT;
        } else {
            action = WisieAnswerAction.WONT_USE_HINT;
        }
        return action;
    }
}
