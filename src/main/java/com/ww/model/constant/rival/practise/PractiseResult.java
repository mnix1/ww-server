package com.ww.model.constant.rival.practise;

public enum PractiseResult {
    IN_PROGRESS,
    ERROR,
    CORRECT,
    WRONG;

    public static PractiseResult fromBoolean(Boolean result) {
        if (result) {
            return CORRECT;
        }
        return WRONG;
    }
}
