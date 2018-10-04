package com.ww.model.container.rival.war;

import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.HeroType;
import com.ww.model.dto.wisie.WarProfileWisieDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TeamMember {
    private int index;
    private HeroType type;
    private boolean present;
    private Object content;
    private Object contentDTO;
    private List<DisguiseType> disguises = new ArrayList<>();

    public TeamMember(int index, HeroType type, Object content, Object contentDTO) {
        this.index = index;
        this.type = type;
        this.present = true;
        this.content = content;
        this.contentDTO = contentDTO;
    }

    public void addDisguise(DisguiseType disguise) {
        disguises.add(disguise);
        setActiveDisguise(disguise);
    }

    public void removeDisguise() {
        DisguiseType disguise = disguises.size() > 1 ? disguises.get(disguises.size() - 2) : null;
        if (!disguises.isEmpty()) {
            disguises.remove(disguises.size() - 1);
        }
        setActiveDisguise(disguise);
    }

    public void resetDisguises() {
        disguises.clear();
        setActiveDisguise(null);
    }

    private void setActiveDisguise(DisguiseType disguise) {
        WarProfileWisieDTO dto = (WarProfileWisieDTO) contentDTO;
        dto.setDisguise(disguise);
    }

    public boolean isWisie() {
        return HeroType.isWisie(type);
    }

}
