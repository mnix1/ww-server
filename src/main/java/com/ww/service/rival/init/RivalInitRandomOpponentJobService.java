package com.ww.service.rival.init;

import com.ww.model.container.Connection;
import com.ww.model.container.ProfileConnection;
import com.ww.model.container.rival.init.RivalOneInit;
import com.ww.model.container.rival.init.RivalTwoInit;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.social.ConnectionService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RivalInitRandomOpponentJobService {
    private static final Logger logger = LoggerFactory.getLogger(RivalInitRandomOpponentJobService.class);
    public static final int RIVAL_INIT_JOB_RATE = 2000;

    private final ConnectionService connectionService;
    private final RivalRunService rivalRunService;
    private final RivalInitRandomOpponentService rivalInitRandomOpponentService;

    @Scheduled(fixedRate = RIVAL_INIT_JOB_RATE)
    private synchronized void maybeInitRival() {
        Map<Long, RivalOneInit> waitingForRivalProfiles = rivalInitRandomOpponentService.getWaitingForRivalProfiles();
        if (waitingForRivalProfiles.isEmpty()) {
//            logger.debug("No waiting for rival profiles");
            return;
        }
        if (waitingForRivalProfiles.size() == 1) {
//            logger.debug("Only one waiting for rival profile");
            return;
        }
        Collection<RivalOneInit> waitingContainers = waitingForRivalProfiles.values();
        RivalOneInit waitingContainer = waitingContainers.stream().findFirst().get();
        Profile profile = waitingContainer.getCreatorProfile();
        Optional<Connection> optionalConnection = connectionService.findByProfileId(profile.getId());
        if (!optionalConnection.isPresent()) {
            waitingForRivalProfiles.remove(profile.getId());
            maybeInitRival();
            return;
        }
        List<Profile> availableOpponentProfiles = waitingContainers.stream()
                .filter(container -> container.getImportance() == waitingContainer.getImportance()
                        && container.getType() == waitingContainer.getType()
                        && !container.getCreatorProfile().equals(profile))
                .map(RivalOneInit::getCreatorProfile)
                .collect(Collectors.toList());
        if (availableOpponentProfiles.size() < 1) {
            return;
        }
        Profile opponent = findOpponentForRival(availableOpponentProfiles, waitingContainer.getCreatorProfile());
        Optional<Connection> optionalOpponentConnection = connectionService.findByProfileId(opponent.getId());
        if (!optionalOpponentConnection.isPresent()) {
            waitingForRivalProfiles.remove(opponent.getId());
            maybeInitRival();
            return;
        }
        logger.trace("Matched profiles {} and {}, now creating rival warManager", profile.getId(), opponent.getId());
        waitingForRivalProfiles.remove(profile.getId());
        waitingForRivalProfiles.remove(opponent.getId());
        RivalTwoInit rival = new RivalTwoInit(waitingContainer.getType(), waitingContainer.getImportance(), profile, opponent);
        rivalRunService.run(rival);
        maybeInitRival();
        return;
    }

    private Profile findOpponentForRival(List<Profile> availableOpponentProfiles, Profile profile) {
        // TODO add more logic
        return availableOpponentProfiles.get(0);
    }


}
