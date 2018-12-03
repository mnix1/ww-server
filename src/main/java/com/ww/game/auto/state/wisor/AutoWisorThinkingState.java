package com.ww.game.auto.state.wisor;

import com.ww.game.auto.AutoManager;
import com.ww.game.auto.flow.AutoWisorFlow;
import com.ww.model.constant.Category;
import com.ww.model.entity.inside.social.InsideProfile;

import java.util.HashMap;
import java.util.Map;

import static com.ww.helper.RandomHelper.randomDouble;

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
        double constPart = randomDouble(0, 0.15);
        double randomPart = randomDouble(.0, 1 - insideProfile.getLuck());
        double difficultyPart = difficulty * .05 + difficulty * randomDouble(0.05,0.1) * (1 - insideProfile.getWisdom()) + difficulty * 0.4 * (1 - insideProfile.getLuck()) * (1 - insideProfile.getWisdom());
        double part = constPart + randomPart + difficultyPart + (0.5 - insideProfile.getSpeed()) + (0.5 - insideProfile.getReflex());
        logger.error("part=" + part + " randomPart=" + randomPart + " difficultyPart=" + difficultyPart + " isHobby=" + isHobby);
        if (isHobby) {
            part /= 2;
        }
        part = Math.max(part, randomDouble(0.7, 0.15));
        part = Math.min(part, randomDouble(Math.max(0.4, Math.min(0.6, 1 - insideProfile.getLuck())), 0.6));
        logger.error("realPart=" + part);
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
