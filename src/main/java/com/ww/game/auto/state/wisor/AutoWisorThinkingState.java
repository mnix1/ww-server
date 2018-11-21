package com.ww.game.auto.state.wisor;

import com.ww.game.auto.AutoManager;
import com.ww.game.auto.flow.AutoWisorFlow;
import com.ww.model.constant.Category;

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
        int difficulty = manager.getAutoPlayContainer().question().getDifficultyLevel().getPoints();
        double betweenInterval = randomDouble(randomDouble(1.5, 3.0), randomDouble(3.5, 6.0)) * difficulty;
        if (isHobby) {
            betweenInterval /= 2;
        }
        betweenInterval = Math.max(betweenInterval, 2);
        return (long) (betweenInterval * manager.getAutoPlayContainer().interval().intervalMultiply());
    }

    private boolean isHobby() {
        List<Category> categories = Category.list();
        int hobbyCount = randomInteger(1, categories.size());
        Collections.shuffle(categories);
        categories = categories.subList(0, hobbyCount);
        return categories.contains(manager.getAutoPlayContainer().question().getType().getCategory());
    }

    @Override
    public String toString() {
        return super.toString() + ", interval=" + interval + ", isHobby=" + isHobby;
    }

}
