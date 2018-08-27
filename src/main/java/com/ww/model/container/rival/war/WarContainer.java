package com.ww.model.container.rival.war;

import com.ww.model.container.rival.RivalContainer;
import com.ww.model.container.rival.RivalProfileContainer;
import com.ww.model.dto.hero.ProfileHeroDTO;
import com.ww.model.dto.hero.WarProfileHeroDTO;
import com.ww.model.entity.hero.ProfileHero;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class WarContainer extends RivalContainer {
    private List<WarProfileHeroDTO> prepareTeam(List<ProfileHero> heroes) {
        return heroes.stream().map(WarProfileHeroDTO::new).collect(Collectors.toList());
    }

    public WarProfileContainer getRivalProfileContainer(Long id) {
        return (WarProfileContainer) super.getRivalProfileContainer(id);
    }

    public void fillModelBasic(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        super.fillModelBasic(model, rivalProfileContainer);
        WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
        model.put("presentIndexes", warProfileContainer.getPresentIndexes());
        model.put("activeIndex", warProfileContainer.getActiveIndex());
        model.put("team", prepareTeam(warProfileContainer.getHeroes()));
        model.put("opponentPresentIndexes", getRivalProfileContainer(rivalProfileContainer.getOpponentId()).getPresentIndexes());
        model.put("opponentActiveIndex", getRivalProfileContainer(rivalProfileContainer.getOpponentId()).getActiveIndex());
        model.put("opponentTeam", prepareTeam(getRivalProfileContainer(rivalProfileContainer.getOpponentId()).getHeroes()));
    }
}
