package com.ww.model.container.rival.war;

import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.HeroType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@ToString
public abstract class TeamMember {
    private int index;
    private HeroType type;
    @Setter
    private boolean present;
    private List<DisguiseType> disguises = new CopyOnWriteArrayList<>();

    public abstract Object getContent();
    public abstract Object getContentDTO();

    public TeamMember(int index, HeroType type) {
        this.index = index;
        this.type = type;
        this.present = true;
    }

    public boolean isWisie() {
        return HeroType.isWisie(type);
    }

    public TeamMember addDisguise(DisguiseType disguise) {
        disguises.add(disguise);
        return this;
    }

    public TeamMember removeDisguise() {
        if (!disguises.isEmpty()) {
            disguises.remove(disguises.size() - 1);
        }
        return this;
    }

    public TeamMember resetDisguises() {
        disguises.clear();
        return this;
    }

    public DisguiseType activeDisguise() {
        if (disguises.isEmpty()) {
            return null;
        }
        return disguises.get(disguises.size() - 1);
    }

}
