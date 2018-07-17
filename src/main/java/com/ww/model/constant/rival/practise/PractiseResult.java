package com.ww.model.constant.rival.practise;

public enum PractiseResult {
    OPEN,
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
