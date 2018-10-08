package com.ww.manager.wisieanswer.skill.state.ghost;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateCheckWasCaught extends WisieState {
    private boolean scareSuccess;

    private Boolean caught;
    private Double chance;

    public WisieStateCheckWasCaught(WisieAnswerManager manager, boolean scareSuccess) {
        super(manager, STATE_TYPE_DECISION);
        this.scareSuccess = scareSuccess;
    }

    @Override
    public String describe() {
        return super.describe() + ", chance=" + chance + ", caught=" + caught;
    }

    @Override
    protected Boolean processBoolean() {
        chance = (manager.getWarWisie().getConfidenceF1() + manager.getWarWisie().getIntuitionF1()) / 2;
        if (scareSuccess) {
            chance += 0.25;
        } else {
            chance -= 0.25;
        }
        caught = chance <= randomDouble();
        return caught;
    }
}
