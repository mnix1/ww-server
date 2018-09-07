package com.ww.manager.rival.war;

import com.ww.helper.TeamHelper;
import com.ww.manager.rival.RivalManager;
import com.ww.manager.rival.state.*;
import com.ww.manager.rival.war.state.*;
import com.ww.model.container.rival.RivalInitContainer;
import com.ww.model.container.rival.RivalProfileContainer;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.container.rival.war.WarContainer;
import com.ww.model.container.rival.war.WarProfileContainer;
import com.ww.model.entity.social.Profile;
import com.ww.model.entity.wisie.OwnedWisie;
import com.ww.model.entity.wisie.ProfileWisie;
import com.ww.service.rival.war.WarService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.websocket.message.Message;
import io.reactivex.disposables.Disposable;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class WarManager extends RivalManager {

    public WarContainer warContainer;

    protected Disposable choosingWhoAnswerDisposable;

    public WarManager(RivalInitContainer container, WarService warService, ProfileConnectionService profileConnectionService) {
        this.rivalService = warService;
        this.profileConnectionService = profileConnectionService;
        Profile creator = container.getCreatorProfile();
        Long creatorId = creator.getId();
        List<ProfileWisie> creatorWisies = warService.getProfileWisies(creator);
        Profile opponent = container.getOpponentProfile();
        Long opponentId = opponent.getId();
        List<ProfileWisie> opponentWisies = warService.getProfileWisies(opponent);
        this.rivalContainer = new WarContainer();
        this.rivalContainer.storeInformationFromInitContainer(container);
        this.rivalContainer.addProfile(creatorId, new WarProfileContainer(creator, opponentId, prepareTeamMembers(creator, creatorWisies)));
        this.rivalContainer.addProfile(opponentId, new WarProfileContainer(opponent, creatorId, prepareTeamMembers(opponent, opponentWisies)));
        this.warContainer = (WarContainer) this.rivalContainer;
    }

    protected List<TeamMember> prepareTeamMembers(Profile profile, List<? extends OwnedWisie> wisies) {
        return TeamHelper.prepareTeamMembers(profile, wisies, rivalContainer.getImportance(), rivalContainer.getType());
    }

    public void disposeFlowable() {
        super.disposeFlowable();
        if (choosingWhoAnswerDisposable != null) {
            choosingWhoAnswerDisposable.dispose();
            choosingWhoAnswerDisposable = null;
        }
    }

    protected Message getMessageReadyFast() {
        return Message.WAR_READY;
    }

    public boolean isEnd() {
        for (RivalProfileContainer rivalProfileContainer : getRivalProfileContainers()) {
            WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
            if (!warProfileContainer.isAnyPresentMember()) {
                return true;
            }
        }
        return false;
    }

    public synchronized void start() {
        new StateIntro(this).startFlowable().subscribe(aLong1 -> {
            phase2();
        });
    }

    public synchronized void phase1() {
        new StatePreparingNextTask(this).startFlowable().subscribe(aLong2 -> {
            answeringTimeoutDisposable = new WarStateAnswering(this).startFlowable().subscribe(aLong3 -> {
                new WarStateAnsweringTimeout(this).startFlowable().subscribe(aLong4 -> {
                    phase2();
                });
            });
        });
    }

    public synchronized void phase2() {
        if (isEnd()) {
            new StateClose(this).startVoid();
        } else {
            choosingTaskPropsDisposable = new StateChoosingTaskProps(this).startFlowable().subscribe(aLong5 -> {
                boolean randomChooseTaskProps = rivalContainer.randomChooseTaskProps();
                if (randomChooseTaskProps) {
                    phase3();
                } else {
                    new StateChoosingTaskPropsTimeout(this).startVoid();
                    phase3();
                }
            });
        }
    }

    public synchronized void phase3() {
        choosingWhoAnswerDisposable = new WarStateChoosingWhoAnswer(this).startFlowable().subscribe(aLong2 -> {
            phase1();
        });
    }

    public synchronized void wisieAnswered(Long profileId, Long answerId) {
        Map<String, Object> content = new HashMap<>();
        content.put("answerId", answerId.intValue());
        answer(profileId, content);
    }

    public synchronized void answer(Long profileId, Map<String, Object> content) {
        disposeFlowable();
        new WarStateAnswered(this, profileId, content).startFlowable().subscribe(aLong -> {
            phase2();
        });
    }

    public synchronized void chosenTaskProps(Long profileId, Map<String, Object> content) {
        if (new StateChosenTaskProps(this, profileId, content).startBoolean()) {
            disposeFlowable();
            phase3();
        }
    }

    public synchronized void chosenWhoAnswer(Long profileId, Map<String, Object> content) {
        if (new WarStateChosenWhoAnswer(this, profileId, content).startBoolean()) {
            disposeFlowable();
            phase1();
        }
    }

    public synchronized void surrender(Long profileId) {
        warContainer.stopWisieAnswerManager();
        super.surrender(profileId);
    }


    public Integer getIntroInterval() {
        return 5500;
//        return 1050000;
    }

    public Integer getChoosingWhoAnswerInterval() {
        return 10000;
    }

}
