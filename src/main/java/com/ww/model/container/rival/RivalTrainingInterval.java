package com.ww.model.container.rival;

import com.ww.model.container.rival.war.WarInterval;

public class RivalTrainingInterval extends WarInterval {
    @Override
    public long getChoosingTaskCategoryInterval() {
        return super.getChoosingTaskCategoryInterval() * 2;
    }

    @Override
    public long getChoosingTaskDifficultyInterval() {
        return super.getChoosingTaskDifficultyInterval() * 2;
    }

    @Override
    public long getChoosingWhoAnswerInterval() {
        return super.getChoosingWhoAnswerInterval() * 2;
    }

    @Override
    public long wisieIntervalMultiply() {
        return super.wisieIntervalMultiply() * 2;
    }

    @Override
    public long getAnsweringInterval() {
        return (long) (super.getAnsweringInterval() * 1.5);
    }

    @Override
    public long getRandomTaskPropsInterval() {
        return (long) (super.getRandomTaskPropsInterval() * 1.5);
    }

    @Override
    public long getPreparingNextTaskInterval() {
        return (long) (super.getPreparingNextTaskInterval() * 1.5);
    }
}
