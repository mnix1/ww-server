package com.ww.manager.wisieanswer.state.phase3;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateCheckFoundAnswerLookingFor extends WisieState {
    private Boolean foundAnswerLookingFor;
    private Double chance;

    public WisieStateCheckFoundAnswerLookingFor(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_DECISION);
    }

    @Override
    public String describe() {
        return super.describe() + ", chance=" + chance + ", foundAnswerLookingFor=" + foundAnswerLookingFor;
    }

    @Override
    protected WisieAnswerAction processWisieAnswerAction() {
        double diffPart = (4 - manager.getDifficulty()) * 0.05;
        double attrPart = (manager.getWarWisie().getWisdomSum() - 0.5) * 4 / 5;
        double hobbyPart = manager.getWarWisie().isHobby() ? 0.1 : 0;
        chance = 0.5 + diffPart + attrPart + hobbyPart;
         foundAnswerLookingFor = chance > randomDouble();
        if (foundAnswerLookingFor) {
            return WisieAnswerAction.FOUND_ANSWER_LOOKING_FOR;
        }
        return WisieAnswerAction.NO_FOUND_ANSWER_LOOKING_FOR;
    }
}
