package com.ww.manager.rival.campaign;

import com.ww.manager.rival.campaign.state.CampaignWarStateChosenWhoAnswer;
import com.ww.manager.rival.war.WarManager;
import com.ww.model.constant.wisie.HeroType;
import com.ww.model.container.rival.RivalInitContainer;
import com.ww.model.container.rival.campaign.CampaignWarContainer;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.dto.wisie.WarProfileWisieDTO;
import com.ww.model.entity.social.Profile;
import com.ww.model.entity.wisie.ProfileWisie;
import com.ww.service.rival.campaign.CampaignWarService;
import com.ww.service.social.ProfileConnectionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CampaignWarManager extends WarManager {
    public static final Long BOT_PROFILE_ID = -1L;

    public CampaignWarContainer campaignWarContainer;

    public CampaignWarManager(RivalInitContainer container, CampaignWarService campaignWarService, ProfileConnectionService profileConnectionService) {
        super(container, campaignWarService, profileConnectionService);
//        this.rivalService = campaignService;
//        this.profileConnectionService = profileConnectionService;
//        Long creatorId = container.getCreatorProfile().getId();
//        this.rivalContainer = new CampaignWarContainer();
//        this.rivalContainer.storeInformationFromInitContainer(container);
//        this.rivalContainer.addProfile(creatorId, new WarProfileContainer(container.getCreatorProfile(), campaignService.getProfileWisies(creatorId), null));
//        this.warContainer = (WarContainer) this.rivalContainer;
//        this.campaignWarContainer = (CampaignWarContainer) this.rivalContainer;
    }

    protected List<TeamMember> prepareTeamMembers(Profile profile, List<ProfileWisie> wisies) {
        if (!profile.getId().equals(BOT_PROFILE_ID)) {
            return super.prepareTeamMembers(profile, wisies);
        }
        List<TeamMember> teamMembers = new ArrayList<>();
        int index = 0;
        for (ProfileWisie wisie : wisies) {
            teamMembers.add(new TeamMember(index++, HeroType.WISIE, wisie, new WarProfileWisieDTO(wisie)));
        }
        return teamMembers;
    }

//    public synchronized void start() {
//        new StateIntro(this).startFlowable().subscribe(aLong1 -> {
//            phase3();
//        });
//    }
//
//    public synchronized void phase1() {
//        new StatePreparingNextTask(this).startFlowable().subscribe(aLong2 -> {
//            answeringTimeoutDisposable = new WarStateAnswering(this).startFlowable().subscribe(aLong3 -> {
//                new WarStateAnsweringTimeout(this).startFlowable().subscribe(aLong4 -> {
//                    phase2();
//                });
//            });
//        });
//    }
//
//    public synchronized void phase2() {
//        if (isEnd()) {
//            new StateClose(this).startVoid();
//        } else {
//            choosingTaskPropsDisposable = new StateChoosingTaskProps(this).startFlowable().subscribe(aLong5 -> {
//                boolean randomChooseTaskProps = rivalContainer.randomChooseTaskProps();
//                if (randomChooseTaskProps) {
//                    phase3();
//                } else {
//                    new StateChoosingTaskPropsTimeout(this).startVoid();
//                    phase3();
//                }
//            });
//        }
//    }
//
//    public synchronized void phase3() {
//        choosingWhoAnswerDisposable = new WarStateChoosingWhoAnswer(this).startFlowable().subscribe(aLong2 -> {
//            phase1();
//        });
//    }

    public synchronized void chosenWhoAnswer(Long profileId, Map<String, Object> content) {
        if (new CampaignWarStateChosenWhoAnswer(this, profileId, content).startBoolean()) {
            disposeFlowable();
            phase1();
        }
    }

}
