package com.ww.service.rival.challenge;

import com.ww.helper.TagHelper;
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

import static com.ww.helper.ModelHelper.putCode;
import static com.ww.helper.ModelHelper.putErrorCode;
import static com.ww.helper.ModelHelper.putSuccessCode;

@Service
public class ChallengeService {
    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private ChallengeProfileRepository challengeProfileRepository;

    @Autowired
    private ChallengeCreateService challengeCreateService;
    @Autowired
    private ChallengeCloseService challengeCloseService;

    @Autowired
    private RivalChallengeService rivalChallengeService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private RivalRunService rivalRunService;

    @Transactional
    public ChallengeGlobalDTO global() {
        Optional<Challenge> optionalChallenge = challengeRepository.findFirstByTypeAndStatus(ChallengeType.GLOBAL, ChallengeStatus.IN_PROGRESS);
        Challenge challenge = optionalChallenge.orElseGet(challengeCreateService::createGlobal);
        Optional<ChallengeProfile> optionalChallengeProfile = challengeProfileRepository.findByProfile_IdAndChallenge_Id(profileService.getProfileId(), challenge.getId());
        return new ChallengeGlobalDTO(challenge, optionalChallengeProfile, challengeCloseService.preparePositions(challenge));
    }

    public Map<String, Object> response(Long challengeId) {
        Map<String, Object> model = new HashMap<>();
        Optional<ChallengeProfile> optionalChallengeProfile = challengeProfileRepository.findByProfile_IdAndChallenge_Id(profileService.getProfileId(), challengeId);
        ChallengeProfile challengeProfile;
        if (optionalChallengeProfile.isPresent()) {
            challengeProfile = optionalChallengeProfile.get();
        } else {
            Optional<Challenge> optionalChallenge = challengeRepository.findById(challengeId);
            if (!optionalChallenge.isPresent()) {
                return putErrorCode(model);
            }
            Challenge challenge = optionalChallenge.get();
            if (challenge.getType() == ChallengeType.GLOBAL) {
                challengeProfile = new ChallengeProfile(challenge, profileService.getProfile(), ChallengeProfileType.JOINED);
            } else {
                return putErrorCode(model);
            }
        }
        if (challengeProfile.getResponseStatus() != ChallengeProfileResponse.OPEN || !challengeProfile.getJoined()) {
            return putErrorCode(model);
        }
        challengeProfile.setResponseStatus(ChallengeProfileResponse.IN_PROGRESS);
        challengeProfile.setResponseStart(Instant.now());
        challengeProfileRepository.save(challengeProfile);
        rivalRunService.run(rivalChallengeService.init(challengeProfile));
        return putSuccessCode(model);
    }

    public List<ChallengePrivateDTO> list(ChallengeStatus status, Boolean participant) {
        if (status == ChallengeStatus.IN_PROGRESS) {
            if (participant) {
                return listActive();
            }
            return listPrivate();
        }
        return listHistory();
    }

    private List<ChallengePrivateDTO> listPrivate() {
        List<Challenge> challenges = challengeRepository.findAllByTypeAndStatusAndAccessIn(ChallengeType.PRIVATE, ChallengeStatus.IN_PROGRESS, Arrays.asList(ChallengeAccess.LOCK, ChallengeAccess.UNLOCK));
        Set<Long> challengeIds = challengeProfileRepository.findAllByProfile_IdAndChallenge_IdIn(profileService.getProfileId(), challenges.stream().map(Challenge::getId).collect(Collectors.toList()))
                .stream().map(challengeProfile -> challengeProfile.getChallenge().getId()).collect(Collectors.toSet());
        return challenges.stream()
                .filter(challenge -> !challengeIds.contains(challenge.getId()) && challenge.getTimeoutInterval() > 60 * 1000)
                .limit(10)
                .map(ChallengePrivateDTO::new)
                .collect(Collectors.toList());
    }

    private List<ChallengePrivateDTO> listActive() {
        List<ChallengeProfile> challengeProfiles = challengeProfileRepository.findAllByProfile_IdAndChallenge_TypeAndChallenge_Status(profileService.getProfileId(), ChallengeType.PRIVATE, ChallengeStatus.IN_PROGRESS);
        return challengeProfiles.stream()
                .map(ChallengeActiveDTO::new)
                .collect(Collectors.toList());
    }

    private List<ChallengePrivateDTO> listHistory() {
        List<ChallengeProfile> challengeProfiles = challengeProfileRepository.findAllByProfile_IdAndChallenge_TypeAndJoinedAndChallenge_StatusOrderByChallenge_CloseDateDesc(profileService.getProfileId(), ChallengeType.PRIVATE, true, ChallengeStatus.CLOSED);
        return challengeProfiles.stream()
                .limit(10)
                .map(challengeProfile -> new ChallengePrivateDTO(challengeProfile.getChallenge()))
                .distinct()
                .collect(Collectors.toList());
    }

    @Transactional
    public Map<String, Object> join(Long challengeId, String creatorTag) {
        Map<String, Object> model = new HashMap<>();
        Optional<Challenge> optionalChallenge = challengeRepository.findById(challengeId);
        if (!optionalChallenge.isPresent()) {
            return putErrorCode(model);
        }
        Challenge challenge = optionalChallenge.get();
        Profile profile = profileService.getProfile();
        if (challenge.getStatus() != ChallengeStatus.IN_PROGRESS || challenge.getTimeoutInterval() <= 0) {
            return putCode(model, -2);
        } else if (!profile.hasEnoughResources(challenge.getCostResources())) {
            return putCode(model, -3);
        }
        Optional<ChallengeProfile> optionalChallengeProfile = challengeProfileRepository.findByProfile_IdAndChallenge_Id(profile.getId(), challengeId);
        ChallengeProfile challengeProfile;
        if (!optionalChallengeProfile.isPresent()) {
            if (challenge.getAccess() == ChallengeAccess.INVITE) {
                return putErrorCode(model);
            } else if (challenge.getAccess() == ChallengeAccess.LOCK) {
                if (TagHelper.shouldPrepareTag(creatorTag)) {
                    creatorTag = TagHelper.prepareTag(creatorTag);
                } else {
                    return putCode(model, -4);
                }
                if (!TagHelper.isCorrectTag(creatorTag) || !challenge.getCreatorProfile().getTag().equals(creatorTag)) {
                    return putCode(model, -4);
                }
            }
            challengeProfile = new ChallengeProfile(challenge, profile, ChallengeProfileType.JOINED);
        } else {
            challengeProfile = optionalChallengeProfile.get();
        }
        if (challengeProfile.getJoined()) {
            return putErrorCode(model);
        }
        challengeProfile.join();
        challengeProfileRepository.save(challengeProfile);
        challenge.joined();
        challengeRepository.save(challenge);
        profile.subtractResources(challenge.getCostResources());
        profileService.save(profile);
        return putSuccessCode(model);
    }

    public ChallengeSummaryDTO summary(Long challengeId) {
        Optional<ChallengeProfile> optionalChallengeProfile = challengeProfileRepository.findByProfile_IdAndChallenge_Id(profileService.getProfileId(), challengeId);
        return optionalChallengeProfile.map(this::summary).orElse(null);
    }

    public ChallengeSummaryDTO summary(ChallengeProfile challengeProfile) {
        Challenge challenge = challengeProfile.getChallenge();
        return new ChallengeSummaryDTO(challenge, challengeCloseService.preparePositions(challenge));
    }

}
