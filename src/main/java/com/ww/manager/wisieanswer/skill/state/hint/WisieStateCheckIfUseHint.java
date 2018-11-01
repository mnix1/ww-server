package com.ww.manager.wisieanswer.skill.state.hint;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.WisieSkillState;
import com.ww.model.constant.wisie.MemberWisieStatus;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateCheckIfUseHint extends WisieSkillState {
    private boolean hintCorrect;

    private MemberWisieStatus action;
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
    protected MemberWisieStatus processWisieAnswerAction() {
        if (hintCorrect) {
            action = MemberWisieStatus.WILL_USE_HINT;
            return action;
        }
        double diffPart = (4 - manager.getDifficulty()) * 0.05;
        double attrPart = ((manager.getWarWisie().getWisdomSum() + manager.getWarWisie().getIntuitionF1() + manager.getWarWisie().getConfidenceF1()) / 3 - 0.5) * 4 / 5;
        double hobbyPart = manager.getWarWisie().isHobby() ? 0.1 : 0;
        chance = 0.5 + diffPart + attrPart + hobbyPart;
        useHint = chance <= randomDouble();
        if (useHint) {
            action = MemberWisieStatus.WILL_USE_HINT;
        } else {
            action = MemberWisieStatus.WONT_USE_HINT;
        }
        return action;
    }
}
