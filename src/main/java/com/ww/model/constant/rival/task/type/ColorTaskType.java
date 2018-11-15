package com.ww.model.constant.rival.task.type;

public enum ColorTaskType {
    BIGGEST_R,
    BIGGEST_G,
    BIGGEST_B,
    LOWEST_R,
    LOWEST_G,
    LOWEST_B,
    COLOR_MIXING;

    public static boolean aboutLowest(ColorTaskType type){
        return type == LOWEST_R
                ||type == LOWEST_G
                ||type == LOWEST_B;
    }
}
