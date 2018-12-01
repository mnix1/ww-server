package com.ww.model.container.rival;

public class RivalTrainingInterval extends RivalInterval {
    public long getAnsweringInterval() {
        return super.getAnsweringInterval() * 2;
    }

    public long getChoosingTaskCategoryInterval() {
        return super.getChoosingTaskCategoryInterval() * 2;
    }

    public long getChoosingTaskDifficultyInterval() {
        return super.getChoosingTaskDifficultyInterval() * 2;
    }

    public long getRandomTaskPropsInterval() {
        return super.getRandomTaskPropsInterval() * 2;
    }

    public long getIntroInterval() {
        return super.getIntroInterval() * 2;
    }

    public long getPreparingNextTaskInterval() {
        return super.getPreparingNextTaskInterval() * 2;
    }
}
