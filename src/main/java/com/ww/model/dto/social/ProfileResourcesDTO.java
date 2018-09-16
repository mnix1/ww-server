package com.ww.model.dto.social;

import com.ww.model.constant.Language;
import com.ww.model.entity.social.Profile;
import lombok.Getter;
import org.apache.commons.codec.language.bm.Lang;

@Getter
public class ProfileResourcesDTO extends ProfileDTO {

    private Long level;
    private Long experience;
    private Long wisdom;
    private Long crystal;
    private Long gold;
    private Long elixir;
    private Long battleElo;
    private Long warElo;
    private Language language;

    public ProfileResourcesDTO(Profile profile) {
        super(profile);
        this.language = profile.getLanguage();
        this.level = profile.getLevel();
        this.experience = profile.getExperience();
        this.wisdom = profile.getWisdom();
        this.crystal = profile.getCrystal();
        this.gold = profile.getGold();
        this.elixir = profile.getElixir();
        this.battleElo = profile.getBattleElo();
        this.warElo = profile.getWarElo();
    }
}
