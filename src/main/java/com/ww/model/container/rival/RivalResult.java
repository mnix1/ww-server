package com.ww.model.container.rival;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;

@Getter
public class RivalResult {
    protected Boolean draw;
    protected Profile winner;
    protected Profile looser;
    protected Boolean resigned;
}
