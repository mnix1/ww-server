package com.ww.game.auto.state.wisor;

import com.ww.game.auto.AutoManager;
import com.ww.game.auto.flow.AutoWisorFlow;
import com.ww.model.constant.Category;
import com.ww.model.entity.inside.social.InsideProfile;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ww.helper.RandomHelper.randomDouble;
import static com.ww.helper.RandomHelper.randomInteger;

public class AutoWisorThinkingState extends AutoWisorState {

    private boolean isHobby;
    private long interval;

    public AutoWisorThinkingState(AutoWisorFlow flow, AutoManager manager) {
        super(flow, manager);
    }

    @Override
    public void initProps() {
        isHobby = isHobby();
        interval = prepareInterval();
    }

    @Override
    public long afterInterval() {
        return interval;
    }

    @Override
    public void after() {
        Map<String, Object> props = new HashMap<>();
        props.put("isHobby", isHobby);
        flow.run("ANSWER", props);
    }

    private long prepareInterval() {
        long answeringInterval = manager.getAutoPlayContainer().interval().getAnsweringInterval();
        int difficulty = manager.getAutoPlayContainer().question().getDifficultyLevel().getPoints();
        InsideProfile insideProfile = manager.getInsideProfile();
        double constPart = .1;
        double randomPart = randomDouble(.0, 1 - insideProfile.getLuck());
        double difficultyPart = difficulty * .1 * (1 - insideProfile.getWisdom()) + difficulty * 0.5 * (1 - insideProfile.getLuck()) * (1 - insideProfile.getWisdom());
        double part = constPart + randomPart + difficultyPart + (0.5 - insideProfile.getSpeed()) - insideProfile.getReflex();
        if (isHobby) {
            part /= 3;
        }
        part = Math.max(part, 0.05);
        part = Math.min(part, 0.9);
        return (long) (answeringInterval * part);
    }

    private boolean isHobby() {
        Category category = manager.getAutoPlayContainer().question().getType().getCategory();
        return manager.getInsideProfile().getHobbies().contains(category);
    }

    @Override
    public String toString() {
        return super.toString() + ", interval=" + interval + ", isHobby=" + isHobby;
    }

}
