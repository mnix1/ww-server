package com.ww.model.container.rival.war;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.rival.RivalManager;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalContainer;
import com.ww.model.container.rival.RivalProfileContainer;
import com.ww.model.dto.wisie.WarProfileWisieDTO;
import com.ww.model.entity.social.Profile;
import com.ww.model.entity.wisie.ProfileWisie;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
public class WarContainer extends RivalContainer {
    List<WisieAnswerManager> wisieAnswerManagers = new ArrayList<>();
    protected Instant endChoosingWhoAnswerDate;

    public void updateWisieAnswerManagers(RivalManager rivalManager) {
        wisieAnswerManagers = new ArrayList<>();
        forEachProfile(rivalProfileContainer -> {
            WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
            ProfileWisie answeringWisie = warProfileContainer.getAnsweringWisie();
            if (answeringWisie != null) {
                wisieAnswerManagers.add(new WisieAnswerManager(answeringWisie, rivalManager));
            }
        });
    }

    private WisieAnswerManager getWisieAnswerManager(WarProfileContainer warProfileContainer) {
        ProfileWisie answeringWisie = warProfileContainer.getAnsweringWisie();
        if (answeringWisie == null) {
            return null;
        }
        for (WisieAnswerManager answerManager : wisieAnswerManagers) {
            if (answerManager.getWisie().equals(answeringWisie)) {
                return answerManager;
            }
        }
        return null;
    }

    public void startWisieAnswerManager() {
        for (WisieAnswerManager manager : wisieAnswerManagers) {
            manager.start();
        }
    }

    public void stopWisieAnswerManager() {
        for (WisieAnswerManager manager : wisieAnswerManagers) {
            manager.stop();
        }
    }

    private List<WarProfileWisieDTO> prepareTeam(List<ProfileWisie> wisies) {
        return wisies.stream().map(WarProfileWisieDTO::new).collect(Collectors.toList());
    }

    public String findChoosingTaskPropsTag() {
        List<RivalProfileContainer> rivalProfileContainers = new ArrayList<>(getProfileIdRivalProfileContainerMap().values());
        Integer presentIndexSize1 = ((WarProfileContainer) rivalProfileContainers.get(0)).getPresentIndexes().size();
        Integer presentIndexSize2 = ((WarProfileContainer) rivalProfileContainers.get(1)).getPresentIndexes().size();
        if (presentIndexSize1.equals(presentIndexSize2)) {
            return null;
        }
        if (presentIndexSize1.compareTo(presentIndexSize2) < 0) {
            return rivalProfileContainers.get(0).getProfile().getTag();
        }
        return rivalProfileContainers.get(1).getProfile().getTag();
    }

    public Optional<Profile> findWinner() {
        List<RivalProfileContainer> rivalProfileContainers = new ArrayList<>(getProfileIdRivalProfileContainerMap().values());
        Integer presentIndexSize1 = ((WarProfileContainer) rivalProfileContainers.get(0)).getPresentIndexes().size();
        Integer presentIndexSize2 = ((WarProfileContainer) rivalProfileContainers.get(1)).getPresentIndexes().size();
        if (presentIndexSize1.equals(presentIndexSize2)) {
            return Optional.empty();
        }
        if (presentIndexSize1 == 0) {
            return Optional.of(rivalProfileContainers.get(1).getProfile());
        }
        return Optional.of(rivalProfileContainers.get(0).getProfile());
    }

    public WarProfileContainer getRivalProfileContainer(Long id) {
        return (WarProfileContainer) super.getRivalProfileContainer(id);
    }

    public void fillModelBasic(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        super.fillModelBasic(model, rivalProfileContainer);
        WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
        model.put("presentIndexes", warProfileContainer.getPresentIndexes());
        model.put("activeIndex", warProfileContainer.getActiveIndex());
        model.put("team", prepareTeam(warProfileContainer.getWisies()));
        model.put("opponentPresentIndexes", getRivalProfileContainer(rivalProfileContainer.getOpponentId()).getPresentIndexes());
        model.put("opponentActiveIndex", getRivalProfileContainer(rivalProfileContainer.getOpponentId()).getActiveIndex());
        model.put("opponentTeam", prepareTeam(getRivalProfileContainer(rivalProfileContainer.getOpponentId()).getWisies()));
    }

    public void fillModelPreparingNextTask(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        super.fillModelPreparingNextTask(model, rivalProfileContainer);
        model.put("opponentActiveIndex", getRivalProfileContainer(rivalProfileContainer.getOpponentId()).getActiveIndex());
    }

    public void fillModelAnswered(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        super.fillModelAnswered(model, rivalProfileContainer);
        WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
        model.put("wisieActions", null);
        model.put("opponentWisieActions", null);
        model.put("presentIndexes", warProfileContainer.getPresentIndexes());
        model.put("opponentPresentIndexes", getRivalProfileContainer(rivalProfileContainer.getOpponentId()).getPresentIndexes());
    }

    public void fillModelAnswering(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        super.fillModelAnswering(model, rivalProfileContainer);
        fillModelWisieAnswering(model, rivalProfileContainer);
    }

    public void fillModelWisieAnswering(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
        model.put("activeIndex", warProfileContainer.getActiveIndex());
        model.put("opponentActiveIndex", getRivalProfileContainer(rivalProfileContainer.getOpponentId()).getActiveIndex());
        WisieAnswerManager manager = getWisieAnswerManager(warProfileContainer);
        if (manager != null) {
            model.put("wisieActions", manager.getActions());
        }
        manager = getWisieAnswerManager(getRivalProfileContainer(rivalProfileContainer.getOpponentId()));
        if (manager != null) {
            model.put("opponentWisieActions", manager.getActions());
        }
    }

    public void fillModelAnsweringTimeout(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        super.fillModelAnsweringTimeout(model, rivalProfileContainer);
        WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
        model.put("wisieActions", null);
        model.put("opponentWisieActions", null);
        model.put("presentIndexes", warProfileContainer.getPresentIndexes());
        model.put("opponentPresentIndexes", getRivalProfileContainer(rivalProfileContainer.getOpponentId()).getPresentIndexes());
    }

    public void fillModelChoosingWhoAnswer(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
        model.put("status", status);
        model.put("choosingWhoAnswerInterval", Math.max(endChoosingWhoAnswerDate.toEpochMilli() - Instant.now().toEpochMilli(), 0L));
        model.put("activeIndex", warProfileContainer.getActiveIndex());
        model.put("presentIndexes", warProfileContainer.getPresentIndexes());
        model.put("isChosenActiveIndex", warProfileContainer.isChosenActiveIndex());
        model.put("task", taskDTOs.get(currentTaskIndex).toTaskMeta());
    }

    public void fillModel(Map<String, Object> model, RivalProfileContainer rivalProfileContainer) {
        super.fillModel(model, rivalProfileContainer);
        if (status == RivalStatus.CHOOSING_WHO_ANSWER) {
            fillModelChoosingWhoAnswer(model, rivalProfileContainer);
        }
    }
}
