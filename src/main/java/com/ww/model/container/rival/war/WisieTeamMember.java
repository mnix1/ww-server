package com.ww.model.container.rival.war;

import com.ww.game.member.MemberWisieManager;
import com.ww.model.constant.wisie.HeroType;
import com.ww.model.dto.wisie.WarProfileWisieDTO;
import com.ww.model.entity.outside.rival.task.Question;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
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

    public Optional<MemberWisieManager> currentManager() {
        if (managers.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(managers.get(managers.size() - 1));
    }

    public void refreshCache(Question question) {
        content.cacheAttributes(question);
        content.cacheHobbies(question);
        contentDTO = new WarProfileWisieDTO(content);
    }

    public void decreaseAttributesByHalf() {
        content.decreaseAttributesByHalf();
    }

    public void increaseAttributesByHalf() {
        content.increaseAttributesByHalf();
    }

    public void increaseWisdomAttributes(WarWisie source, double factor) {
        content.increaseWisdomAttributes(source, factor);
    }

    public void increaseMentalAttributes(WarWisie source, double factor) {
        content.increaseMentalAttributes(source, factor);
    }

    @Override
    public String toString() {
        return content.toString() + ", " + super.toString();
    }

}
