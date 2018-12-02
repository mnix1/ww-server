package com.ww.model.container.rival;

public class RivalTrainingInterval extends RivalInterval {
    public long getChoosingTaskCategoryInterval() {
        return super.getChoosingTaskCategoryInterval() * 2;
    }

    public long getChoosingTaskDifficultyInterval() {
        return super.getChoosingTaskDifficultyInterval() * 2;
    }

    public long getRandomTaskPropsInterval() {
        return (long) (super.getRandomTaskPropsInterval() * 1.5);
    }

    public long getIntroInterval() {
        return (long) (super.getIntroInterval() * 1.5);
    }

    public long getPreparingNextTaskInterval() {
        return (long) (super.getPreparingNextTaskInterval() * 1.5);
    }
}
