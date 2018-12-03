package com.ww.service.auto.command;

import com.ww.game.auto.AutoManager;
import com.ww.helper.ModelHelper;
import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.constant.rival.challenge.ChallengeAccess;
import com.ww.model.constant.rival.challenge.ChallengeApproach;
import com.ww.model.constant.rival.challenge.ChallengeStatus;
import com.ww.model.constant.social.ResourceType;
import com.ww.model.container.AutoProfileConnection;
import com.ww.model.container.rival.init.RivalOneInit;
import com.ww.model.dto.rival.challenge.ChallengeGlobalDTO;
import com.ww.model.dto.rival.challenge.ChallengePrivateDTO;
import com.ww.service.rival.challenge.ChallengeCreateService;
import com.ww.service.rival.challenge.ChallengeService;
import com.ww.service.rival.global.RivalMessageService;
import com.ww.service.rival.init.RivalInitRandomOpponentService;
import com.ww.service.social.ConnectionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ww.helper.RandomHelper.randomDouble;
import static com.ww.helper.RandomHelper.randomElement;
import static com.ww.service.rival.challenge.ChallengeCreateService.DURATIONS;
import static com.ww.service.rival.challenge.ChallengeCreateService.RESOURCE_COSTS;

@Service
@AllArgsConstructor
public class AutoStartRivalService {

    private final RivalInitRandomOpponentService rivalInitRandomOpponentService;
    private final ConnectionService connectionService;
    private final RivalMessageService rivalMessageService;
    private final ChallengeService challengeService;
    private final ChallengeCreateService challengeCreateService;

    public boolean start(AutoManager manager) {
        AutoProfileConnection autoProfileConnection = connectionService.newAutoProfileConnection(rivalMessageService, manager);
        manager.setConnection(autoProfileConnection);
        Optional<RivalOneInit> optionalRivalOneInit = rivalInitRandomOpponentService.maybeGetRivalInitWaitingLong();
        RivalOneInit init = optionalRivalOneInit.orElse(prepareRandomRivalInit(manager));
        if (init.getType() == RivalType.CHALLENGE) {
            if (randomDouble() > 0.74) {
                return joinPrivateChallenge(manager);
            } else {
                return joinGlobalChallenge(manager);
            }
        }
        return ModelHelper.success(rivalInitRandomOpponentService.start(init.getType(), init.getImportance(), manager.getProfile().getId()));
    }

    private boolean joinGlobalChallenge(AutoManager manager) {
        ChallengeGlobalDTO globalChallenge = challengeService.global(manager.getProfile().getId());
        if (!globalChallenge.getCanJoin()) {
            return joinPrivateChallenge(manager);
        }
        challengeService.join(globalChallenge.getId(), null, manager.getProfile().getId());
        return ModelHelper.success(challengeService.response(globalChallenge.getId(), manager.getProfile().getId()));
    }

    private boolean joinPrivateChallenge(AutoManager manager) {
        List<ChallengePrivateDTO> challenges = challengeService.list(ChallengeStatus.IN_PROGRESS, false, manager.getProfile().getId()).stream()
                .filter(e -> e.getAccess() == ChallengeAccess.UNLOCK)
                .collect(Collectors.toList());
        if (challenges.isEmpty()) {
            challenges = challengeService.list(ChallengeStatus.IN_PROGRESS, true, manager.getProfile().getId()).stream()
                    .filter(e -> e.getAccess() == ChallengeAccess.UNLOCK && e.getName().equals(manager.getProfile().getName()))
                    .collect(Collectors.toList());
            if (challenges.isEmpty()) {
                return createPrivateChallenge(manager);
            }
            return false;
        }
        Collections.shuffle(challenges);
        for (ChallengePrivateDTO challenge : challenges) {
            if (manager.getProfile().hasEnoughResources(challenge.getCost())) {
                challengeService.join(challenge.getId(), null, manager.getProfile().getId());
                return ModelHelper.success(challengeService.response(challenge.getId(), manager.getProfile().getId()));
            }
        }
        if (randomDouble() > 0.9) {
            return createPrivateChallenge(manager);
        }
        return false;
    }

    private boolean createPrivateChallenge(AutoManager manager) {
        challengeCreateService.createPrivate(Collections.emptyList(), ChallengeAccess.UNLOCK, ChallengeApproach.ONE, ResourceType.random(), randomElement(RESOURCE_COSTS), randomElement(DURATIONS), manager.getProfile().getId());
        return false;
    }

    private RivalOneInit prepareRandomRivalInit(AutoManager manager) {
        RivalType type = RivalType.WAR;
        if (randomDouble() > 0.6) {
            type = RivalType.BATTLE;
            if (randomDouble() > 0.6) {
                type = RivalType.CHALLENGE;
            }
        }
        RivalImportance importance = RivalImportance.RANKING;
        if (randomDouble() > 0.95 || type == RivalType.CHALLENGE) {
            importance = RivalImportance.FAST;
        }
        return new RivalOneInit(type, importance, manager.getProfile());
    }
}
