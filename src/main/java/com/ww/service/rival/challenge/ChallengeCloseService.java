package com.ww.service.rival.challenge;

import com.ww.model.constant.rival.challenge.ChallengeAccess;
import com.ww.model.constant.rival.challenge.ChallengeApproach;
import com.ww.model.constant.rival.challenge.ChallengeProfileResponse;
import com.ww.model.constant.rival.challenge.ChallengeStatus;
import com.ww.model.container.Resources;
import com.ww.model.container.rival.challenge.ChallengePosition;
import com.ww.model.entity.outside.rival.challenge.Challenge;
import com.ww.model.entity.outside.rival.challenge.ChallengeProfile;
import com.ww.model.entity.outside.social.Profile;
import com.ww.repository.outside.rival.challenge.ChallengeProfileRepository;
import com.ww.repository.outside.rival.challenge.ChallengeRepository;
import com.ww.service.social.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChallengeCloseService {
    private static final int CHALLENGE_CLOSE_JOB_RATE = 30000;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private ChallengeProfileRepository challengeProfileRepository;

    @Autowired
    private ProfileService profileService;

    @Transactional
    @Scheduled(fixedRate = CHALLENGE_CLOSE_JOB_RATE)
    public synchronized void closeChallenges() {
        Instant closeDate = Instant.now();
        List<Challenge> challenges = challengeRepository.findAllByStatusAndTimeoutDateLessThanEqual(ChallengeStatus.IN_PROGRESS, closeDate);
        if (challenges.isEmpty()) {
            return;
        }
        List<Challenge> challengesToClose = new ArrayList<>();
        for (Challenge challenge : challenges) {
            List<ChallengeProfile> challengeProfiles = challengeProfileRepository.findAllByChallenge_IdAndResponseStatus(challenge.getId(), ChallengeProfileResponse.IN_PROGRESS);
            if (challengeProfiles.isEmpty()) {
                closeChallenge(challenge, closeDate);
                challengesToClose.add(challenge);
            }
        }
        challengeRepository.saveAll(challengesToClose);
        for (Challenge challenge : challengesToClose) {
            rewardProfiles(challenge, challengeProfileRepository.findAllByChallenge_Id(challenge.getId()));
        }
    }

    @Transactional
    public void rewardProfiles(Challenge challenge, List<ChallengeProfile> challengeProfiles) {
        List<ChallengePosition> challengePositions = filterNotClosedPositions(preparePositions(challengeProfiles));
        if (challengePositions.isEmpty()) {
            return;
        }
        List<ChallengeProfile> rewardedChallengeProfiles = new ArrayList<>();
        Resources challengeSummaryGain = challenge.getGainResources();
        int profilesWithReward = Math.max(1, challengePositions.size() / 10);
        for (int i = profilesWithReward - 1; i >= 0; i--) {
            ChallengeProfile challengeProfile = challengePositions.get(i).getChallengeProfile();
            Resources resources = challenge.getCostResources();
            if (i == 2) {
                for (int k = 1; k < challengePositions.size() / 6; k++) {
                    resources.add(challenge.getCostResources());
                }
            } else if (i == 1) {
                for (int k = 1; k < challengePositions.size() / 3; k++) {
                    resources.add(challenge.getCostResources());
                }
            } else if (i == 0) {
                resources = challenge.getGainResources();
            }
            rewardedChallengeProfiles.add(challengeProfile);
            challengeProfile.setGainResources(resources);
            challenge.setGainResources(challenge.getGainResources().subtract(resources));
        }
        List<Profile> profiles = new ArrayList<>();
        for (ChallengeProfile challengeProfile : rewardedChallengeProfiles) {
            profiles.add(challengeProfile.getProfile());
            challengeProfile.getProfile().addResources(challengeProfile.getGainResources());
            challengeProfile.setRewarded(true);
        }
        challenge.setGainResources(challengeSummaryGain);
        challengeProfileRepository.saveAll(rewardedChallengeProfiles);
        profileService.save(profiles);
    }

    public List<ChallengePosition> preparePositions(Challenge challenge) {
        return preparePositions(challengeProfileRepository.findAllByChallenge_Id(challenge.getId()));
    }

    public List<ChallengePosition> preparePositions(List<ChallengeProfile> challengeProfiles) {
        List<ChallengePosition> positions = new ArrayList<>();
        for (ChallengeProfile cp : challengeProfiles) {
            if (cp.getJoined()) {
                ChallengePosition position = new ChallengePosition(cp);
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
                return o1.getInterval().compareTo(o2.getInterval());
            }
            return o2.getScore().compareTo(o1.getScore());
        });
        for (int i = 0; i < positions.size(); i++) {
            positions.get(i).setPosition((long) i + 1);
        }
        return positions;
    }

    public List<ChallengePosition> filterNotClosedPositions(List<ChallengePosition> positions) {
        return positions.stream().filter(challengePosition -> challengePosition.getStatus() == ChallengeProfileResponse.CLOSED).collect(Collectors.toList());
    }

    public void closeChallenge(Challenge challenge, Instant closeDate) {
        challenge.setStatus(ChallengeStatus.CLOSED);
        challenge.setCloseDate(closeDate);
    }

    public void maybeCloseChallenge(Challenge challenge, Instant closeDate) {
        if (challenge.getAccess() != ChallengeAccess.INVITE || challenge.getApproach() != ChallengeApproach.ONE) {
            return;
        }
        List<ChallengeProfile> challengeProfiles = challengeProfileRepository.findAllByChallenge_Id(challenge.getId());
        if (challengeProfiles.stream().anyMatch(challengeProfile -> challengeProfile.getResponseStatus() != ChallengeProfileResponse.CLOSED)) {
            return;
        }
        closeChallenge(challenge, closeDate);
        challengeRepository.save(challenge);
        rewardProfiles(challenge, challengeProfiles);
    }
}
