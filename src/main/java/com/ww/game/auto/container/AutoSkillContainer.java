package com.ww.game.auto.container;

import com.ww.model.constant.Skill;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class AutoSkillContainer {
    Set<Skill> used = new HashSet<>();
}
