package com.ww.model.dto.shop;

import com.ww.model.constant.shop.ChestType;
import com.ww.model.entity.shop.Chest;
import com.ww.model.entity.shop.ProfileChest;
import lombok.Getter;

@Getter
public class ChestDTO {

    private Long id;
    private String namePolish;
    private String nameEnglish;
    private ChestType type;

    public ChestDTO(Chest chest) {
    }

    public ChestDTO(ProfileChest profileChest) {
        this.id = profileChest.getId();
        this.type = profileChest.getChest().getType();
    }
}
