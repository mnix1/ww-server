package com.ww.service.auto.command;

import com.ww.game.auto.AutoManager;
import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.container.AutoProfileConnection;
import com.ww.model.container.rival.init.RivalOneInit;
import com.ww.model.dto.rival.challenge.ChallengeGlobalDTO;
import com.ww.service.rival.challenge.ChallengeService;
import com.ww.service.rival.global.RivalMessageService;
import com.ww.service.rival.init.RivalInitRandomOpponentService;
import com.ww.service.social.ConnectionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.ww.helper.RandomHelper.randomDouble;

@Service
@AllArgsConstructor
public class AutoStartRivalService {

    private final RivalInitRandomOpponentService rivalInitRandomOpponentService;
    private final ConnectionService connectionService;
    private final RivalMessageService rivalMessageService;
    private final ChallengeService challengeService;

    public void start(AutoManager manager) {
        AutoProfileConnection autoProfileConnection = connectionService.newAutoProfileConnection(rivalMessageService, manager);
        manager.setConnection(autoProfileConnection);
        Optional<RivalOneInit> optionalRivalOneInit = rivalInitRandomOpponentService.maybeGetRivalInitWaitingLong();
        RivalOneInit init = optionalRivalOneInit.orElse(prepareRandomRivalInit(manager));
        if (init.getType() == RivalType.CHALLENGE) {
            ChallengeGlobalDTO globalChallenge = challengeService.global(manager.getProfile().getId());
            challengeService.join(globalChallenge.getId(), null, manager.getProfile().getId());
            challengeService.response(globalChallenge.getId(), manager.getProfile().getId());
        } else {
            rivalInitRandomOpponentService.start(init.getType(), init.getImportance(), manager.getProfile().getId());
        }
    }

    private RivalOneInit prepareRandomRivalInit(AutoManager manager) {
        RivalType type = RivalType.WAR;
        if (randomDouble() > 0.7) {
            type = RivalType.BATTLE;
            if (randomDouble() > 0.5) {
                ChallengeGlobalDTO globalChallenge = challengeService.global(manager.getProfile().getId());
                if (globalChallenge.getCanJoin()) {
                    type = RivalType.CHALLENGE;
                }
            }
        }
        RivalImportance importance = RivalImportance.RANKING;
        if (randomDouble() > 0.7 || type == RivalType.CHALLENGE) {
            importance = RivalImportance.FAST;
        }
        return new RivalOneInit(type, importance, manager.getProfile());
    }
}
