package com.ww.model.constant.wisie;

import java.util.Arrays;
import java.util.List;

public enum WisieAnswerAction {
    NONE,

    HINT_RECEIVED,
    THINKING_IF_USE_HINT,
    WILL_USE_HINT,
    WONT_USE_HINT,

    WATER_PISTOL_USED_ON_IT,
    CLEANING,

    WAITING_FOR_QUESTION,
    RECOGNIZING_QUESTION,
    THINKING,

    THINK_KNOW_ANSWER,
    LOOKING_FOR_ANSWER,

    NO_FOUND_ANSWER_LOOKING_FOR,
    FOUND_ANSWER_LOOKING_FOR,

    NOT_SURE_OF_ANSWER,
    RECOGNIZING_ANSWERS,

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

    public static List<WisieAnswerAction> getNoConcentrationActions() {
        return Arrays.asList(DREAMING_ABOUT_VACATION, SCRATCHING, YAWNING, HUNG_UP, NEED_GO_TO_TOILET);
    }

    public static boolean isNoConcentration(WisieAnswerAction action) {
        return getNoConcentrationActions().contains(action);
    }
}
