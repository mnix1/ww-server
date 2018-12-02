package com.ww.game.auto.state.wisor;

import com.ww.game.auto.AutoManager;
import com.ww.game.auto.command.AutoWisorAnswerCommand;
import com.ww.game.auto.flow.AutoWisorFlow;
import com.ww.model.entity.inside.social.InsideProfile;

import java.util.ArrayList;

import static com.ww.helper.RandomHelper.randomDouble;
import static com.ww.helper.RandomHelper.randomElement;

public class AutoWisorAnswerState extends AutoWisorState {

    private boolean isHobby;
    private double chanceCorrect;
    private double random;
    private boolean correct;
    private Long answerId;

    public AutoWisorAnswerState(AutoWisorFlow flow, AutoManager manager) {
        super(flow, manager);
    }

    @Override
    public void initProps() {
        isHobby = (boolean) params.get("isHobby");
        random = randomDouble();
        correct = isCorrect();
        answerId = findAnswerId();
    }

    @Override
    public void initCommands() {
        commands.add(new AutoWisorAnswerCommand(manager, answerId));
    }

    private boolean isCorrect() {
        int difficulty = manager.getAutoPlayContainer().question().getDifficultyLevel().getPoints();
        InsideProfile insideProfile = manager.getInsideProfile();
        chanceCorrect = 0.5 - (difficulty - 1) * 0.15 + insideProfile.getWisdom() * .5 + insideProfile.getCunning() * .25;
        if (insideProfile.getLuck() > randomDouble()) {
            chanceCorrect += 0.1;
        }
        if (isHobby) {
            chanceCorrect += 0.3;
        }
        return chanceCorrect >= random;
    }

    private Long findAnswerId() {
        if (correct) {
            return manager.getAutoPlayContainer().question().findCorrectAnswerId();
        }
        return manager.getAutoPlayContainer().question().randomAnswerId();
    }

    @Override
    public String toString() {
        return super.toString() + ", isHobby=" + isHobby + ", chanceCorrect=" + chanceCorrect + ", random=" + random + ", correct=" + correct + ", answerId=" + answerId;
    }

}
