package com.ww.model.container.rival.war;

import com.ww.game.member.MemberWisieManager;
import com.ww.model.constant.wisie.HeroType;
import com.ww.model.constant.wisie.MentalAttribute;
import com.ww.model.constant.wisie.WisdomAttribute;
import com.ww.model.dto.wisie.WarProfileWisieDTO;
import com.ww.model.entity.outside.rival.task.Question;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class WisieTeamMember extends TeamMember {
    @Getter
    private WarWisie content;
    private WarProfileWisieDTO contentDTO;
    private List<MemberWisieManager> managers = new CopyOnWriteArrayList<>();

    public WisieTeamMember(int index, WarWisie content) {
        super(index, HeroType.WISIE);
        this.content = content;
    }

    public WarProfileWisieDTO getContentDTO() {
        if (contentDTO == null) {
            contentDTO = new WarProfileWisieDTO(content);
        }
        return contentDTO;
    }

    public void addManager(MemberWisieManager manager) {
        managers.add(manager);
    }

    public MemberWisieManager currentManager() {
        return managers.get(managers.size() - 1);
    }

    public void refreshCache(Question question) {
        content.cacheAttributes(question);
        content.cacheHobbies(question);
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
