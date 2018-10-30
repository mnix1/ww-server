package com.ww.model.container.rival;

public class RivalInterval {
    protected long calculateInterval(double interval) {
        return (long) (intervalMultiply() * interval);
    }

    protected long intervalMultiply() {
        return 1000;
    }

    public long getIntroInterval() {
        return calculateInterval(3.5);
    }

    public long getPreparingNextTaskInterval() {
        return calculateInterval(2);
    }

    public long getAnsweringInterval() {
        return calculateInterval(45);
    }

    public long getAnsweringTimeoutInterval() {
        return calculateInterval(8);
    }

    public long getChangingTaskInterval() {
        return calculateInterval(4);
    }

    public long getAnsweredInterval() {
        return calculateInterval(8);
    }

    public long getChoosingTaskCategoryInterval() {
        return calculateInterval(10);
    }

    public long getChoosingTaskDifficultyInterval() {
        return calculateInterval(10);
    }

    public long getChoosingTaskPropsInterval() {
        return calculateInterval(14);
    }

    public long getRandomTaskPropsInterval() {
        return calculateInterval(2.5);
    }
}
