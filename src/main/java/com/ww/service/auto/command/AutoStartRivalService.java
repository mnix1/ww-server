package com.ww.service.auto.command;

import com.ww.game.auto.AutoManager;
import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.container.AutoProfileConnection;
import com.ww.model.container.rival.init.RivalOneInit;
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

    public void start(AutoManager manager) {
        AutoProfileConnection autoProfileConnection = connectionService.newAutoProfileConnection(rivalMessageService, manager);
        manager.setConnection(autoProfileConnection);
        Optional<RivalOneInit> optionalRivalOneInit = rivalInitRandomOpponentService.maybeGetRivalInitWaitingLong();
        RivalOneInit init = optionalRivalOneInit.orElse(prepareRandomRivalInit(manager));
        rivalInitRandomOpponentService.start(init.getType(), init.getImportance(), manager.getProfile().getId());
    }

    private RivalOneInit prepareRandomRivalInit(AutoManager manager) {
        RivalType type = RivalType.WAR;
        RivalImportance importance = RivalImportance.RANKING;
        if (randomDouble() > 0.7) {
            type = RivalType.BATTLE;
        }
        if (randomDouble() > 0.7) {
            importance = RivalImportance.FAST;
        }
        return new RivalOneInit(type, importance, manager.getProfile());
    }
}
