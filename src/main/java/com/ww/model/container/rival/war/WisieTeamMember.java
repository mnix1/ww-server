package com.ww.model.container.rival.war;

import com.ww.game.member.MemberWisieManager;
import com.ww.model.constant.wisie.HeroType;
import com.ww.model.constant.wisie.MentalAttribute;
import com.ww.model.constant.wisie.WisdomAttribute;
import com.ww.model.dto.wisie.WarProfileWisieDTO;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WisieTeamMember extends TeamMember {
    @Getter
    private WarWisie content;
    @Getter
    private WarProfileWisieDTO contentDTO;
    private List<MemberWisieManager> managers;

    public WisieTeamMember(int index, WarWisie content) {
        super(index, HeroType.WISIE);
        this.content = content;
    }

    public void addManager(MemberWisieManager manager) {
        managers.add(manager);
    }

    public MemberWisieManager currentManager() {
        return managers.get(managers.size() - 1);
    }

    private void refreshCache() {
        content.cacheAttributes();
        content.cacheHobbies();
        contentDTO = new WarProfileWisieDTO(content);
    }

    public void decreaseAttributesByHalf() {
        content.decreaseAttributesByHalf();
    }

    public void increaseWisdomAttributes(WarWisie source, double factor) {
        content.increaseWisdomAttributes(source, factor);
    }

    public void increaseMentalAttributes(WarWisie source, double factor) {
        content.increaseMentalAttributes(source, factor);
    }

}
