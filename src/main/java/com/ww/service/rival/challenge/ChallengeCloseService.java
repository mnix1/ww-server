package com.ww.service.rival.challenge;

import com.ww.model.constant.rival.challenge.*;
import com.ww.model.dto.rival.challenge.*;
import com.ww.model.entity.outside.rival.challenge.Challenge;
import com.ww.model.entity.outside.rival.challenge.ChallengeProfile;
import com.ww.model.entity.outside.social.Profile;
import com.ww.repository.outside.rival.challenge.ChallengeProfileRepository;
import com.ww.repository.outside.rival.challenge.ChallengeRepository;
import com.ww.service.rival.init.RivalRunService;
import com.ww.service.social.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.ww.helper.ModelHelper.*;

@Service
public class ChallengeCloseService {
    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private ChallengeProfileRepository challengeProfileRepository;

    @Autowired
    private ChallengeCreateService challengeCreateService;

    @Autowired
    private RivalChallengeService rivalChallengeService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private RivalRunService rivalRunService;

    public List<ChallengePositionDTO> preparePositions(Challenge challenge) {
        List<ChallengePositionDTO> positions = new ArrayList<>();
        for (ChallengeProfile cp : challengeProfileRepository.findAllByChallenge_Id(challenge.getId())) {
            if (cp.getJoined()) {
                ChallengePositionDTO position = new ChallengePositionDTO(cp);
                positions.add(position);
            }
        }
        positions.sort((o1, o2) -> {
            if (o1.getStatus() != ChallengeProfileResponse.CLOSED) {
                if (o2.getStatus() != ChallengeProfileResponse.CLOSED) {
                    return 0;
                }
                return 1;
            }
            if (o2.getStatus() != ChallengeProfileResponse.CLOSED) {
                return -1;
            }
            if (o1.getScore().equals(o2.getScore())) {
                return o2.getInterval().compareTo(o1.getInterval());
            }
            return o2.getScore().compareTo(o1.getScore());
        });
        return positions;
    }

    public void maybeCloseChallenge(Challenge challenge, Instant closeDate) {
        List<ChallengeProfile> challengeProfiles = challengeProfileRepository.findAllByChallenge_Id(challenge.getId());
        if (challenge.getAccess() == ChallengeAccess.INVITE && challengeProfiles.stream().anyMatch(challengeProfile -> challengeProfile.getResponseStatus() != ChallengeProfileResponse.CLOSED)) {
            return;
        }
        challenge.setStatus(ChallengeStatus.CLOSED);
        challenge.setCloseDate(closeDate);
        challengeRepository.save(challenge);
    }
}
