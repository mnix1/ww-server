package com.ww.model.container.rival.war;

import com.ww.manager.HeroAnswerManager;
import com.ww.manager.RivalManager;
import com.ww.model.constant.hero.HeroAnswerAction;
import com.ww.model.container.rival.RivalContainer;
import com.ww.model.container.rival.RivalProfileContainer;
import com.ww.model.container.rival.battle.BattleProfileContainer;
import com.ww.model.dto.hero.ProfileHeroDTO;
import com.ww.model.dto.hero.WarProfileHeroDTO;
import com.ww.model.entity.hero.ProfileHero;
import com.ww.model.entity.rival.task.Question;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class WarContainer extends RivalContainer {
    List<HeroAnswerManager> heroAnswerManagers;

    public void updateHeroAnswerManagers(RivalManager rivalManager) {
        heroAnswerManagers = new ArrayList<>();
        forEachProfile(rivalProfileContainer -> {
            WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
            ProfileHero answeringHero = warProfileContainer.getAnsweringHero();
            if (answeringHero != null) {
                heroAnswerManagers.add(new HeroAnswerManager(answeringHero, rivalManager));
            }
        });
    }

    private HeroAnswerManager getHeroAnswerManager(WarProfileContainer warProfileContainer) {
        ProfileHero answeringHero = warProfileContainer.getAnsweringHero();
        if(answeringHero == null){
            return null;
        }
        for (HeroAnswerManager answerManager : heroAnswerManagers) {
            if (answerManager.getHero().getId().equals(answeringHero.getId())) {
                return answerManager;
            }
        }
        return null;
    }

    public void startHeroAnswerManager() {
        for (HeroAnswerManager manager : heroAnswerManagers) {
            manager.start();
        }
    }

    public void stopHeroAnswerManager() {
        for (HeroAnswerManager manager : heroAnswerManagers) {
            manager.stop();
        }
    }

    private List<WarProfileHeroDTO> prepareTeam(List<ProfileHero> heroes) {
        return heroes.stream().map(WarProfileHeroDTO::new).collect(Collectors.toList());
    }

    public String findChoosingTaskPropsTag() {
        return null;
    }

    public String findWinnerTag() {
        List<RivalProfileContainer> rivalProfileContainers = new ArrayList<>(getProfileIdRivalProfileContainerMap().values());
        Integer presentIndexSize1 = ((WarProfileContainer) rivalProfileContainers.get(0)).getPresentIndexes().size();
        Integer presentIndexSize2 = ((WarProfileContainer) rivalProfileContainers.get(1)).getPresentIndexes().size();
        if (presentIndexSize1.equals(presentIndexSize2)) {
            return RivalManager.DRAW_WINNER_TAG;
        }
        if (presentIndexSize1 == 0) {
            return rivalProfileContainers.get(1).getProfile().getTag();
        }
        return rivalProfileContainers.get(0).getProfile().getTag();
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

    public void fillModelAnswered(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        super.fillModelAnswered(model, rivalProfileContainer);
        WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
        model.put("heroActions", null);
        model.put("opponentHeroActions", null);
        model.put("presentIndexes", warProfileContainer.getPresentIndexes());
        model.put("opponentPresentIndexes", getRivalProfileContainer(rivalProfileContainer.getOpponentId()).getPresentIndexes());
    }

    public void fillModelAnswering(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        super.fillModelAnswering(model, rivalProfileContainer);
        fillModelHeroAnswering(model, rivalProfileContainer);
    }

    public void fillModelHeroAnswering(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
        HeroAnswerManager manager = getHeroAnswerManager(warProfileContainer);
        if (manager != null) {
            model.put("heroActions", manager.getActions());
        }
        manager = getHeroAnswerManager(getRivalProfileContainer(rivalProfileContainer.getOpponentId()));
        if (manager != null) {
            model.put("opponentHeroActions", manager.getActions());
        }
    }

    public void fillModelAnsweringTimeout(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        super.fillModelAnsweringTimeout(model, rivalProfileContainer);
        WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
        model.put("heroActions", null);
        model.put("opponentHeroActions", null);
        model.put("presentIndexes", warProfileContainer.getPresentIndexes());
        model.put("opponentPresentIndexes", getRivalProfileContainer(rivalProfileContainer.getOpponentId()).getPresentIndexes());
    }

    public void fillModelChoosingTaskProps(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
        model.put("status", status);
        model.put("activeIndex", warProfileContainer.getActiveIndex());
        model.put("opponentActiveIndex", getRivalProfileContainer(rivalProfileContainer.getOpponentId()).getActiveIndex());
        model.put("taskId", currentTaskIndex + 1);
        model.put("task", taskDTOs.get(currentTaskIndex).toTaskMeta());
    }
}
