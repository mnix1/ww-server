package com.ww.model.dto.rival.challenge;

import com.ww.model.constant.rival.challenge.ChallengeApproach;
import com.ww.model.constant.rival.challenge.ChallengeProfileResponse;
import com.ww.model.container.Resources;
import com.ww.model.container.rival.challenge.ChallengePosition;
import com.ww.model.entity.outside.rival.challenge.Challenge;
import com.ww.model.entity.outside.rival.challenge.ChallengeProfile;
import com.ww.model.entity.outside.social.Profile;
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
    private ChallengeApproach approach;
    private Resources cost;
    private Resources gain;
    private Boolean canResponse = false;
    private Boolean canTryAgain = false;
    private Boolean joined = false;
    private Boolean canJoin;
    private List<ChallengePositionDTO> positions;

    public ChallengeGlobalDTO(Challenge challenge, Optional<ChallengeProfile> optionalChallengeProfile, List<ChallengePosition> positions, Profile profile) {
        this.id = challenge.getId();
        this.closeDate = challenge.getCloseDate();
        this.timeoutInterval = challenge.getTimeoutInterval();
        this.participants = challenge.getParticipants();
        this.cost = challenge.getCostResources();
        this.gain = challenge.getGainResources();
        this.approach = challenge.getApproach();
        if (optionalChallengeProfile.isPresent()) {
            ChallengeProfile challengeProfile = optionalChallengeProfile.get();
            joined = challengeProfile.getJoined();
            canResponse = joined && challengeProfile.getResponseStatus() == ChallengeProfileResponse.OPEN;
            canTryAgain = joined && approach == ChallengeApproach.MANY && challengeProfile.getResponseStatus() == ChallengeProfileResponse.CLOSED;
        }
        this.canJoin = !joined && profile.hasEnoughResources(cost);
        String tag = optionalChallengeProfile.isPresent() ? optionalChallengeProfile.get().getProfile().getTag() : "";
        this.positions = positions.stream().limit(20).map((ChallengePosition challengePosition) -> new ChallengePositionDTO(challengePosition, tag)).collect(Collectors.toList());
    }
}
