package com.ww.model.dto.rival;

import com.ww.model.constant.Skill;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SkillDTO {
    private Skill type;
    private int count;
}
