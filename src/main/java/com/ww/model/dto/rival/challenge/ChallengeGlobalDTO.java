package com.ww.model.dto.rival.challenge;

import com.ww.model.constant.rival.challenge.ChallengeProfileResponse;
import com.ww.model.container.Resources;
import com.ww.model.container.rival.challenge.ChallengePosition;
import com.ww.model.entity.outside.rival.challenge.Challenge;
import com.ww.model.entity.outside.rival.challenge.ChallengeProfile;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public class ChallengeGlobalDTO {

    private Long id;
    private Instant closeDate;
    private Long timeoutInterval;
    private Long participants;
    private Resources cost;
    private Resources gain;
    private Boolean canResponse = false;
    private Boolean joined = false;
    private List<ChallengePositionDTO> positions;

    public ChallengeGlobalDTO(Challenge challenge, Optional<ChallengeProfile> optionalChallengeProfile, List<ChallengePosition> positions) {
        this.id = challenge.getId();
        this.closeDate = challenge.getCloseDate();
        this.timeoutInterval = challenge.getTimeoutInterval();
        this.participants = challenge.getParticipants();
        this.cost = challenge.getCostResources();
        this.gain = challenge.getGainResources();
        if (optionalChallengeProfile.isPresent()) {
            ChallengeProfile challengeProfile = optionalChallengeProfile.get();
            canResponse = challengeProfile.getResponseStatus() == ChallengeProfileResponse.OPEN;
            joined = challengeProfile.getJoined();
        }
        this.positions = positions.stream().limit(20).map(ChallengePositionDTO::new).collect(Collectors.toList());
    }
}
