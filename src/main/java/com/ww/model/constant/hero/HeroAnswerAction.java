package com.ww.model.constant.hero;

import java.util.Arrays;
import java.util.List;

public enum HeroAnswerAction {
    NONE,

    WAITING_FOR_QUESTION,
    READING_QUESTION,
    THINKING,

    THINK_KNOW_ANSWER,
    LOOKING_FOR_ANSWER,

    NO_FOUND_ANSWER_LOOKING_FOR,
    FOUND_ANSWER_LOOKING_FOR,

    NOT_SURE_OF_ANSWER,
    READING_ANSWERS,

    ANSWERED,
    SURRENDER,

    WILL_GIVE_RANDOM_ANSWER,
    WONT_GIVE_RANDOM_ANSWER,

    THINKING_WHICH_ANSWER_MATCH,
    THINKING_IF_GIVE_RANDOM_ANSWER,

    DREAMING_ABOUT_VACATION,
    SCRATCHING,
    YAWNING,
    HUNG_UP,
    NEED_GO_TO_TOILET;

    public static List<HeroAnswerAction> getNoConcentrationActions() {
        return Arrays.asList(DREAMING_ABOUT_VACATION, SCRATCHING, YAWNING, HUNG_UP, NEED_GO_TO_TOILET);
    }

    public static boolean isNoConcentration(HeroAnswerAction action) {
        return getNoConcentrationActions().contains(action);
    }
}
