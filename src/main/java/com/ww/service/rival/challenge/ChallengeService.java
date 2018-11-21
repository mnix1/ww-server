package com.ww.service.rival.challenge;

import com.ww.helper.TagHelper;
import com.ww.model.constant.rival.challenge.*;
import com.ww.model.dto.rival.challenge.*;
import com.ww.model.entity.outside.rival.challenge.Challenge;
import com.ww.model.entity.outside.rival.challenge.ChallengeProfile;
import com.ww.model.entity.outside.social.Profile;
import com.ww.repository.outside.rival.challenge.ChallengeProfileRepository;
import com.ww.repository.outside.rival.challenge.ChallengeRepository;
import com.ww.service.auto.AutoService;
import com.ww.service.rival.init.RivalRunService;
import com.ww.service.social.ProfileService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@AllArgsConstructor
public class ChallengeService {
    private static Logger logger = LoggerFactory.getLogger(ChallengeService.class);
    private final ChallengeRepository challengeRepository;
    private final ChallengeProfileRepository challengeProfileRepository;
    private final ChallengeCreateService challengeCreateService;
    private final ChallengeCloseService challengeCloseService;
    private final RivalChallengeService rivalChallengeService;
    private final ProfileService profileService;
    private final RivalRunService rivalRunService;

    @Transactional
    public ChallengeGlobalDTO global(Long profileId) {
        Profile profile = profileService.getProfile(profileId);
        Optional<Challenge> optionalChallenge = challengeRepository.findFirstByTypeAndStatus(ChallengeType.GLOBAL, ChallengeStatus.IN_PROGRESS);
        Challenge challenge = optionalChallenge.orElseGet(challengeCreateService::createGlobal);
        Optional<ChallengeProfile> optionalChallengeProfile = challengeProfileRepository.findByProfile_IdAndChallenge_Id(profileId, challenge.getId());
        return new ChallengeGlobalDTO(challenge, optionalChallengeProfile, challengeCloseService.preparePositions(challenge), profile);
    }

    @Transactional
    public Map<String, Object> response(Long challengeId, Long profileId) {
        logger.debug("response id={}, profileId={}", challengeId, profileId);
        Map<String, Object> model = new HashMap<>();
        Optional<ChallengeProfile> optionalChallengeProfile = challengeProfileRepository.findByProfile_IdAndChallenge_Id(profileId, challengeId);
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
                challengeProfile = new ChallengeProfile(challenge, profileService.getProfile(profileId), ChallengeProfileType.JOINED);
            } else {
                return putErrorCode(model);
            }
        }
        if (challengeProfile.getResponseStatus() != ChallengeProfileResponse.OPEN || !challengeProfile.getJoined()) {
            return putErrorCode(model);
        }
        logger.debug("response success id={}, profileId={}", challengeId, profileId);
        challengeProfile.setResponseStatus(ChallengeProfileResponse.IN_PROGRESS);
        challengeProfile.setResponseStart(Instant.now());
        challengeProfileRepository.save(challengeProfile);
        rivalRunService.run(rivalChallengeService.init(challengeProfile));
        return putSuccessCode(model);
    }

    public List<ChallengePrivateDTO> list(ChallengeStatus status, Boolean participant, Long profileId) {
        if (status == ChallengeStatus.IN_PROGRESS) {
            if (participant) {
                return listActive(profileId);
            }
            return listPrivate(profileId);
        }
        return listHistory(profileId);
    }

    private List<ChallengePrivateDTO> listPrivate(Long profileId) {
        List<Challenge> challenges = challengeRepository.findAllByTypeAndStatusAndAccessIn(ChallengeType.PRIVATE, ChallengeStatus.IN_PROGRESS, Arrays.asList(ChallengeAccess.LOCK, ChallengeAccess.UNLOCK));
        Set<Long> challengeIds = challengeProfileRepository.findAllByProfile_IdAndChallenge_IdIn(profileId, challenges.stream().map(Challenge::getId).collect(Collectors.toList()))
                .stream().map(challengeProfile -> challengeProfile.getChallenge().getId()).collect(Collectors.toSet());
        return challenges.stream()
                .filter(challenge -> !challengeIds.contains(challenge.getId()) && challenge.getTimeoutInterval() > 60 * 1000)
                .limit(10)
                .map(ChallengePrivateDTO::new)
                .collect(Collectors.toList());
    }

    private List<ChallengePrivateDTO> listActive(Long profileId) {
        List<ChallengeProfile> challengeProfiles = challengeProfileRepository.findAllByProfile_IdAndChallenge_TypeAndChallenge_Status(profileId, ChallengeType.PRIVATE, ChallengeStatus.IN_PROGRESS);
        return challengeProfiles.stream()
                .map(ChallengeActiveDTO::new)
                .collect(Collectors.toList());
    }

    private List<ChallengePrivateDTO> listHistory(Long profileId) {
        List<ChallengeProfile> challengeProfiles = challengeProfileRepository.findAllByProfile_IdAndChallenge_TypeAndJoinedAndChallenge_StatusOrderByChallenge_CloseDateDesc(profileId, ChallengeType.PRIVATE, true, ChallengeStatus.CLOSED);
        return challengeProfiles.stream()
                .limit(10)
                .map(challengeProfile -> new ChallengePrivateDTO(challengeProfile.getChallenge()))
                .distinct()
                .collect(Collectors.toList());
    }

    @Transactional
    public Map<String, Object> tryAgain(Long challengeId, Long profileId) {
        Map<String, Object> model = new HashMap<>();
        Optional<Challenge> optionalChallenge = challengeRepository.findById(challengeId);
        if (!optionalChallenge.isPresent()) {
            return putErrorCode(model);
        }
        Challenge challenge = optionalChallenge.get();
        Profile profile = profileService.getProfile(profileId);
        if (challenge.getApproach() != ChallengeApproach.MANY) {
            return putErrorCode(model);
        }
        if (challenge.getStatus() != ChallengeStatus.IN_PROGRESS || challenge.getTimeoutInterval() <= 0) {
            return putCode(model, -2);
        } else if (!profile.hasEnoughResources(challenge.getCostResources())) {
            return putCode(model, -3);
        }
        Optional<ChallengeProfile> optionalChallengeProfile = challengeProfileRepository.findByProfile_IdAndChallenge_Id(profile.getId(), challengeId);
        if (!optionalChallengeProfile.isPresent()) {
            return putErrorCode(model);
        }
        ChallengeProfile challengeProfile = optionalChallengeProfile.get();
        challengeProfileRepository.delete(challengeProfile);
        ChallengeProfile nextTryChallengeProfile = new ChallengeProfile(challenge, profile, challengeProfile.getType());
        nextTryChallengeProfile.join();
        challengeProfileRepository.save(nextTryChallengeProfile);
        challenge.joined();
        challengeRepository.save(challenge);
        profile.subtractResources(challenge.getCostResources());
        profileService.save(profile);
        response(challengeId, profileId);
        return putSuccessCode(model);
    }

    @Transactional
    public Map<String, Object> join(Long challengeId, String creatorTag, Long profileId) {
        logger.debug("join id={}, profileId={}", challengeId, profileId);
        Map<String, Object> model = new HashMap<>();
        Optional<Challenge> optionalChallenge = challengeRepository.findById(challengeId);
        if (!optionalChallenge.isPresent()) {
            return putErrorCode(model);
        }
        Challenge challenge = optionalChallenge.get();
        Profile profile = profileService.getProfile(profileId);
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
        logger.debug("join success id={}, profileId={}", challengeId, profileId);
        challengeProfile.join();
        challengeProfileRepository.save(challengeProfile);
        challenge.joined();
        challengeRepository.save(challenge);
        profile.subtractResources(challenge.getCostResources());
        profileService.save(profile);
        return putSuccessCode(model);
    }

    public ChallengeSummaryDTO summary(Long challengeId,Long profileId) {
        Optional<ChallengeProfile> optionalChallengeProfile = challengeProfileRepository.findByProfile_IdAndChallenge_Id(profileId, challengeId);
        return optionalChallengeProfile.map(this::summary).orElse(null);
    }

    public ChallengeSummaryDTO summary(ChallengeProfile challengeProfile) {
        Challenge challenge = challengeProfile.getChallenge();
        return new ChallengeSummaryDTO(challenge, challengeCloseService.preparePositions(challenge), challengeProfile.getProfile().getTag());
    }

}
